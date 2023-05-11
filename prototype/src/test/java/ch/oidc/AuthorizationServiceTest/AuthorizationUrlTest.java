package ch.oidc.AuthorizationServiceTest;

import ch.oidc.OIDCAuthorizationService;
import org.junit.Test;


import static org.junit.Assert.*;

public class AuthorizationUrlTest {

    @Test
    public void testGetAuthorizationUrlNotNullOrEmpty() {
        OIDCAuthorizationService authorizationService = new OIDCAuthorizationService();
        String authorizationUrl = authorizationService.getAuthorizationUrl();

        assertNotNull(authorizationUrl);
        assertNotEquals("", authorizationUrl);
    }

    @Test
    public void testGetAuthorizationUrlHasCorrectFormat() {
        OIDCAuthorizationService authorizationService = new OIDCAuthorizationService();
        String authorizationUrl = authorizationService.getAuthorizationUrl();

        assertTrue(authorizationUrl.startsWith("http://"));
        assertTrue(authorizationUrl.contains("?"));
        assertTrue(authorizationUrl.contains("&"));
    }

    @Test
    public void testGetAuthorizationUrlContainsRequiredParameters() {
        OIDCAuthorizationService authorizationService = new OIDCAuthorizationService();
        String authorizationUrl = authorizationService.getAuthorizationUrl();

        String clientID = "minio";
        String redirectURI = "http://localhost:9000/oidc/callback";
        assertTrue(authorizationUrl.contains("client_id=" + clientID));
        assertTrue(authorizationUrl.contains("redirect_uri=" + redirectURI));
        assertTrue(authorizationUrl.contains("scope=" + "openid"));
    }

    @Test
    public void testGetAuthorizationUrlIncludesAuthorizationParameters() {
        OIDCAuthorizationService authorizationService = new OIDCAuthorizationService();
        String authorizationUrl = authorizationService.getAuthorizationUrl();

        assertTrue(authorizationUrl.contains("response_type=code"));
        //assertTrue(authorizationUrl.contains("state="));
    }

    @Test
    public void testGetAuthorizationUrlIncludesAuthorizationServerEndpoint() {
        OIDCAuthorizationService authorizationService = new OIDCAuthorizationService();
        String authorizationUrl = authorizationService.getAuthorizationUrl();

        String authorizationEndpoint = "http://localhost:8080/auth/realms/cyberduckrealm/protocol/openid-connect/auth";
        assertTrue(authorizationUrl.contains(authorizationEndpoint));
    }

}
