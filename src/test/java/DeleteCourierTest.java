import clients.CourierClient;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.Courier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class DeleteCourierTest {

    private CourierClient courierClient;
    private int createdCourierId = 0;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    // Если после теста остался созданный курьер, удаляем его
    @After
    public void tearDown() {
        if (createdCourierId > 0) {
            courierClient.deleteCourier(createdCourierId);
        }
    }

    // Шаги
    @Step("Логинимся, получаем ID")
    public int getCourierId(Courier courier) {
        Response loginResponse = courierClient.loginCourier(courier);
        loginResponse.then().statusCode(200);
        return loginResponse.path("id");
    }

    @Step("Проверяем, что курьер удален: статус 200, ok: true")
    public void verifyDeleteSuccess(Response response) {
        response.then()
                .statusCode(200)
                .and().body("ok", is(true));
    }

    @Step("Проверяем ошибку при удалении: статус expectedStatus, сообщение expectedMessage")
    public void verifyDeleteError(Response response, int expectedStatus, String expectedMessage) {
        response.then()
                .statusCode(expectedStatus)
                .and().body("message", equalTo(expectedMessage));
    }

    @Test
    @DisplayName("DELETE /api/v1/courier/:id Успешный запрос на удаление курьера")
    @Description("Создаём курьера, удаляем его, проверяем статус 200 и ok:true")
    public void deleteCourierSuccess() {

        String login = "deleteTest_" + System.currentTimeMillis();
        Courier courier = new Courier(login, "password123", "Анна");
        Response createResponse = courierClient.createCourier(courier);
        createResponse.then().statusCode(201);

        int courierId = getCourierId(courier);
        createdCourierId = courierId;

        Response deleteResponse = courierClient.deleteCourier(courierId);
        verifyDeleteSuccess(deleteResponse);

        createdCourierId = 0;
    }

    @Test
    @DisplayName("DELETE /api/v1/courier/:id Нельзя удалить курьера с несуществующим ID")
    @Description("Ожидаем ошибку 404 и сообщение - Курьера с таким id нет")
    public void deleteCourierWithNonExistentId() {
        int nonExistentId = 999999; // заведомо несуществующий ID
        Response response = courierClient.deleteCourier(nonExistentId);
        verifyDeleteError(response, 404, "Курьера с таким id нет");
    }

    @Test
    @DisplayName("DELETE /api/v1/courier/:id Нельзя удалить курьера без ID в запросе")
    @Description("Ожидаем ошибку 400 и сообщение - Недостаточно данных для удаления курьера")
    public void deleteCourierWithoutId() {
        Response response = courierClient.deleteCourierWithoutId();
        verifyDeleteError(response, 400, "Недостаточно данных для удаления курьера");
    }
}
