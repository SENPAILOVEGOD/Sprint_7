package clients;

import io.restassured.response.Response;
import models.Courier;
import models.LoginCourier;

public class CourierClient extends BaseClient {

    private static final String COURIER_PATH = "/api/v1/courier";
    private static final String COURIER_LOGIN_PATH = COURIER_PATH + "/login";

    public Response createCourier(Courier courier) {
        return getBaseSpec()
                .body(courier)
                .when()
                .post(COURIER_PATH);
    }

    public Response loginCourier(Courier courier) {
        LoginCourier loginCourier = new LoginCourier(courier.getLogin(), courier.getPassword());
        return getBaseSpec()
                .body(loginCourier)
                .when()
                .post(COURIER_LOGIN_PATH);
    }

    public Response loginCourierWithoutBody() {
        return getBaseSpec()
                .when()
                .post(COURIER_LOGIN_PATH);
    }

    public Response loginCourierWithOnlyLogin(String login) {
        LoginCourier loginCourier = new LoginCourier(login, null);
        return getBaseSpec()
                .body(loginCourier)
                .when()
                .post(COURIER_LOGIN_PATH);
    }

    public Response loginCourierWithOnlyPassword(String password) {
        LoginCourier loginCourier = new LoginCourier(null, password);
        return getBaseSpec()
                .body(loginCourier)
                .when()
                .post(COURIER_LOGIN_PATH);
    }

    public Response deleteCourier(int courierId) {
        return getBaseSpec()
                .when()
                .delete(COURIER_PATH + "/" + courierId);
    }

    public Response deleteCourierWithoutId() {
        return getBaseSpec()
                .when()
                .delete(COURIER_PATH);
    }

}