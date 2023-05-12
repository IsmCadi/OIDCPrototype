package ch.oidc;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

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
    public void testGetAuthorizationUrlNotNullOrEmpty() {
        OIDCAuthorizationService authorizationService = new OIDCAuthorizationService();
        String authorizationUrl = authorizationService.getAuthorizationUrl();

        assertNotNull(authorizationUrl);
        assertNotEquals("", authorizationUrl);
    }



}
