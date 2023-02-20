package me.waterbroodje.waterlinker.utilities;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class DiscordLinkManager {
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final int redirectPort;

    public DiscordLinkManager(String clientId, String clientSecret, String redirectUri, int redirectPort) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.redirectPort = redirectPort;
    }

    public String createAuthUrl() {
        String encodedRedirectUri = URLEncoder.encode(redirectUri, StandardCharsets.UTF_8);
        return String.format("https://discord.com/api/oauth2/authorize?client_id=%s&redirect_uri=%s&response_type=code&scope=identify", clientId, encodedRedirectUri);
    }

    public String getAccessToken(String authCode) throws IOException {
        String encodedRedirectUri = URLEncoder.encode(redirectUri, StandardCharsets.UTF_8);
        String body = String.format("client_id=%s&client_secret=%s&grant_type=authorization_code&code=%s&redirect_uri=%s&scope=identify",
                                    clientId, clientSecret, authCode, encodedRedirectUri);
        String response = HttpUtil.post("https://discord.com/api/oauth2/token", body);
        return JsonUtil.getString(response, "access_token");
    }

    public void startRedirectListener(AccessTokenCallback callback) {
        try {
            ServerSocket serverSocket = new ServerSocket(redirectPort);
            Socket socket = serverSocket.accept();
            String request = HttpUtil.readRequest(socket.getInputStream());
            String authCode = HttpUtil.getParameter(request, "code");
            String accessToken = getAccessToken(authCode);
            callback.onAccessToken(accessToken);
            String response = "HTTP/1.1 200 OK\r\nContent-Length: 2\r\n\r\nOK";
            socket.getOutputStream().write(response.getBytes());
            socket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface AccessTokenCallback {
        void onAccessToken(String accessToken);
    }
}
