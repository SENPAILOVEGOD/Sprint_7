package helpers;

import clients.CourierClient;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.Courier;

public class CourierTestHelper {

    private final CourierClient courierClient;
    private int createdCourierId = 0;

    public CourierTestHelper(CourierClient courierClient) {
        this.courierClient = courierClient;
    }

    @Step("Создаем курьера с password и firstName")
    public CourierCreationResult createCourier(String password, String firstName) {
        String login = "login_" + System.currentTimeMillis();
        Courier courier = new Courier(login, password, firstName);
        Response createResponse = courierClient.createCourier(courier);
        createResponse.then().statusCode(201);
        int id = getCourierId(courier);
        this.createdCourierId = id;
        return new CourierCreationResult(courier, createResponse, id);
    }

    @Step("Получаем ID курьера по логину и паролю")
    public int getCourierId(Courier courier) {
        Response loginResponse = courierClient.loginCourier(courier);
        loginResponse.then().statusCode(200);
        return loginResponse.path("id");
    }

    @Step("Удаляем курьера")
    public void cleanUp() {
        if (createdCourierId > 0) {
            courierClient.deleteCourier(createdCourierId);
            createdCourierId = 0;
        }
    }

    public void resetCreatedCourierId() {
        this.createdCourierId = 0;
    }
}
