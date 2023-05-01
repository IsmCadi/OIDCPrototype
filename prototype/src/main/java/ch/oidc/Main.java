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
        String accessToken = oidcConfig.exchangeAuthorizationCodeForAccessToken(authorizationCode);

        System.out.println(accessToken);
        System.out.println("Thats the userinfo:");

        oidcConfig.validateAccessToken(accessToken);

        try {
            OIDCAuthorizationService.uploadFile(accessToken);
        } catch (IOException | MinioException | InvalidKeyException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // Try to upload a file into the cyberduck bucket
/*        try {
            OIDCMinioUploader.uploadFile();
        } catch (IOException | MinioException | InvalidKeyException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }*/
    }
}