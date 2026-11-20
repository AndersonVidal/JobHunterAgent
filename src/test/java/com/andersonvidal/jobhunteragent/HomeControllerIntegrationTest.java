package com.andersonvidal.jobhunteragent;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.client.RestTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
public class HomeControllerIntegrationTest {

    @Autowired
    private RestTestClient restTestClient;

    @Test
    void indexShouldReturnDefaultMessage() throws Exception {
        RestTestClient.ResponseSpec response = restTestClient.get()
                .uri("/")
                .exchange();

        response.expectStatus().isOk();
        response.expectBody(String.class).isEqualTo("Job Hunter Agent");
    }
}
