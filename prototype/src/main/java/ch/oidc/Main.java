package ch.oidc;

import io.minio.errors.MinioException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {


        OIDCAuthorizationService oidcConfig = new OIDCAuthorizationService();
        String authorizationUrl = oidcConfig.getAuthorizationUrl();

        System.out.println("Open the following URL in your browser:");
        System.out.println(authorizationUrl);

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the authorization code: ");
        String authorizationCode = scanner.nextLine();
        String accessToken = OIDCAuthorizationService.exchangeAuthorizationCodeForAccessToken(authorizationCode);

        System.out.println(accessToken);
        System.out.println("Thats the userinfo:");

        OIDCAuthorizationService.validateAccessToken(accessToken);

        System.out.println("Enter the id token: ");
        String idToken = scanner.nextLine();
        OIDCAuthorizationService.minioSts(idToken);


        System.out.println("Enter the session token from STS: ");
        String sessionToken = scanner.nextLine();

        System.out.println("Enter the access key from STS: ");
        String accessKey = scanner.nextLine();

        System.out.println("Enter the secret key from STS: ");
        String secretKey = scanner.nextLine();

        // Try to upload a file into the cyberduck bucket
        try {
            OIDCMinioUploader.uploadFile(accessToken, accessKey, secretKey);
        } catch (IOException | MinioException | InvalidKeyException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}