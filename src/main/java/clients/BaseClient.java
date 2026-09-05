package clients;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public abstract class BaseClient {

    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";

    static {
        RestAssured.baseURI = BASE_URI;
    }

    protected RequestSpecification getBaseSpec() {
        return RestAssured.given()
                .filter(new AllureRestAssured())
                .contentType("application/json");
    }
}