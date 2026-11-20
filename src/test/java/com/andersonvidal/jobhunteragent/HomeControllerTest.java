package com.andersonvidal.jobhunteragent;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class HomeControllerTest {

    private HomeController controller;

    @Test
    public void shouldReturnIndex() {
        HomeController controller = new HomeController();
        String response = controller.index();
        assertThat(response).isEqualTo("Job Hunter Agent");
    }
}