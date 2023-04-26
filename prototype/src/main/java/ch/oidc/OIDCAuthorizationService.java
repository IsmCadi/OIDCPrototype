package ch.oidc;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Collections;

public class OIDCAuthorizationService {
    private static final String clientID = "minio";
    private static final String clientSecret = "password";
    private static final String redirectURI = "http://localhost:9000/oidc/callback";
    private static final String scopes = "openid profile minio-authorization";
    private static final String tokenEndpoint = "http://localhost:8080/auth/realms/cyberduckrealm/protocol/openid-connect/token";
    private static final String authorizationEndpoint = "http://localhost:8080/auth/realms/cyberduckrealm/protocol/openid-connect/auth";

    // Sets up the HTTP transport and JSON factory
    private final HttpTransport httpTransport = new NetHttpTransport();
    private static final GsonFactory jsonFactory = new GsonFactory();

    // Builds flow and trigger user authorization request
    public String getAuthorizationUrl() {

        // Sets up the authorization code flow
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
        // Generates the authorization URL
        AuthorizationCodeRequestUrl authorizationUrl = flow.newAuthorizationUrl()
                .setRedirectUri(redirectURI);
        return authorizationUrl.build();
    }

    // Exchanges the authorization code for an access token
/*    public String exchangeAuthorizationCodeForAccessToken(String authorizationCode) throws IOException {
        TokenResponse response = new AuthorizationCodeTokenRequest(new NetHttpTransport(), new GsonFactory(),
                new GenericUrl(tokenEndpoint), authorizationCode)
                .setRedirectUri(redirectURI)
                .setClientAuthentication(new BasicAuthentication(clientID, clientSecret))
                .execute();
        return response.getAccessToken();
    }*/

    // Exchanges the authorization code for the access and id token
    public static String exchangeAuthorizationCodeForAccessToken(String authorizationCode) throws IOException {
        TokenResponse response = new AuthorizationCodeTokenRequest(new NetHttpTransport(), jsonFactory,
                new GenericUrl(tokenEndpoint), authorizationCode)
                .setRedirectUri(redirectURI)
                .setClientAuthentication(new BasicAuthentication(clientID, clientSecret))
                .execute();
        // Gets the access and id token from the response
        String accessToken = response.getAccessToken();
        String idToken = response.get("id_token").toString();
        System.out.println("Access token: " + accessToken);
        System.out.println("ID token: " + idToken);
        return accessToken;
    }

    public static void validateAccessToken(String accessToken) throws IOException {
        HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
        GenericUrl url = new GenericUrl("http://localhost:9001/auth/realms/master/protocol/openid-connect/userinfo");
        HttpRequest request = requestFactory.buildGetRequest(url);
        request.getHeaders().setAuthorization("Bearer " + accessToken);
        HttpResponse response = request.execute();
        String userInfo = response.parseAsString();
        System.out.println(userInfo);
    }
}
