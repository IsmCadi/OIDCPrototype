package ch.oidc;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.net.URL;

public class OIDCMinioUploader {
    private static final String MINIO_SERVER_URL = "http://localhost:9000";
    private static final String accessToken = "take-your-accesscode-when-issued";
    private static String bucketName = "cyberduckbucket";
    static String filePath = "prototype/src/main/java/ch/oidc/test/test.txt";
    private static final URL url;
    static HttpURLConnection connection;

    static {
        try {
            url = new URL("http://localhost:9000/" + bucketName + "/");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    static {
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void uploadFile() throws IOException, InvalidKeyException, MinioException, NoSuchAlgorithmException {
        File file = new File(filePath);
        InputStream inputStream = new FileInputStream(file);

        // Create a new MinioClient object
        MinioClient minioClient = MinioClient.builder()
                .endpoint(MINIO_SERVER_URL)
                .build();

        // Upload input stream with headers and user metadata.
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + accessToken);
        Map<String, String> userMetadata = new HashMap<>();
        userMetadata.put("My-Project", "Project One");
        minioClient.putObject(
                PutObjectArgs.builder().bucket(bucketName).object("testIt").stream(
                                inputStream, 1, -1)
                        .headers(headers)
                        .userMetadata(userMetadata)
                        .build());
        System.out.println("File uploaded successfully!");
    }
}