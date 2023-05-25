package ch.oidc;

import ch.oidc.AuthorizationServiceTest.AuthorizationUrlTest;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;


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


}
