import clients.OrderClient;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest {

    private final OrderClient orderClient = new OrderClient();
    private final List<String> colors;

    public CreateOrderTest(List<String> colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters(name = "Цвет: {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {null},
                {Arrays.asList("BLACK")},
                {Arrays.asList("GREY")},
                {Arrays.asList("BLACK", "GREY")}
        });
    }

    @Step("Отправляем запрос на создание заказа")
    public Response sendCreateOrderRequest(Order order) {
        return orderClient.createOrder(order);
    }

    @Step("Проверяем статус ответа: ожидается 201 Created")
    public void verifyOrderStatus(Response response) {
        response.then().statusCode(201);
    }

    @Step("Проверяем наличие поля track в ответе")
    public void verifyOrderTrackPresent(Response response) {
        response.then().body("track", notNullValue());
    }

    @Test
    @DisplayName("POST /api/v1/orders Заказ успешно создается как с цветом, так и без него")
    @Description("Проверяем, что заказ создаётся с цветами: null, BLACK, GREY, BLACK+GREY, и в ответе есть track")
    public void createOrderWithDifferentColors() {
        Order order = new Order(
                "Naruto",
                "Uchiha",
                "Konoha, 142 apt.",
                "4",
                "+7 800 355 35 35",
                5,
                "2020-06-06",
                "Saske, come back to Konoha",
                colors
        );

        Response response = sendCreateOrderRequest(order);
        verifyOrderStatus(response);
        verifyOrderTrackPresent(response);
    }
}