package ch.oidc;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.logging.Logger;

public class Authentication {

    private final String clientID = "minio";
    private final String clientSecret = "password";
    private final String redirectURI = "http://localhost:9000/oidc/callback";
    //private final String redirectURI = "http://localhost:8080/callback";
    private final String authorizationEndpoint = "http://localhost:8080/auth/realms/cyberduckrealm/protocol/openid-connect/auth";
    private final String tokenEndpoint = "http://localhost:8080/auth/realms/cyberduckrealm/protocol/openid-connect/token";
    private static final String scope = "openid profile";
    private static final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

    // Authentication request
    public String getAuthorizationUrl() {
        AuthorizationCodeRequestUrl authorizationUrl = new AuthorizationCodeRequestUrl(authorizationEndpoint, clientID)
                .setRedirectUri(redirectURI)
                .setScopes(Collections.singletonList("profile"));
        return authorizationUrl.build();
    }

    // Token request
    public String exchangeAuthorizationCodeForAccessToken(String authorizationCode) throws IOException {
        TokenResponse response = new AuthorizationCodeTokenRequest(new NetHttpTransport(), jsonFactory,
                new GenericUrl(tokenEndpoint), authorizationCode)
                .setRedirectUri(redirectURI)
                .setClientAuthentication(new BasicAuthentication(clientID, clientSecret))
                .execute();
        return response.getAccessToken();
    }

    // Validate the access token after exchange
    public void validateAccessToken(String accessToken) throws IOException {
        HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
        GenericUrl url = new GenericUrl("http://localhost:8080/auth/realms/master/protocol/openid-connect/userinfo");
        HttpRequest request = requestFactory.buildGetRequest(url);
        request.getHeaders().setAuthorization("Bearer " + accessToken);
        HttpResponse response = request.execute();
        String userInfo = response.parseAsString();
        System.out.println(userInfo);
    }
}
