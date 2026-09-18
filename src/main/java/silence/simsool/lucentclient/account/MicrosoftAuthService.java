package silence.simsool.lucentclient.account;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpServer;

import silence.simsool.lucent.general.utils.useful.UDesktop;

public class MicrosoftAuthService {

	private static final String CLIENT_ID = "c36a9fb6-4f2a-41ff-90bd-ae7cc92031eb";
	private static final String SCOPE = "XboxLive.signin offline_access";
	private static final String TOKEN_URL = "https://login.live.com/oauth20_token.srf";
	private static final String AUTHORIZE_URL = "https://login.live.com/oauth20_authorize.srf";

	private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
		.connectTimeout(Duration.ofSeconds(15))
		.build();

	private static final ScheduledExecutorService SCHEDULER = Executors.newSingleThreadScheduledExecutor();
	private static HttpServer currentServer = null;
	private static ScheduledFuture<?> timeoutFuture = null;

	private static final String SUCCESS_HTML = "<!DOCTYPE html><html><head><meta charset=\"utf-8\"><title>Lucent Client</title>"
		+ "<style>body{background:#121318;color:#fff;font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,sans-serif;"
		+ "display:flex;align-items:center;justify-content:center;height:100vh;margin:0;}"
		+ ".card{background:#1a1c24;padding:48px 56px;border-radius:18px;text-align:center;box-shadow:0 12px 40px rgba(0,0,0,0.5);border:1px solid #2d313d;max-width:420px;}"
		+ "h1{color:#5c8fff;margin:0 0 16px 0;font-size:26px;}p{color:#b0b4c0;line-height:1.6;font-size:15px;margin:0;}"
		+ ".check{width:60px;height:60px;background:#5c8fff22;color:#5c8fff;border-radius:50%;display:flex;align-items:center;justify-content:center;font-size:32px;margin:0 auto 20px;}</style></head>"
		+ "<body><div class=\"card\"><div class=\"check\">&#10003;</div><h1>Login Successful</h1><p>You have linked your account to <b>LucentClient</b>.<br>You can close this tab and return to the game.</p></div></body></html>";

	public static synchronized void cancel() {
		if (timeoutFuture != null) {
			timeoutFuture.cancel(true);
			timeoutFuture = null;
		}
		if (currentServer != null) {
			try {
				currentServer.stop(0);
			} catch (Exception ignored) {}
			currentServer = null;
		}
	}

	public static void startOAuthFlow(Consumer<Account> onSuccess, Consumer<String> onError) {
		cancel();

		CompletableFuture.runAsync(() -> {
			try {
				byte[] verifierBytes = new byte[32];
				new SecureRandom().nextBytes(verifierBytes);
				String codeVerifier = Base64.getUrlEncoder().withoutPadding().encodeToString(verifierBytes);

				MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
				byte[] challengeBytes = sha256.digest(codeVerifier.getBytes(StandardCharsets.US_ASCII));
				String codeChallenge = Base64.getUrlEncoder().withoutPadding().encodeToString(challengeBytes);

				HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
				currentServer = server;
				int port = server.getAddress().getPort();
				String redirectUri = "http://127.0.0.1:" + port;

				String msAuthUrl = AUTHORIZE_URL
					+ "?client_id=" + CLIENT_ID
					+ "&response_type=code"
					+ "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8)
					+ "&scope=" + URLEncoder.encode(SCOPE, StandardCharsets.UTF_8)
					+ "&prompt=select_account"
					+ "&code_challenge=" + codeChallenge
					+ "&code_challenge_method=S256";

				server.createContext("/", exchange -> {
					try {
						String path = exchange.getRequestURI().getPath();
						if ("/login".equals(path)) {
							exchange.getResponseHeaders().set("Location", msAuthUrl);
							exchange.sendResponseHeaders(302, -1);
							return;
						}

						String query = exchange.getRequestURI().getQuery();
						String code = null;
						if (query != null) {
							for (String param : query.split("&")) {
								String[] pair = param.split("=", 2);
								if (pair.length == 2 && "code".equals(pair[0])) {
									code = pair[1];
									break;
								}
							}
						}

						if (code != null) {
							byte[] responseBytes = SUCCESS_HTML.getBytes(StandardCharsets.UTF_8);
							exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
							exchange.sendResponseHeaders(200, responseBytes.length);
							try (OutputStream os = exchange.getResponseBody()) {
								os.write(responseBytes);
							}

							String finalCode = code;
							CompletableFuture.runAsync(() -> {
								try {
									Account account = exchangeCodeForAccount(finalCode, codeVerifier, redirectUri);
									onSuccess.accept(account);
								} catch (Exception e) {
									onError.accept("Auth failed: " + e.getMessage());
								}
							});
							SCHEDULER.schedule(MicrosoftAuthService::cancel, 1, TimeUnit.SECONDS);
						} else {
							exchange.sendResponseHeaders(400, 0);
							exchange.close();
						}
					} catch (Exception ignored) {
					} finally {
						exchange.close();
					}
				});

				server.start();

				timeoutFuture = SCHEDULER.schedule(() -> {
					cancel();
					onError.accept("Login timed out. Please try again.");
				}, 90, TimeUnit.SECONDS);

				String localGateway = "http://127.0.0.1:" + port + "/login";
				UDesktop.openBrowse(URI.create(localGateway));
			} catch (Exception e) {
				cancel();
				onError.accept("Could not start login: " + e.getMessage());
			}
		});
	}

	private static Account exchangeCodeForAccount(String code, String codeVerifier, String redirectUri) throws Exception {
		String tokenParams = "client_id=" + CLIENT_ID
			+ "&scope=" + URLEncoder.encode(SCOPE, StandardCharsets.UTF_8)
			+ "&code=" + code
			+ "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8)
			+ "&grant_type=authorization_code"
			+ "&code_verifier=" + codeVerifier;

		HttpRequest tokenReq = HttpRequest.newBuilder()
			.uri(URI.create(TOKEN_URL))
			.header("Content-Type", "application/x-www-form-urlencoded")
			.POST(HttpRequest.BodyPublishers.ofString(tokenParams))
			.build();

		HttpResponse<String> tokenRes = HTTP_CLIENT.send(tokenReq, HttpResponse.BodyHandlers.ofString());
		if (tokenRes.statusCode() != 200) {
			throw new RuntimeException("MS Token error: " + tokenRes.body());
		}

		JsonObject msJson = JsonParser.parseString(tokenRes.body()).getAsJsonObject();
		String msAccessToken = msJson.get("access_token").getAsString();
		String refreshToken = msJson.get("refresh_token").getAsString();

		return completeMinecraftAuth(msAccessToken, refreshToken);
	}

	public static Account refresh(Account account) throws Exception {
		String tokenParams = "client_id=" + CLIENT_ID
			+ "&scope=" + URLEncoder.encode(SCOPE, StandardCharsets.UTF_8)
			+ "&refresh_token=" + URLEncoder.encode(account.getRefreshToken(), StandardCharsets.UTF_8)
			+ "&grant_type=refresh_token";

		HttpRequest tokenReq = HttpRequest.newBuilder()
			.uri(URI.create(TOKEN_URL))
			.header("Content-Type", "application/x-www-form-urlencoded")
			.POST(HttpRequest.BodyPublishers.ofString(tokenParams))
			.build();

		HttpResponse<String> tokenRes = HTTP_CLIENT.send(tokenReq, HttpResponse.BodyHandlers.ofString());
		if (tokenRes.statusCode() != 200) {
			throw new RuntimeException("Refresh failed: " + tokenRes.body());
		}

		JsonObject msJson = JsonParser.parseString(tokenRes.body()).getAsJsonObject();
		String msAccessToken = msJson.get("access_token").getAsString();
		String newRefreshToken = msJson.has("refresh_token") ? msJson.get("refresh_token").getAsString() : account.getRefreshToken();

		Account updated = completeMinecraftAuth(msAccessToken, newRefreshToken);
		account.setAccessToken(updated.getAccessToken());
		account.setRefreshToken(updated.getRefreshToken());
		account.setExpiresAt(updated.getExpiresAt());
		account.setUsername(updated.getUsername());
		return account;
	}

	private static Account completeMinecraftAuth(String msAccessToken, String refreshToken) throws Exception {
		// 1. Xbox Live
		JsonObject xblBody = new JsonObject();
		JsonObject xblProps = new JsonObject();
		xblProps.addProperty("AuthMethod", "RPS");
		xblProps.addProperty("SiteName", "user.auth.xboxlive.com");
		xblProps.addProperty("RpsTicket", "d=" + msAccessToken);
		xblBody.add("Properties", xblProps);
		xblBody.addProperty("RelyingParty", "http://auth.xboxlive.com");
		xblBody.addProperty("TokenType", "JWT");

		HttpRequest xblReq = HttpRequest.newBuilder()
			.uri(URI.create("https://user.auth.xboxlive.com/user/authenticate"))
			.header("Content-Type", "application/json")
			.header("Accept", "application/json")
			.POST(HttpRequest.BodyPublishers.ofString(xblBody.toString()))
			.build();

		HttpResponse<String> xblRes = HTTP_CLIENT.send(xblReq, HttpResponse.BodyHandlers.ofString());
		if (xblRes.statusCode() != 200) {
			throw new RuntimeException("Xbox Live error: " + xblRes.body());
		}

		JsonObject xblJson = JsonParser.parseString(xblRes.body()).getAsJsonObject();
		String xblToken = xblJson.get("Token").getAsString();
		String userHash = xblJson.getAsJsonObject("DisplayClaims")
			.getAsJsonArray("xui").get(0).getAsJsonObject()
			.get("uhs").getAsString();

		// 2. XSTS
		JsonObject xstsBody = new JsonObject();
		JsonObject xstsProps = new JsonObject();
		xstsProps.addProperty("SandboxId", "RETAIL");
		JsonArray userTokens = new JsonArray();
		userTokens.add(xblToken);
		xstsProps.add("UserTokens", userTokens);
		xstsBody.add("Properties", xstsProps);
		xstsBody.addProperty("RelyingParty", "rp://api.minecraftservices.com/");
		xstsBody.addProperty("TokenType", "JWT");

		HttpRequest xstsReq = HttpRequest.newBuilder()
			.uri(URI.create("https://xsts.auth.xboxlive.com/xsts/authorize"))
			.header("Content-Type", "application/json")
			.header("Accept", "application/json")
			.POST(HttpRequest.BodyPublishers.ofString(xstsBody.toString()))
			.build();

		HttpResponse<String> xstsRes = HTTP_CLIENT.send(xstsReq, HttpResponse.BodyHandlers.ofString());
		if (xstsRes.statusCode() != 200) {
			throw new RuntimeException("XSTS error: " + xstsRes.body());
		}

		JsonObject xstsJson = JsonParser.parseString(xstsRes.body()).getAsJsonObject();
		String xstsToken = xstsJson.get("Token").getAsString();

		// 3. Minecraft Login
		JsonObject mcLoginBody = new JsonObject();
		mcLoginBody.addProperty("identityToken", "XBL3.0 x=" + userHash + ";" + xstsToken);

		HttpRequest mcLoginReq = HttpRequest.newBuilder()
			.uri(URI.create("https://api.minecraftservices.com/authentication/login_with_xbox"))
			.header("Content-Type", "application/json")
			.header("Accept", "application/json")
			.POST(HttpRequest.BodyPublishers.ofString(mcLoginBody.toString()))
			.build();

		HttpResponse<String> mcLoginRes = HTTP_CLIENT.send(mcLoginReq, HttpResponse.BodyHandlers.ofString());
		if (mcLoginRes.statusCode() != 200) {
			throw new RuntimeException("MC Login error: " + mcLoginRes.body());
		}

		JsonObject mcLoginJson = JsonParser.parseString(mcLoginRes.body()).getAsJsonObject();
		String mcAccessToken = mcLoginJson.get("access_token").getAsString();
		long expiresIn = mcLoginJson.has("expires_in") ? mcLoginJson.get("expires_in").getAsLong() : 86400L;

		// 4. Minecraft Profile
		HttpRequest profileReq = HttpRequest.newBuilder()
			.uri(URI.create("https://api.minecraftservices.com/minecraft/profile"))
			.header("Authorization", "Bearer " + mcAccessToken)
			.header("Accept", "application/json")
			.GET()
			.build();

		HttpResponse<String> profileRes = HTTP_CLIENT.send(profileReq, HttpResponse.BodyHandlers.ofString());
		if (profileRes.statusCode() != 200) {
			throw new RuntimeException("MC Profile error: " + profileRes.body());
		}

		JsonObject profileJson = JsonParser.parseString(profileRes.body()).getAsJsonObject();
		String rawUuid = profileJson.get("id").getAsString();
		String username = profileJson.get("name").getAsString();

		UUID uuid = UUID.fromString(rawUuid.replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
		long expiresAt = System.currentTimeMillis() + (expiresIn * 1000L);

		return new Account(uuid, username, mcAccessToken, refreshToken, expiresAt);
	}

}