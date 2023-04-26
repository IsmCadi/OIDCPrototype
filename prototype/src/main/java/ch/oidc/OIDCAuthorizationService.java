package ch.oidc;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import java.io.IOException;
import java.util.Collections;

public class OIDCAuthorizationService {
    private static final String clientID = "minio";
    private static final String clientSecret = "password";
    private static final String redirectURI = "http://192.168.1.65:9000/oidc/callback";
    private static final String scopes = "openid profile minio-authorization";
    private static final String tokenEndpoint = "http://192.168.1.65:8080/auth/realms/cyberduckrealm/protocol/openid-connect/token";
    private static final String authorizationEndpoint = "http://192.168.1.65:8080/auth/realms/cyberduckrealm/protocol/openid-connect/auth";

    public String getAuthorizationUrl() {
        HttpTransport httpTransport = new NetHttpTransport();
        GsonFactory jsonFactory = new GsonFactory();
        AuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport,
                jsonFactory,
                clientID,
                clientSecret,
                Collections.singleton(scopes))
                .setAccessType("offline")
                .setApprovalPrompt("force")
                .setAuthorizationServerEncodedUrl(authorizationEndpoint)
                .build();
        AuthorizationCodeRequestUrl authorizationUrl = flow.newAuthorizationUrl()
                .setRedirectUri(redirectURI);
        return authorizationUrl.build();
    }

    public String exchangeAuthorizationCodeForAccessToken(String authorizationCode) throws IOException {
        TokenResponse response = new AuthorizationCodeTokenRequest(new NetHttpTransport(), new GsonFactory(),
                new GenericUrl(tokenEndpoint), authorizationCode)
                .setRedirectUri(redirectURI)
                .setClientAuthentication(new BasicAuthentication(clientID, clientSecret))
                .execute();
        return response.getAccessToken();
    }

    public static void validateAccessToken(String accessToken) throws IOException {
        HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
        GenericUrl url = new GenericUrl("http://192.168.1.65:9001/auth/realms/master/protocol/openid-connect/userinfo");
        HttpRequest request = requestFactory.buildGetRequest(url);
        request.getHeaders().setAuthorization("Bearer " + accessToken);
        HttpResponse response = request.execute();
        String userInfo = response.parseAsString();
        System.out.println(userInfo);
    }
}
