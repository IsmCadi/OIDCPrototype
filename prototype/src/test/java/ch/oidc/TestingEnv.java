package ch.oidc;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TestingEnv {

    @ClassRule
    public static DockerComposeContainer<?> compose = new DockerComposeContainer<>(
            new File("src/test/resources/docker-compose.yml"))
            .withExposedService("keycloak", 8080, Wait.forHttp("/auth"))
            .withExposedService("minio", 9000, Wait.forHttp("/minio/health/life"));

    @Test
    public void testConnectivityToContainers() throws IOException {
        HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
        GenericUrl url = new GenericUrl("http://localhost:9000");

        Map<String, String> data = new HashMap<>();
        data.put("Action", "AssumeRoleWithWebIdentity");
        data.put("WebIdentityToken", "invalidXXXX");
        data.put("Version", "2011-06-15");
//        data.put("DurationSeconds", "86000");

        HttpContent content = new UrlEncodedContent(data);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept("*/*");

        HttpRequest request = requestFactory.buildPostRequest(url, content);
        request.setHeaders(headers);
        HttpResponse response = request.execute();

        System.out.println(response.parseAsString());

        Assert.assertNotNull(response);
    }
}
