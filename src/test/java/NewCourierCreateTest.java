import clients.CourierClient;
import helpers.CourierCreationResult;
import helpers.CourierTestHelper;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.Courier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class NewCourierCreateTest {

    private CourierClient courierClient;
    private CourierTestHelper courierHelper;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courierHelper = new CourierTestHelper(courierClient);
    }

    @After
    public void tearDown() {
        courierHelper.cleanUp();
    }

    // Шаги
    @Step("Запрос на создание курьера") // Нужен для негативных проверок без логина или пароля
    public Response sendCreateCourierRequest(Courier courier) {
        return courierClient.createCourier(courier);
    }

    @Step("Проверяем статус ошибки")
    public void verifyErrorStatus(Response response, int expectedStatus) {
        response.then().statusCode(expectedStatus);
    }

    @Step("Проверяем сообщение ошибки")
    public void verifyErrorMessage(Response response, String expectedMessage) {
        response.then().body("message", equalTo(expectedMessage));
    }

    // Тесты
    @Test
    @DisplayName("POST /api/v1/courier Можно создать курьера с валидными параметрами")
    @Description("Курьер с уникальным логином создается, ожидаем код 201 и ok:true")
    public void createCourierSuccess() {
        CourierCreationResult result = courierHelper.createCourier("password123", "Иван");
        result.getResponse().then().body("ok", is(true));
    }

    @Test
    @DisplayName("POST /api/v1/courier Нельзя создать курьера с повторяющимся логином")
    @Description("Ожидаем ошибку 409 при попытке создать курьера с уже существующим логином")
    public void cannotCreateDuplicateCourier() {
        CourierCreationResult firstResult = courierHelper.createCourier("password123", "Петр");
        Courier firstCourier = firstResult.getCourier();

        Courier duplicateCourier = new Courier(firstCourier.getLogin(), "differentPass", "Сергей");
        Response duplicateResponse = courierClient.createCourier(duplicateCourier);
        verifyErrorStatus(duplicateResponse, 409);
        verifyErrorMessage(duplicateResponse, "Этот логин уже используется");
    }

    @Test
    @DisplayName("POST /api/v1/courier Нельзя создать курьера без логина")
    @Description("Ожидаем ошибку 400 при отсутствии поля login")
    public void cannotCreateCourierWithoutLogin() {
        Courier courier = new Courier(null, "password123", "Иван");
        Response response = sendCreateCourierRequest(courier);
        verifyErrorStatus(response, 400);
        verifyErrorMessage(response, "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("POST /api/v1/courier Нельзя создать курьера без пароля")
    @Description("Ожидаем ошибку 400 при отсутствии поля password")
    public void cannotCreateCourierWithoutPassword() {
        Courier courier = new Courier("login_" + System.currentTimeMillis(), null, "Иван");
        Response response = sendCreateCourierRequest(courier);
        verifyErrorStatus(response, 400);
        verifyErrorMessage(response, "Недостаточно данных для создания учетной записи");
    }
}