package silence.simsool.lucentclient.account;

import static silence.simsool.lucent.Lucent.mc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.UserApiService;
import com.mojang.authlib.yggdrasil.ProfileResult;

import net.minecraft.client.User;
import net.minecraft.client.multiplayer.ProfileKeyPairManager;
import silence.simsool.lucentclient.mixin.accessors.MinecraftAccessor;

public class AccountManager {

	private static final AccountManager INSTANCE = new AccountManager();
	private final List<Account> accounts = new ArrayList<>();
	private Account activeAccount;
	private Account launcherAccount;
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
		}

		User launcherUser = (mc != null) ? mc.getUser() : null;
		launcherAccount = null;
		if (launcherUser != null && launcherUser.getProfileId() != null) {
			for (Account acc : accounts) {
				if (acc.getId().equals(launcherUser.getProfileId()) || acc.getUsername().equalsIgnoreCase(launcherUser.getName())) {
					launcherAccount = acc;
					break;
				}
			}

			if (launcherAccount != null) {
				launcherAccount.setAccessToken(launcherUser.getAccessToken());
				launcherAccount.setUsername(launcherUser.getName());
				launcherAccount.setLoginFailed(false);
				if (launcherAccount.isExpired()) {
					launcherAccount.setExpiresAt(System.currentTimeMillis() + 86400000L);
				}
			} else {
				launcherAccount = new Account(launcherUser.getProfileId(), launcherUser.getName(), launcherUser.getAccessToken(), "", Long.MAX_VALUE);
				launcherAccount.setLoginFailed(false);
				accounts.add(launcherAccount);
			}
		}

		if (activeAccount == null) {
			activeAccount = launcherAccount != null ? launcherAccount : (!accounts.isEmpty() ? accounts.get(0) : null);
		}

		if (activeAccount != null && activeAccount.isLoginFailed()) {
			Account validAccount = findFirstValidAccount();
			if (validAccount != null) {
				activeAccount = validAccount;
			}
		}

		if (activeAccount != null) {
			applySession(activeAccount);
			refreshAccountIfNeeded(activeAccount);
		}

		validateAndRefreshAccountsAsync();
		save();
	}

	public synchronized Account findFirstValidAccount() {
		for (Account acc : accounts) {
			if (!acc.isLoginFailed()) {
				return acc;
			}
		}
		return null;
	}

	public synchronized boolean areAllAccountsFailed() {
		if (accounts.isEmpty()) return true;
		for (Account acc : accounts) {
			if (!acc.isLoginFailed()) return false;
		}
		return true;
	}

	private void refreshAccountIfNeeded(Account account) {
		if (account == null) return;
		if (account.isExpired()) {
			if (account.getRefreshToken() != null && !account.getRefreshToken().isEmpty()) {
				CompletableFuture.runAsync(() -> {
					try {
						MicrosoftAuthService.refresh(account);
						account.setLoginFailed(false);
						synchronized (AccountManager.this) {
							if (activeAccount != null && activeAccount.getId().equals(account.getId())) {
								applySession(account);
							}
						}
						save();
					} catch (Exception e) {
						account.setLoginFailed(true);
						save();
						synchronized (AccountManager.this) {
							if (activeAccount != null && activeAccount.getId().equals(account.getId())) {
								Account valid = findFirstValidAccount();
								if (valid != null && !valid.getId().equals(account.getId())) {
									switchAccount(valid);
								}
							}
						}
					}
				});
			} else {
				account.setLoginFailed(true);
			}
		}
	}

	private void validateAndRefreshAccountsAsync() {
		CompletableFuture.runAsync(() -> {
			for (Account acc : new ArrayList<>(accounts)) {
				if (acc == activeAccount) continue;
				if (acc.isExpired()) {
					if (acc.getRefreshToken() != null && !acc.getRefreshToken().isEmpty()) {
						try {
							MicrosoftAuthService.refresh(acc);
							acc.setLoginFailed(false);
							save();
						} catch (Exception e) {
							acc.setLoginFailed(true);
							save();
						}
					} else {
						acc.setLoginFailed(true);
					}
				}
			}
		});
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

		if (account.getRefreshToken() != null && !account.getRefreshToken().isEmpty()) {
			if (account.isExpired() || account.isLoginFailed()) {
				CompletableFuture.runAsync(() -> {
					try {
						MicrosoftAuthService.refresh(account);
						account.setLoginFailed(false);
						synchronized (AccountManager.this) {
							if (activeAccount != null && activeAccount.getId().equals(account.getId())) {
								applySession(account);
							}
						}
						save();
					} catch (Exception ignored) {
						account.setLoginFailed(true);
						save();
					}
				});
			}
		}
	}

	public synchronized void addAccount(Account account) {
		account.setLoginFailed(false);
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
		if (mc == null || account == null) return;
		try {
			User newUser = new User(
				account.getUsername(),
				account.getId(),
				account.getAccessToken() != null ? account.getAccessToken() : "",
				Optional.empty(),
				Optional.empty()
			);

			MinecraftAccessor accessor = (MinecraftAccessor) mc;
			accessor.setUser(newUser);

			CompletableFuture<ProfileResult> profileFuture = CompletableFuture.supplyAsync(() -> {
				try {
					if (mc.services() != null && mc.services().sessionService() != null) {
						ProfileResult result = mc.services().sessionService().fetchProfile(account.getId(), true);
						if (result != null) return result;
					}
				} catch (Throwable ignored) {}
				return new ProfileResult(new GameProfile(account.getId(), account.getUsername()));
			});
			accessor.setProfileFuture(profileFuture);

			try {
				UserApiService userApiService = accessor.getUserApiService();
				boolean isLauncherUser = (launcherAccount != null && launcherAccount.getId().equals(account.getId()));
				if (isLauncherUser && userApiService != null && mc.gameDirectory != null) {
					accessor.setProfileKeyPairManager(ProfileKeyPairManager.create(userApiService, newUser, mc.gameDirectory.toPath()));
				} else {
					accessor.setProfileKeyPairManager(ProfileKeyPairManager.EMPTY_KEY_MANAGER);
				}
			} catch (Throwable ignored) {
				try {
					accessor.setProfileKeyPairManager(ProfileKeyPairManager.EMPTY_KEY_MANAGER);
				} catch (Throwable ignored2) {}
			}
		} catch (Throwable ignored) {}
	}

}