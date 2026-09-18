package silence.simsool.lucentclient.account.ui;

import java.util.List;

import silence.simsool.lucent.general.utils.useful.UMouse;
import silence.simsool.lucent.ui.utils.nvg.Fonts;
import silence.simsool.lucent.ui.utils.nvg.NVGRenderer;
import silence.simsool.lucentclient.account.Account;
import silence.simsool.lucentclient.account.AccountAvatarManager;
import silence.simsool.lucentclient.account.AccountManager;

public class AccountDropdownWidget {

	private static boolean open = false;
	private static final float WIDGET_X = 18.0f;
	private static final float WIDGET_Y = 18.0f;
	private static final float WIDGET_WIDTH = 190.0f;
	private static final float HEADER_HEIGHT = 44.0f;
	private static final float ITEM_HEIGHT = 40.0f;
	private static final float CORNER_RADIUS = 10.0f;

	public static void render(float titleUiScale) {
		AccountManager manager = AccountManager.getInstance();
		Account activeAccount = manager.getActiveAccount();
		List<Account> accounts = manager.getAccounts();

		float mx = UMouse.getNvgScaledX(titleUiScale);
		float my = UMouse.getNvgScaledY(titleUiScale);

		// 1. Main Header Box
		boolean headerHover = isPointInside(mx, my, WIDGET_X, WIDGET_Y, WIDGET_WIDTH, HEADER_HEIGHT);
		int headerBg = headerHover ? 0xEE181B23 : 0xDD14161E;

		NVGRenderer.rect(WIDGET_X, WIDGET_Y, WIDGET_WIDTH, HEADER_HEIGHT, headerBg, CORNER_RADIUS);

		// Avatar & Name
		String currentName = activeAccount != null ? activeAccount.getUsername() : "Guest";
		AccountAvatarManager.renderAvatar(activeAccount != null ? activeAccount.getId() : null, currentName, WIDGET_X + 8.0f, WIDGET_Y + 8.0f, 28.0f, 6.0f);
		NVGRenderer.text(currentName, WIDGET_X + 42.0f, WIDGET_Y + 16.0f, Fonts.PRETENDARD_MEDIUM, 0xFFFFFFFF, 14.0f);

		// Chevron Arrow
		float arrowCenterX = WIDGET_X + WIDGET_WIDTH - 18.0f;
		float arrowCenterY = WIDGET_Y + HEADER_HEIGHT / 2.0f;
		int arrowColor = headerHover ? 0xFFFFFFFF : 0xBB8F96A6;
		drawChevron(arrowCenterX, arrowCenterY, open, arrowColor);

		// 2. Dropdown Menu
		if (open) {
			float dropdownY = WIDGET_Y + HEADER_HEIGHT + 6.0f;
			int totalItems = accounts.size() + 1; // accounts + add button
			float dropdownHeight = 8.0f + totalItems * ITEM_HEIGHT + 8.0f;

			NVGRenderer.rect(WIDGET_X, dropdownY, WIDGET_WIDTH, dropdownHeight, 0xF5111319, 12.0f);

			float curY = dropdownY + 8.0f;

			// Accounts List
			for (Account acc : accounts) {
				boolean isActive = activeAccount != null && acc.getId().equals(activeAccount.getId());
				boolean itemHover = isPointInside(mx, my, WIDGET_X + 6.0f, curY, WIDGET_WIDTH - 12.0f, ITEM_HEIGHT);

				if (itemHover) {
					NVGRenderer.rect(WIDGET_X + 6.0f, curY, WIDGET_WIDTH - 12.0f, ITEM_HEIGHT, 0x22FFFFFF, 8.0f);
				} else if (isActive) {
					NVGRenderer.rect(WIDGET_X + 6.0f, curY, WIDGET_WIDTH - 12.0f, ITEM_HEIGHT, 0x14FFFFFF, 8.0f);
				}

				// Account Avatar & Name
				AccountAvatarManager.renderAvatar(acc.getId(), acc.getUsername(), WIDGET_X + 12.0f, curY + 7.0f, 26.0f, 6.0f);
				int nameColor = isActive ? 0xFFFFFFFF : (itemHover ? 0xFFF0F2F8 : 0xCCD0D5E0);
				NVGRenderer.text(acc.getUsername(), WIDGET_X + 42.0f, curY + 14.0f, Fonts.PRETENDARD, nameColor, 14.0f);

				// Active Checkmark or Delete button on hover
				if (isActive) {
					drawCheckmark(WIDGET_X + WIDGET_WIDTH - 20.0f, curY + ITEM_HEIGHT / 2.0f, 0xFF5C8FFF);
				} else if (itemHover && accounts.size() > 1) {
					// Delete button (X)
					boolean deleteHover = isPointInside(mx, my, WIDGET_X + WIDGET_WIDTH - 28.0f, curY + 10.0f, 20.0f, 20.0f);
					int xColor = deleteHover ? 0xFFFF6060 : 0x88AAAAAA;
					drawXMark(WIDGET_X + WIDGET_WIDTH - 18.0f, curY + ITEM_HEIGHT / 2.0f, xColor);
				}

				curY += ITEM_HEIGHT;
			}

			// Add Account Row
			boolean addHover = isPointInside(mx, my, WIDGET_X + 6.0f, curY, WIDGET_WIDTH - 12.0f, ITEM_HEIGHT);
			if (addHover) {
				NVGRenderer.rect(WIDGET_X + 6.0f, curY, WIDGET_WIDTH - 12.0f, ITEM_HEIGHT, 0x22FFFFFF, 8.0f);
			}

			if (manager.isLoggingIn()) {
				NVGRenderer.circle(WIDGET_X + 25.0f, curY + ITEM_HEIGHT / 2.0f, 9.0f, 0x335C8FFF);
				String status = manager.getStatusMessage() != null ? manager.getStatusMessage() : "Opening browser...";
				NVGRenderer.text(status, WIDGET_X + 44.0f, curY + 14.0f, Fonts.PRETENDARD_MEDIUM, 0xFF5C8FFF, 12.0f);

				// Cancel button on hover
				drawXMark(WIDGET_X + WIDGET_WIDTH - 18.0f, curY + ITEM_HEIGHT / 2.0f, addHover ? 0xFFFF6060 : 0x88AAAAAA);
			} else {
				// Plus circle
				float plusCenterX = WIDGET_X + 25.0f;
				float plusCenterY = curY + ITEM_HEIGHT / 2.0f;
				NVGRenderer.circle(plusCenterX, plusCenterY, 11.0f, 0x2EFFFFFF);
				NVGRenderer.line(plusCenterX - 4.0f, plusCenterY, plusCenterX + 4.0f, plusCenterY, 1.6f, 0xFFE0E2EC);
				NVGRenderer.line(plusCenterX, plusCenterY - 4.0f, plusCenterX, plusCenterY + 4.0f, 1.6f, 0xFFE0E2EC);

				int addTextColor = addHover ? 0xFFFFFFFF : 0xFFCCD0DC;
				NVGRenderer.text("Add Account", WIDGET_X + 46.0f, curY + 14.0f, Fonts.PRETENDARD_MEDIUM, addTextColor, 14.0f);
			}
		}
	}

	public static boolean handleMouseClick(float titleUiScale, int button) {
		if (button != 0) return false;

		float mx = UMouse.getNvgScaledX(titleUiScale);
		float my = UMouse.getNvgScaledY(titleUiScale);

		// Header Click
		if (isPointInside(mx, my, WIDGET_X, WIDGET_Y, WIDGET_WIDTH, HEADER_HEIGHT)) {
			open = !open;
			return true;
		}

		// Dropdown Click
		if (open) {
			AccountManager manager = AccountManager.getInstance();
			List<Account> accounts = manager.getAccounts();
			float dropdownY = WIDGET_Y + HEADER_HEIGHT + 6.0f;
			int totalItems = accounts.size() + 1;
			float dropdownHeight = 8.0f + totalItems * ITEM_HEIGHT + 8.0f;

			if (isPointInside(mx, my, WIDGET_X, dropdownY, WIDGET_WIDTH, dropdownHeight)) {
				float curY = dropdownY + 8.0f;
				for (Account acc : accounts) {
					if (isPointInside(mx, my, WIDGET_X + 6.0f, curY, WIDGET_WIDTH - 12.0f, ITEM_HEIGHT)) {
						// Check if delete button was clicked
						if (accounts.size() > 1 && isPointInside(mx, my, WIDGET_X + WIDGET_WIDTH - 28.0f, curY + 10.0f, 20.0f, 20.0f)) {
							manager.removeAccount(acc);
						} else {
							manager.switchAccount(acc);
							open = false;
						}
						return true;
					}
					curY += ITEM_HEIGHT;
				}

				// Add Account Row Click
				if (isPointInside(mx, my, WIDGET_X + 6.0f, curY, WIDGET_WIDTH - 12.0f, ITEM_HEIGHT)) {
					if (manager.isLoggingIn()) {
						manager.cancelAddAccount();
					} else {
						manager.startAddAccount();
					}
					return true;
				}
				return true;
			} else {
				open = false;
			}
		}

		return false;
	}

	private static void drawChevron(float cx, float cy, boolean up, int color) {
		float halfW = 4.5f;
		float h = 3.0f;
		if (up) {
			NVGRenderer.line(cx - halfW, cy + h / 2.0f, cx, cy - h / 2.0f, 1.8f, color);
			NVGRenderer.line(cx, cy - h / 2.0f, cx + halfW, cy + h / 2.0f, 1.8f, color);
		} else {
			NVGRenderer.line(cx - halfW, cy - h / 2.0f, cx, cy + h / 2.0f, 1.8f, color);
			NVGRenderer.line(cx, cy + h / 2.0f, cx + halfW, cy - h / 2.0f, 1.8f, color);
		}
	}

	private static void drawCheckmark(float cx, float cy, int color) {
		NVGRenderer.line(cx - 5.0f, cy, cx - 1.5f, cy + 3.5f, 2.0f, color);
		NVGRenderer.line(cx - 1.5f, cy + 3.5f, cx + 5.0f, cy - 3.5f, 2.0f, color);
	}

	private static void drawXMark(float cx, float cy, int color) {
		float s = 3.5f;
		NVGRenderer.line(cx - s, cy - s, cx + s, cy + s, 1.6f, color);
		NVGRenderer.line(cx - s, cy + s, cx + s, cy - s, 1.6f, color);
	}

	private static boolean isPointInside(float px, float py, float x, float y, float w, float h) {
		return px >= x && px <= x + w && py >= y && py <= y + h;
	}

}