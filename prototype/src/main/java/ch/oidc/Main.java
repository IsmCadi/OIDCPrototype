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
        OAuthTokens creds = OIDCAuthorizationService.exchangeAuthorizationCodeForTokens(authorizationCode);
        //System.out.println("Thats the userinfo:");
        //OIDCAuthorizationService.validateAccessToken(accessToken);

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

        // Try to upload files into the cyberduck bucket

        System.out.println("Type some chars to upload a file. No chars -> exit loop");
        while (scanner.hasNext()) {
            String input = scanner.nextLine();
            try {
                OIDCMinioUploader.uploadFile(sessionToken, accessKey, secretKey);
            } catch (InvalidKeyException | MinioException e) {
                System.out.println("Try Refresh Token");
            } catch (IOException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

    }
}