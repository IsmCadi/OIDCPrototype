package ch.oidc;

import ch.oidc.AuthorizationServiceTest.AuthorizationUrlTest;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class TestingEnvTest {

    private static final Logger logger = LoggerFactory.getLogger(TestingEnvTest.class);
//    private static final Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(logger);

    @ClassRule
    public static DockerComposeContainer<?> compose = new DockerComposeContainer<>(
            new File("src/test/resources/docker-compose.yml"))
            .withPull(false)
            .withLocalCompose(true)
            .withOptions("--compatibility")
            //.withLogConsumer("keycloak_1", new Slf4jLogConsumer(logger))
            //.withLogConsumer("minio_1", new Slf4jLogConsumer(logger))
            .withExposedService("keycloak_1", 8080, Wait.forListeningPort())
            .withExposedService("minio_1", 9000, Wait.forListeningPort()); //forHttp("/minio/health/life")


    @Test
    public void testAuthorizationUrl() {
        compose.start();
        // created an instance of AuthorizationUrlTest
        AuthorizationUrlTest authUrlTest = new AuthorizationUrlTest();
        // call the prepared tests
        authUrlTest.testGetAuthorizationUrlNotNullOrEmpty();
        authUrlTest.testGetAuthorizationUrlHasCorrectFormat();
        authUrlTest.testGetAuthorizationUrlIncludesAuthorizationParameters();
        authUrlTest.testGetAuthorizationUrlIncludesAuthorizationParameters();
        authUrlTest.testGetAuthorizationUrlHasCorrectAccessTypeAndApprovalPrompt();
        authUrlTest.testGetAuthorizationUrlIncludesAuthorizationServerEndpoint();
        compose.stop();
    }

//    @Test
//    public void testConnectivityToContainers() throws IOException {
//        compose.start();
//        HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
//
//        String minioUrl = compose.getServiceHost("minio_1", 9000)
//                + ":" +
//                compose.getServicePort("minio_1", 9000);
//        System.out.println(minioUrl);
//        GenericUrl url = new GenericUrl("http://" + minioUrl);
//
//        Map<String, String> data = new HashMap<>();
//        data.put("Action", "AssumeRoleWithWebIdentity");
//        data.put("WebIdentityToken", "invalidXXXX");
//        data.put("Version", "2011-06-15");
////        data.put("DurationSeconds", "86000");
//
//        HttpContent content = new UrlEncodedContent(data);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setAccept("*/*");
//
//        HttpRequest request = requestFactory.buildPostRequest(url, content);
//        request.setHeaders(headers);
//        HttpResponse response = request.execute();
//
//        System.out.println(response.parseAsString());
//
//        Assert.assertEquals(response.getStatusCode(), 400);
//        compose.stop();
//    }

}
