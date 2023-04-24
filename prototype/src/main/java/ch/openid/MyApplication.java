package ch.openid;


import com.google.api.client.auth.oauth2.*;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;

import static ch.oidc.Authentication.jsonFactory;

public class MyApplication {

    private static final String MINIO_SERVER_URL = "https://your-minio-server-url.com";
    private static final String KEYCLOAK_AUTHORIZATION_ENDPOINT = "https://your-keycloak-server-url.com/auth/realms/your-realm/protocol/openid-connect/auth";
    private static final String KEYCLOAK_TOKEN_ENDPOINT = "https://your-keycloak-server-url.com/auth/realms/your-realm/protocol/openid-connect/token";
    private static final String clientID = "minio";
    private static final String clientSecret = "password";
    private static final String REDIRECT_URI = "http://localhost:8080/Callback";
    private static final String redirectURI = "http://192.168.1.65:9000/oidc/callback";
    private static final String MINIO_BUCKET_NAME = "your-minio-bucket-name";
    private static final String OBJECT_NAME = "your-object-name";
    private  static final String scopes =  "openid profile minio-authorization";
    private static final String tokenEndpoint = "http://192.168.1.65:8080/auth/realms/cyberduckrealm/protocol/openid-connect/token";

    private static final String authorizationEndpoint = "http://192.168.1.65:8080/auth/realms/cyberduckrealm/protocol/openid-connect/auth";

    public static String exchangeAuthorizationCodeForAccessToken(String authorizationCode) throws IOException {
        TokenResponse response = new AuthorizationCodeTokenRequest(new NetHttpTransport(), jsonFactory,
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

    public static void main(String[] args) throws IOException {
        HttpTransport httpTransport = new NetHttpTransport();
        GsonFactory jsonFactory = new GsonFactory();

        // Set up the OAuth2 client flow
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

        // Generate the authorization URL
        AuthorizationCodeRequestUrl authorizationUrl = flow.newAuthorizationUrl()
                .setRedirectUri(redirectURI);

        System.out.println("Open the following URL in your browser:");
        System.out.println(authorizationUrl);

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the authorization code: ");
        String authorizationCode = scanner.nextLine();


        String accessToken = exchangeAuthorizationCodeForAccessToken(authorizationCode);
        System.out.println(accessToken);
        System.out.println("------------------------------------Bananamarama------------------------------------");
        validateAccessToken(accessToken);
    }
}