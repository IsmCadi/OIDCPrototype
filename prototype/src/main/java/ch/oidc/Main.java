package ch.oidc;

import io.minio.errors.MinioException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {

        OIDCAuthorizationService oidcConfig = new OIDCAuthorizationService();
        String authorizationUrl = oidcConfig.getAuthorizationUrl();

        System.out.println("Open the following URL in your browser:");
        System.out.println(authorizationUrl);

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the authorization code: ");
        String authorizationCode = scanner.nextLine();
        OAuthTokens tokens = OIDCAuthorizationService.exchangeAuthorizationCodeForTokens(authorizationCode);

        STSSessionCredentials credentials = getSTStempCredentials(tokens);

        // Try to upload files into the cyberduck bucket
        System.out.println("\nType some chars to upload a file. No chars -> exit loop");
        while (scanner.hasNext()) {
            String input = scanner.nextLine();
            try {
                OIDCMinioUploader.uploadFile(credentials.getAWSAccessKeyId(), credentials.getAWSSecretKey(),
                        credentials.getSessionToken());
            } catch (InvalidKeyException | MinioException e) {
                System.out.println("Try to fetch new credetials by the Refresh Token");
                tokens = OIDCAuthorizationService.refresh(tokens.getRefreshToken());
                credentials = getSTStempCredentials(tokens);
            } catch (IOException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
    }

    private static STSSessionCredentials getSTStempCredentials(OAuthTokens creds) throws ParserConfigurationException, IOException, SAXException {
        String stsResponse = OIDCAuthorizationService.minioSts(creds.getAccessToken());

        Map<String, String> keysAndToken = OIDCAuthorizationService.extractKeysAndToken(stsResponse);
        String accessKey = keysAndToken.get("AccessKey");
        String secretKey = keysAndToken.get("SecretKey");
        String sessionToken = keysAndToken.get("SessionToken");

        System.out.println("----------Access and secret key start----------");
        System.out.println("AccessKey: " + accessKey);
        System.out.println("SecretKey: " + secretKey);
        System.out.println("-----------Access and secret key end-----------");
        System.out.println("---------------Sessiontoken start--------------");
        System.out.println(sessionToken);
        System.out.println("----------------Sessiontoken end---------------");

        return new STSSessionCredentials(accessKey, secretKey, sessionToken);
    }
}