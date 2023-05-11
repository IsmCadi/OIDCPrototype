package ch.oidc.AuthorizationServiceTest;

import ch.oidc.OAuthTokens;
import ch.oidc.OIDCAuthorizationService;
import org.junit.Test;

import java.io.IOException;

import static ch.oidc.OIDCAuthorizationService.exchangeAuthorizationCodeForTokens;
import static org.junit.Assert.*;

public class ExchangeForTokenTest {

    @Test
    public void testExchangeWrongAuthorizationCodeForTokensReturnsNonNullTokens() throws IOException {
        OIDCAuthorizationService authorizationService = new OIDCAuthorizationService();
        String authorizationCode = "someAuthorizationCode";

        OAuthTokens tokens = exchangeAuthorizationCodeForTokens(authorizationCode);

        assertNotNull(tokens);
        assertNotNull(tokens.getAccessToken());
        assertNotNull(tokens.getRefreshToken());
    }
}
