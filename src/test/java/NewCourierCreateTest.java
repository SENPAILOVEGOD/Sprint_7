import clients.CourierClient;
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

    // Здесь храним ID курьера, полученный методом getCourierId (нужно для удаления после тестов)
    private int createdCourierId = 0;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @After
    public void tearDown() {
        if (createdCourierId > 0) {
            courierClient.deleteCourier(createdCourierId);
        }
    }

    // Шаги
    @Step("Запрос на создание курьера")
    public Response sendCreateCourierRequest(Courier courier) {
        return courierClient.createCourier(courier);
    }

    @Step("Проверяем статус ответа: ожидаем 201 Created")
    public void verifyCreateCourierStatus(Response response) {
        response.then().statusCode(201);
    }

    @Step("Проверяем, что ответ содержит ok: true")
    public void verifyCreateCourierBodyOk(Response response) {
        response.then().body("ok", is(true));
    }

    @Step("Проверяем статус ошибки: ожидаем expectedStatus")
    public void verifyErrorStatus(Response response, int expectedStatus) {
        response.then().statusCode(expectedStatus);
    }

    @Step("Проверяем сообщение ошибки: ожидаем expectedMessage")
    public void verifyErrorMessage(Response response, String expectedMessage) {
        response.then().body("message", equalTo(expectedMessage));
    }

    @Step("Получаем ID курьера по логину и паролю (нужно для удаления курьера после тестов)")
    public int getCourierId(Courier courier) {
        Response loginResponse = courierClient.loginCourier(courier);
        loginResponse.then().statusCode(200);
        return loginResponse.path("id");
    }

    // Тесты
    @Test
    @DisplayName("Можно создать курьера")
    @Description("Курьер с уникальным логином создается, ожидаем код 201 и ok:true")
    public void createCourierSuccess() {
        String uniqueLogin = "login_" + System.currentTimeMillis();
        Courier courier = new Courier(uniqueLogin, "password123", "Иван");

        Response response = sendCreateCourierRequest(courier);
        verifyCreateCourierStatus(response);
        verifyCreateCourierBodyOk(response);

        int courierId = getCourierId(courier);
        createdCourierId = courierId;
    }

    @Test
    @DisplayName("Нельзя создать курьера с повторяющимся логином")
    @Description("Ожидаем ошибку 409 при попытке создать курьера с уже существующим логином")
    public void cannotCreateDuplicateCourier() {
        String login = "duplicateLogin_" + System.currentTimeMillis();
        Courier courier = new Courier(login, "password123", "Петр");

        Response firstResponse = sendCreateCourierRequest(courier);
        verifyCreateCourierStatus(firstResponse);
        verifyCreateCourierBodyOk(firstResponse);
        createdCourierId = getCourierId(courier);

        Courier duplicateCourier = new Courier(login, "differentPass", "Сергей");
        Response duplicateResponse = sendCreateCourierRequest(duplicateCourier);
        verifyErrorStatus(duplicateResponse, 409);
        verifyErrorMessage(duplicateResponse, "Этот логин уже используется");
    }

    @Test
    @DisplayName("Нельзя создать курьера без логина")
    @Description("Ожидаем ошибку 400 при отсутствии поля login")
    public void cannotCreateCourierWithoutLogin() {
        Courier courier = new Courier(null, "password123", "Иван");
        Response response = sendCreateCourierRequest(courier);
        verifyErrorStatus(response, 400);
        verifyErrorMessage(response, "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Нельзя создать курьера без пароля")
    @Description("Ожидаем ошибку 400 при отсутствии поля password")
    public void cannotCreateCourierWithoutPassword() {
        Courier courier = new Courier("login_" + System.currentTimeMillis(), null, "Иван");
        Response response = sendCreateCourierRequest(courier);
        verifyErrorStatus(response, 400);
        verifyErrorMessage(response, "Недостаточно данных для создания учетной записи");
    }
}
