package com.assetco.search.service;

import io.restassured.*;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.web.server.*;

import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ServiceBootsIT {
    @LocalServerPort
    private int port;

    @Test
    public void healthCheck() {
        RestAssured.get("http://localhost:" + port +"/health/status").then().statusCode(is(200)).and().body(is("{\"up\":true}"));
    }
}
