import clients.CourierClient;
import helpers.CourierCreationResult;
import helpers.CourierTestHelper;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.apache.http.HttpStatus.*;

public class DeleteCourierTest {

    private CourierClient courierClient;
    private CourierTestHelper courierHelper;
    private int courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courierHelper = new CourierTestHelper(courierClient);

        CourierCreationResult result = courierHelper.createCourier("password123", "Иван");
        courierId = result.getId();
    }

    @After
    public void tearDown() {
        courierHelper.cleanUp();
    }

    // Шаги
    @Step("Проверяем, что курьер удален: статус 200, ok: true")
    public void verifyDeleteSuccess(Response response) {
        response.then()
                .statusCode(200)
                .and().body("ok", is(true));
    }

    @Step("Проверяем статус и сообщение неуспешного удаления")
    public void verifyDeleteError(Response response, int expectedStatus, String expectedMessage) {
        response.then()
                .statusCode(expectedStatus)
                .and().body("message", equalTo(expectedMessage));
    }

    @Test
    @DisplayName("DELETE /api/v1/courier/:id Успешный запрос на удаление курьера")
    @Description("Создаём курьера, удаляем его, проверяем статус 200 и ok:true")
    public void deleteCourierSuccess() {

        Response deleteResponse = courierClient.deleteCourier(courierId);
        verifyDeleteSuccess(deleteResponse);

        // Сбрасываем ID в хелпере, чтобы tearDown не пытался удалить курьера повторно
        courierHelper.resetCreatedCourierId();
    }

    @Test
    @DisplayName("DELETE /api/v1/courier/:id Нельзя удалить курьера с несуществующим ID")
    @Description("Ожидаем ошибку 404 и сообщение - Курьера с таким id нет")
    public void deleteCourierWithNonExistentId() {
        int nonExistentId = 999999; // заведомо несуществующий ID
        Response response = courierClient.deleteCourier(nonExistentId);
        verifyDeleteError(response, SC_NOT_FOUND, "Курьера с таким id нет");
    }

    @Test
    @DisplayName("DELETE /api/v1/courier/:id Нельзя удалить курьера без ID в запросе")
    @Description("Ожидаем ошибку 400 и сообщение - Недостаточно данных для удаления курьера")
    public void deleteCourierWithoutId() {
        Response response = courierClient.deleteCourierWithoutId();
        verifyDeleteError(response, SC_BAD_REQUEST, "Недостаточно данных для удаления курьера");
    }
}
