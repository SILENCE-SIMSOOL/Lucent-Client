package silence.simsool.lucentclient.account;

import java.util.UUID;

public class Account {

	private final UUID id;
	private String username;
	private String accessToken;
	private String refreshToken;
	private long expiresAt;

	public Account(UUID id, String username, String accessToken, String refreshToken, long expiresAt) {
		this.id = id;
		this.username = username;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.expiresAt = expiresAt;
	}

	public UUID getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public long getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(long expiresAt) {
		this.expiresAt = expiresAt;
	}

	public boolean isExpired() {
		return System.currentTimeMillis() >= expiresAt - 60000L;
	}

}