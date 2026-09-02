package helpers;

import io.restassured.response.Response;
import models.Courier;

public class CourierCreationResult {
    private final Courier courier;
    private final Response response;
    private final int id;

    public CourierCreationResult(Courier courier, Response response, int id) {
        this.courier = courier;
        this.response = response;
        this.id = id;
    }

    public Courier getCourier() {
        return courier;
    }

    public Response getResponse() {
        return response;
    }

    public int getId() {
        return id;
    }
}
