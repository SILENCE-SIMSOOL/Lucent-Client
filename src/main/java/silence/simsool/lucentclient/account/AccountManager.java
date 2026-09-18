package silence.simsool.lucentclient.account;

import static silence.simsool.lucent.Lucent.mc;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import net.minecraft.client.Minecraft;
import net.minecraft.client.User;
import sun.misc.Unsafe;

public class AccountManager {

	private static final AccountManager INSTANCE = new AccountManager();
	private final List<Account> accounts = new ArrayList<>();
	private Account activeAccount;
	private String statusMessage = null;
	private boolean loggingIn = false;

	public static AccountManager getInstance() {
		return INSTANCE;
	}

	private AccountManager() {
		load();
	}

	public synchronized void load() {
		accounts.clear();
		AccountStorage.StorageData data = AccountStorage.load();
		if (data != null && data.accounts != null && !data.accounts.isEmpty()) {
			accounts.addAll(data.accounts);
			if (data.activeAccountId != null) {
				for (Account acc : accounts) {
					if (acc.getId().equals(data.activeAccountId)) {
						activeAccount = acc;
						break;
					}
				}
			}
			if (activeAccount == null && !accounts.isEmpty()) {
				activeAccount = accounts.get(0);
			}
		}

		if (activeAccount == null) {
			if (mc != null && mc.getUser() != null) {
				User user = mc.getUser();
				Account initial = new Account(user.getProfileId(), user.getName(), user.getAccessToken(), "", Long.MAX_VALUE);
				accounts.add(initial);
				activeAccount = initial;
				save();
			}
		} else applySession(activeAccount);
	}

	public synchronized void save() {
		AccountStorage.StorageData data = new AccountStorage.StorageData();
		data.accounts = new ArrayList<>(accounts);
		data.activeAccountId = activeAccount != null ? activeAccount.getId() : null;
		AccountStorage.save(data);
	}

	public synchronized List<Account> getAccounts() {
		return Collections.unmodifiableList(new ArrayList<>(accounts));
	}

	public synchronized Account getActiveAccount() {
		return activeAccount;
	}

	public synchronized void switchAccount(Account account) {
		if (account == null) return;
		this.activeAccount = account;
		applySession(account);
		save();

		if (account.isExpired() && account.getRefreshToken() != null && !account.getRefreshToken().isEmpty()) {
			CompletableFuture.runAsync(() -> {
				try {
					MicrosoftAuthService.refresh(account);
					applySession(account);
					save();
				} catch (Exception ignored) {}
			});
		}
	}

	public synchronized void addAccount(Account account) {
		accounts.removeIf(a -> a.getId().equals(account.getId()));
		accounts.add(account);
		switchAccount(account);
	}

	public synchronized void removeAccount(Account account) {
		accounts.removeIf(a -> a.getId().equals(account.getId()));
		if (activeAccount != null && activeAccount.getId().equals(account.getId())) {
			if (!accounts.isEmpty()) switchAccount(accounts.get(0));
			else activeAccount = null;
		}
		save();
	}

	public void startAddAccount() {
		if (loggingIn) return;
		loggingIn = true;
		statusMessage = "Opening browser...";

		MicrosoftAuthService.startOAuthFlow(account -> {
			loggingIn = false;
			statusMessage = null;
			addAccount(account);
		}, error -> {
			loggingIn = false;
			statusMessage = error.contains("timed out") ? "Timed out" : "Login failed";
			CompletableFuture.runAsync(() -> {
				try {
					Thread.sleep(2500);
				} catch (InterruptedException ignored) {}
				if (statusMessage != null && (statusMessage.equals("Login failed") || statusMessage.equals("Timed out"))) {
					statusMessage = null;
				}
			});
		});
	}

	public synchronized void cancelAddAccount() {
		if (loggingIn) {
			MicrosoftAuthService.cancel();
			loggingIn = false;
			statusMessage = null;
		}
	}

	public boolean isLoggingIn() {
		return loggingIn;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	private void applySession(Account account) {
		if (mc == null) return;
		try {
			User newUser = new User(
				account.getUsername(),
				account.getId(),
				account.getAccessToken() != null ? account.getAccessToken() : "",
				Optional.empty(),
				Optional.empty()
			);

			// Safely update non-static final field using Unsafe to prevent IllegalAccessError on Java 21+
			Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
			unsafeField.setAccessible(true);

			Field targetField = null;
			for (Field f : Minecraft.class.getDeclaredFields()) {
				if (f.getType() == User.class) {
					targetField = f;
					break;
				}
			}
			if (targetField != null) {
				MethodHandles.Lookup lookup = MethodHandles.privateLookupIn(Minecraft.class, MethodHandles.lookup());
				VarHandle handle = lookup.unreflectVarHandle(targetField);
				handle.set(mc, newUser);
			}
		} catch (Throwable ignored) {}
	}

}