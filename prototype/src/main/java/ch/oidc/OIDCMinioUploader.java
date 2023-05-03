package ch.oidc;

import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.MinioException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class OIDCMinioUploader {
    private static final String minioServerUrl = "localhost";

    public static void uploadFile(String sessionToken, String accessKey, String secretKey) throws IOException, InvalidKeyException, MinioException, NoSuchAlgorithmException {
        // Create a new MinioClient object
        MinioClient minioClient = MinioClient.builder()
                .endpoint(minioServerUrl, 9000, false)
                .credentials(accessKey, secretKey)
                .build();

        // Set the access token as a header for authentication
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + sessionToken);
        //minioClient.setRequestHeaders(headers);

        System.out.println(minioClient.listBuckets());

        // Upload the file to the MinIO bucket
        minioClient.uploadObject(
                UploadObjectArgs.builder()
                        .bucket("cyberduckbucket")
                        .object("test")
                        .filename("prototype/src/main/java/ch/oidc/test/test.txt")
                        .build());
        System.out.println("File uploaded successfully!");
    }
}
