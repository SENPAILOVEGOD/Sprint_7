package clients;

import io.restassured.response.Response;
import models.Order;

import java.util.Map;

public class OrderClient extends BaseClient {

    private static final String ORDER_PATH = "/api/v1/orders";

    public Response createOrder(Order order) {
        return getBaseSpec()
                .body(order)
                .when()
                .post(ORDER_PATH);
    }

    public Response getOrders(Map<String, Object> queryParams) {
        return getBaseSpec()
                .queryParams(queryParams)
                .when()
                .get(ORDER_PATH);
    }

    public Response getOrders() {
        return getOrders(null);
    }
}