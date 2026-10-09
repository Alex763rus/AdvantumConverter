package com.example.advantumconverter.rest;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SetupControllerTest {

    @Test
    void setup_returnsView() {
        assertThat(new SetupController().setup()).isEqualTo("setup");
    }
}
