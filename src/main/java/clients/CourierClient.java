package clients;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.Courier;
import models.LoginCourier;

import static io.restassured.RestAssured.given;

public class CourierClient {

    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";
    private static final String COURIER_PATH = "/api/v1/courier";

    public Response createCourier(Courier courier) {
        return given()
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .body(courier)
                .when()
                .post(BASE_URI + COURIER_PATH);
    }

    public Response loginCourier(Courier courier) {
        LoginCourier loginCourier = new LoginCourier(courier.getLogin(), courier.getPassword());
        return given()
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .body(loginCourier)
                .when()
                .post(BASE_URI + COURIER_PATH + "/login");
    }

    public Response loginCourierWithoutBody() {
        return given()
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .when()
                .post(BASE_URI + COURIER_PATH + "/login");
    }

    public Response loginCourierWithOnlyLogin(String login) {
        String body = String.format("{\"login\": \"%s\"}", login);
        return given()
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(BASE_URI + COURIER_PATH + "/login");
    }

    public Response loginCourierWithOnlyPassword(String password) {
        String body = String.format("{\"password\": \"%s\"}", password);
        return given()
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(BASE_URI + COURIER_PATH + "/login");
    }

    public Response deleteCourier(int courierId) {
        return given()
                .filter(new AllureRestAssured())
                .when()
                .delete(BASE_URI + COURIER_PATH + "/" + courierId);
    }

    public Response deleteCourierWithoutId() {
        return given()
                .filter(new AllureRestAssured())
                .when()
                .delete(BASE_URI + COURIER_PATH);
    }

}
