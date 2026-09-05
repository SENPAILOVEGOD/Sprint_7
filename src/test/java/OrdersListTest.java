import clients.CourierClient;
import clients.OrderClient;
import helpers.CourierCreationResult;
import helpers.CourierTestHelper;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrdersListTest {

    private OrderClient orderClient;
    private CourierClient courierClient;
    private CourierTestHelper courierHelper;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        courierClient = new CourierClient();
        courierHelper = new CourierTestHelper(courierClient);
    }

    @After
    public void tearDown() {
        courierHelper.cleanUp();
    }

    @Step("Отправляем GET-запрос /api/v1/orders")
    public Response sendGetOrdersRequest(Map<String, Object> params) {
        if (params == null) {
            return orderClient.getOrders();
        } else {
            return orderClient.getOrders(params);
        }
    }

    @Step("Проверяем, что ответ содержит список заказов")
    public void verifyOrdersList(Response response) {
        response.then()
                .statusCode(200)
                .and().body("orders", notNullValue());
    }

    @Step("Проверяем ошибку 404 с сообщением о несуществующем курьере")
    public void verifyNotFoundError(Response response, int courierId) {
        response.then()
                .statusCode(404)
                .and().body("message", equalTo("Курьер с идентификатором " + courierId + " не найден"));
    }

    @Test
    @DisplayName("GET /api/v1/orders Получение списка заказов без параметров")
    @Description("Проверяем, что ответ содержит поле orders")
    public void getOrdersWithoutParams() {
        Response response = sendGetOrdersRequest(new HashMap<>());
        verifyOrdersList(response);
    }

    @Test
    @DisplayName("GET /api/v1/orders Получение списка заказов с существующим courierId")
    @Description("Создаем курьера, запрашиваем его заказы, проверяем, что ответ содержит orders")
    public void getOrdersWithExistingCourierId() {
        CourierCreationResult result = courierHelper.createCourier("password123", "Иван");
        int courierId = result.getId();

        Map<String, Object> params = new HashMap<>();
        params.put("courierId", courierId);
        Response response = sendGetOrdersRequest(params);
        verifyOrdersList(response);
    }

    @Test
    @DisplayName("GET /api/v1/orders Ошибка при запросе с несуществующим courierId")
    @Description("Передаем несуществующий ID, ожидаем 404 и сообщение об ошибке")
    public void getOrdersWithNonExistentCourierId() {
        int nonExistentId = 999999;
        Map<String, Object> params = new HashMap<>();
        params.put("courierId", nonExistentId);
        Response response = sendGetOrdersRequest(params);
        verifyNotFoundError(response, nonExistentId);
    }
}
