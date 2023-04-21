/*
package ch.oidc;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

public class OidcAuthentication {
    private static final String AUTHORIZATION_SERVER_URL = "http://localhost:8080/auth/realms/my-realm/protocol/openid-connect/auth";
    private static final String TOKEN_SERVER_URL = "http://localhost:8080/auth/realms/my-realm/protocol/openid-connect/token";
    private static final String REDIRECT_URI = "http://localhost:8080/callback";

    private static final String CLIENT_ID = "my-client";
    private static final String CLIENT_SECRET = "my-client-secret";

    private static final String SCOPE = "openid profile";

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private final HttpTransport httpTransport;
    private final GoogleClientSecrets clientSecrets;
    private final String stateToken;

    public OidcAuthentication(HttpTransport httpTransport) throws IOException {
        this.httpTransport = httpTransport;
        this.clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new FileReader("keycloak.json"));
        this.stateToken = UUID.randomUUID().toString();
    }

    public String buildAuthorizationUrl() {
        AuthorizationCodeRequestUrl authorizationUrl = new AuthorizationCodeRequestUrl(AUTHORIZATION_SERVER_URL, CLIENT_ID)
                .setScopes(Arrays.asList(SCOPE))
                .setState(stateToken)
                .setRedirectUri(REDIRECT_URI);

        return authorizationUrl.build();
    }
}
*/
