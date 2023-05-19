package ch.oidc;

import com.google.api.client.auth.oauth2.*;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class OIDCAuthorizationService {
    private static final String clientID = "minio";
    private static final String clientSecret = "password";
    private static final String minioServerUrl = "http://localhost:9000";
    private static final String redirectURI = "http://localhost:9000/oidc/callback";
    private static final String scopes = "openid profile minio-authorization";
    //private static final String tokenEndpoint = "http://localhost:8080/auth/realms/cyberduckrealm/protocol/openid-connect/token";
    private static final String tokenEndpoint = "http://localhost:8080/realms/cyberduckrealm/protocol/openid-connect/token";
    private static final String authorizationEndpoint = "http://localhost:8080/realms/cyberduckrealm/protocol/openid-connect/auth";

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

    // Exchanges the authorization code for the access and id token
    public static OAuthTokens exchangeAuthorizationCodeForTokens(String authorizationCode) throws IOException {
        TokenResponse response = new AuthorizationCodeTokenRequest(new NetHttpTransport(), jsonFactory,
                new GenericUrl(tokenEndpoint), authorizationCode)
                .setRedirectUri(redirectURI)
                .setClientAuthentication(new BasicAuthentication(clientID, clientSecret))
                .execute();

        // extract tokens and save them in an OAuthTokens object.
        final long expiryInMilliseconds = System.currentTimeMillis() + response.getExpiresInSeconds() * 1000;
        return new OAuthTokens(response.getAccessToken(), response.getRefreshToken(), expiryInMilliseconds);
    }

/*    public static void validateAccessToken(String accessToken) throws IOException {
        HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
        GenericUrl url = new GenericUrl("http://localhost:9000/auth/realms/master/protocol/openid-connect/userinfo");
        HttpRequest request = requestFactory.buildGetRequest(url);
        request.getHeaders().setAuthorization("Bearer " + accessToken);
        HttpResponse response = request.execute();
        String userInfo = response.parseAsString();
        System.out.println(userInfo);
    }*/

    public static OAuthTokens refresh(String refreshToken) throws IOException {
        TokenResponse response = new RefreshTokenRequest(new NetHttpTransport(), jsonFactory,
                new GenericUrl(tokenEndpoint), refreshToken)
                .setClientAuthentication(new ClientParametersAuthentication(clientID, clientSecret))
                .setScopes(Collections.singleton(scopes))
//                .setGrantType("refresh_token")
//                .setRefreshToken(refreshToken)
                .execute();

        // extract tokens and save them in an OAuthTokens object.
        final long expiryInMilliseconds = System.currentTimeMillis() + response.getExpiresInSeconds() * 1000;
        return new OAuthTokens(response.getAccessToken(), response.getRefreshToken(), expiryInMilliseconds);
    }

    public static String minioSts(String accessToken) throws IOException {
        HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
        GenericUrl url = new GenericUrl(minioServerUrl);

        Map<String, String> data = new HashMap<>();
        data.put("Action", "AssumeRoleWithWebIdentity");
        data.put("WebIdentityToken", accessToken);
        data.put("Version", "2011-06-15");
//        data.put("DurationSeconds", "86000");

        HttpContent content = new UrlEncodedContent(data);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept("*/*");

        HttpRequest request = requestFactory.buildPostRequest(url, content);
        request.setHeaders(headers);
        HttpResponse response = request.execute();

        return response.parseAsString();
    }

    // Extract keys and sessionToken for the MinIO connection in the upload
    public static Map<String, String> extractKeysAndToken(String stsResponse) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource inputSource = new InputSource(new StringReader(stsResponse));
        Document document = builder.parse(inputSource);

        Element accessKeyElement = (Element) document.getElementsByTagName("AccessKeyId").item(0);
        String accessKey = accessKeyElement.getTextContent();

        Element secretKeyElement = (Element) document.getElementsByTagName("SecretAccessKey").item(0);
        String secretKey = secretKeyElement.getTextContent();

        Element sessionTokenElement = (Element) document.getElementsByTagName("SessionToken").item(0);
        String sessionToken = sessionTokenElement.getTextContent();

        Map<String, String> keysAndToken = new HashMap<>();
        keysAndToken.put("AccessKey", accessKey);
        keysAndToken.put("SecretKey", secretKey);
        keysAndToken.put("SessionToken", sessionToken);

        return keysAndToken;
    }

}
