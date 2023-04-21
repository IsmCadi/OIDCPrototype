package ch.oidc;

import java.util.Scanner;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        // Create an instance of the Authentication class
        Authentication auth = new Authentication();

        // Get the authorization URL
        String authorizationUrl = auth.getAuthorizationUrl();

        // Print the authorization URL and ask the user to open it in a browser and authorize the application
        System.out.println("Open the following URL in a browser to authorize the application:");
        System.out.println(authorizationUrl);

        // Wait for the user to authorize the application and enter the authorization code
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the authorization code: ");
        String authorizationCode = scanner.nextLine();

        // Exchange the authorization code for an access token
        String accessToken = auth.exchangeAuthorizationCodeForAccessToken(authorizationCode);

        // Validate the access token
        auth.validateAccessToken(accessToken);

        System.out.println(accessToken);


    }
}