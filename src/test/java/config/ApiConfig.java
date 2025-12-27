package org.example.config;

import io.restassured.RestAssured;

public class ApiConfig {

    public static void init() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 3000;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}
