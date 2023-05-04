package ch.oidc;

import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.credentials.StaticProvider;
import io.minio.errors.MinioException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class OIDCMinioUploader {
    private static final String minioServerUrl = "http://localhost:9000";

    public static void uploadFile(String sessionToken, String accessKey, String secretKey) throws IOException, InvalidKeyException, MinioException, NoSuchAlgorithmException {
        //AWSStaticCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(accessKey, secretKey, sessionToken);
        StaticProvider staticProvider = new StaticProvider(accessKey, secretKey, sessionToken);

        MinioClient minioClient = new MinioClient.Builder()
                .endpoint(minioServerUrl)
                .credentialsProvider(staticProvider)
                .build();

        // Set the access token as a header for authentication
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + sessionToken);

        // Upload the file to the MinIO bucket
        minioClient.uploadObject(
                UploadObjectArgs.builder()
                        .bucket("cyberduckbucket")
                        .object("test")
                        .filename("prototype/src/main/java/ch/oidc/test/test.txt")
                        .headers(headers)
                        .build());
        System.out.println("File uploaded successfully!");
    }
}
