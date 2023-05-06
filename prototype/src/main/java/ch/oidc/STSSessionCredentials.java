package ch.oidc;

public class STSSessionCredentials {

    private final String accessKey;
    private final String secretKey;
    private final String sessionToken;

    public STSSessionCredentials(String accessKey, String secretKey, String sessionToken) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.sessionToken = sessionToken;
    }

    public String getAWSAccessKeyId() { return accessKey; }

    public String getAWSSecretKey() {
        return secretKey;
    }

    public String getSessionToken() {
        return sessionToken;
    }
}
