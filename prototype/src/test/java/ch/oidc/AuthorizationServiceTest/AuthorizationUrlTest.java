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
        System.out.println("Test GetAuthorizationUrlNotNullOrEmpty passed");
    }

    @Test
    public void testGetAuthorizationUrlHasCorrectFormat() {
        OIDCAuthorizationService authorizationService = new OIDCAuthorizationService();
        String authorizationUrl = authorizationService.getAuthorizationUrl();

        assertTrue(authorizationUrl.startsWith("http://"));
        assertTrue(authorizationUrl.contains("?"));
        assertTrue(authorizationUrl.contains("&"));
        System.out.println("Test GetAuthorizationUrlHasCorrectFormat passed");
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
        System.out.println("Test GetAuthorizationUrlContainsRequiredParameters passed");
    }

    @Test
    public void testGetAuthorizationUrlIncludesAuthorizationParameters() {
        OIDCAuthorizationService authorizationService = new OIDCAuthorizationService();
        String authorizationUrl = authorizationService.getAuthorizationUrl();

        assertTrue(authorizationUrl.contains("response_type=code"));
        //assertTrue(authorizationUrl.contains("state="));
        System.out.println("Test GetAuthorizationUrlIncludesAuthorizationParameters passed");
    }

    @Test
    public void testGetAuthorizationUrlHasCorrectAccessTypeAndApprovalPrompt() {
        OIDCAuthorizationService authorizationService = new OIDCAuthorizationService();
        String authorizationUrl = authorizationService.getAuthorizationUrl();

        assertTrue(authorizationUrl.contains("access_type=offline"));
        assertTrue(authorizationUrl.contains("approval_prompt=force"));
        System.out.println("Test GetAuthorizationUrlHasCorrectAccessTypeAndApprovalPrompt passed");
    }


    @Test
    public void testGetAuthorizationUrlIncludesAuthorizationServerEndpoint() {
        OIDCAuthorizationService authorizationService = new OIDCAuthorizationService();
        String authorizationUrl = authorizationService.getAuthorizationUrl();

        String authorizationEndpoint = "http://localhost:8080/auth/realms/cyberduckrealm/protocol/openid-connect/auth";
        assertTrue(authorizationUrl.contains(authorizationEndpoint));
        System.out.println("Test GetAuthorizationUrlIncludesAuthorizationServerEndpoint passed");
    }

}
