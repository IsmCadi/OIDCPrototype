package ch.oidc;

import com.google.api.client.auth.oauth2.TokenResponse;
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
        TokenResponse tokenResponse = OIDCAuthorizationService.exchangeAuthorizationCodeForTokens(authorizationCode);
        String accessToken = OIDCAuthorizationService.getAccessToken(tokenResponse);
        //System.out.println("Thats the userinfo:");
        //OIDCAuthorizationService.validateAccessToken(accessToken);

        String idToken = OIDCAuthorizationService.getIdToken(tokenResponse);
        String stsResponse = OIDCAuthorizationService.minioSts(idToken);
        OIDCAuthorizationService.extractKeysAndToken(stsResponse);

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

        // Try to upload a file into the cyberduck bucket
        try {
            OIDCMinioUploader.uploadFile(sessionToken, accessKey, secretKey);
        } catch (IOException | MinioException | InvalidKeyException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}