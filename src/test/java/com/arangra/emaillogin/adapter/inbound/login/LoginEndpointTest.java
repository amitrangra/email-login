package com.arangra.emaillogin.adapter.inbound.login;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class LoginEndpointTest {
    private WebTestClient webTestClient;

    @LocalServerPort
    private Integer port;

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withDisabledAuthentication())
            .withPerMethodLifecycle(false);

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:"+port)
                .build();
    }

    @Test
    void loginPageReturns2xx() {
        webTestClient
                .get()
                .uri("/login")
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    @Test
    void emailloginPostReturns2xx() throws MessagingException, IOException {
        String gotoPage = "http://localhost:"+port+"/welcome";
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("goto", gotoPage);
        formData.add("email", "dummy@example.com");
        webTestClient
                .post()
                .uri("/login")
                .contentType(APPLICATION_FORM_URLENCODED)
                .body(fromFormData(formData))
                .exchange()
                .expectStatus().is2xxSuccessful();

        await().atMost(5, SECONDS).untilAsserted(() -> {
            MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
            assertThat(receivedMessages.length).isEqualTo(1);
            MimeMessage receivedMessage = receivedMessages[0];
            assertThat(receivedMessage.getFrom()[0].toString()).isEqualTo("email-client-test@example.com");
            assertThat(receivedMessage.getSubject()).contains("Email Login Token @");
            assertThat(receivedMessage.getAllRecipients()[0].toString()).contains("dummy@example.com");
            assertThat(receivedMessage.getContent().toString()).contains("Token:");
        });
    }
}