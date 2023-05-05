package ch.oidc;

public class OAuthTokens {
    public static final OAuthTokens EMPTY = new OAuthTokens(null, null, Long.MAX_VALUE);

    private final String accessToken;
    private final String refreshToken;
    private final Long expiryInMilliseconds;

    public OAuthTokens(final String accessToken, final String refreshToken, final Long expiryInMilliseconds) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiryInMilliseconds = expiryInMilliseconds;
    }

    public boolean validate() {
        return !accessToken.isEmpty();
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Long getExpiryInMilliseconds() {
        return expiryInMilliseconds;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() >= expiryInMilliseconds;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OAuthTokens{");
        sb.append("accessToken='").append(accessToken);
        sb.append(", refreshToken='").append(refreshToken);
        sb.append(", expiryInMilliseconds=").append(expiryInMilliseconds);
        sb.append('}');
        return sb.toString();
    }
}
