package ch.oidc;

import io.minio.MinioClient;
import io.minio.errors.MinioException;
import io.minio.UploadObjectArgs;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class OIDCMinioUploader {
    private static final String MINIO_SERVER_URL = "http://localhost:9000";
    private static final String ACCESS_TOKEN = "";

    public static void uploadFile() throws IOException, InvalidKeyException, MinioException, NoSuchAlgorithmException {
        // Create a new MinioClient object
        MinioClient minioClient = MinioClient.builder()
                .endpoint(MINIO_SERVER_URL)
                .build();

        // Set the access token as a header for authentication
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + ACCESS_TOKEN);
        //minioClient.setRequestHeaders(headers);
        var req = "";


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
