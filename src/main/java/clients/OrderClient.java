package clients;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.Order;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class OrderClient {

    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";
    private static final String ORDER_PATH = "/api/v1/orders";

    public Response createOrder(Order order) {
        return given()
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post(BASE_URI + ORDER_PATH);
    }

    public Response getOrders(Map<String, Object> queryParams) {
        return given()
                .filter(new AllureRestAssured())
                .queryParams(queryParams)
                .when()
                .get(BASE_URI + ORDER_PATH);
    }

    public Response getOrders() {
        return getOrders(null);
    }
}