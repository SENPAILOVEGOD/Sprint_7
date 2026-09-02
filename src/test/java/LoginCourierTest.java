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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginCourierTest {

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
    @Step("Проверяем успешный логин: статус 200, id не null")
    public void verifyLoginSuccess(Response response) {
        response.then()
                .statusCode(200)
                .and().body("id", notNullValue());
    }

    @Step("Проверяем статус и сообщение неуспешного логина")
    public void verifyLoginError(Response response, int expectedStatus, String expectedMessage) {
        response.then()
                .statusCode(expectedStatus)
                .and().body("message", equalTo(expectedMessage));
    }

    @Test
    @DisplayName("POST /api/v1/courier/login Успешный логин курьера")
    @Description("Курьер создаётся, затем логинится, ожидаем статус 200 и id не null")
    public void loginCourierSuccess() {
        CourierCreationResult result = courierHelper.createCourier("password123", "Иван");
        Courier courier = result.getCourier();

        Response loginResponse = courierClient.loginCourier(courier);
        verifyLoginSuccess(loginResponse);
    }

    @Test
    @DisplayName("POST /api/v1/courier/login Ошибка в ответе на запрос без логина")
    @Description("Ожидаем ошибку 400 с сообщением - Недостаточно данных для входа")
    public void loginWithoutLogin() {
        Response response = courierClient.loginCourierWithOnlyPassword("somePassword");
        verifyLoginError(response, 400, "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("POST /api/v1/courier/login Ошибка в ответе на запрос без пароля")
    @Description("Ожидаем ошибку 400 с сообщением - Недостаточно данных для входа")
    public void loginWithoutPassword() {
        Response response = courierClient.loginCourierWithOnlyLogin("someLogin");
        verifyLoginError(response, 400, "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("POST /api/v1/courier/login Ошибка в ответе на запрос без тела")
    @Description("Ожидаем ошибку 400 с сообщением - Недостаточно данных для входа")
    public void loginWithoutBody() {
        Response response = courierClient.loginCourierWithoutBody();
        verifyLoginError(response, 400, "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("POST /api/v1/courier/login Ошибка в ответе на запрос с неверным паролем")
    @Description("Ожидаем ошибку 404 с сообщением - Учетная запись не найдена")
    public void loginWithWrongPassword() {
        CourierCreationResult result = courierHelper.createCourier("password123", "Иван");
        Courier courier = result.getCourier();

        courier.setPassword("wrongPassword");
        Response loginResponse = courierClient.loginCourier(courier);
        verifyLoginError(loginResponse, 404, "Учетная запись не найдена");
    }

    @Test
    @DisplayName("POST /api/v1/courier/login Ошибка в ответе на запрос с несуществующим логином")
    @Description("Ожидаем ошибку 404 с сообщением - Учетная запись не найдена")
    public void loginWithNonExistentLogin() {
        Courier courier = new Courier("nonexistent_login_" + System.currentTimeMillis(), "anyPassword", null);
        Response loginResponse = courierClient.loginCourier(courier);
        verifyLoginError(loginResponse, 404, "Учетная запись не найдена");
    }

}