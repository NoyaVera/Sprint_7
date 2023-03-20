import courier.*;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

public class AuthorizationTest {
    private final Authorization authorization = new Authorization();
    private final CourierClient courierClient = new CourierClient();
    private Courier courier;
    private int id;

    @Test
    @DisplayName("Успешная авторизация")
    public void authDoneTest() {
        courier = CourierGenerator.getRandom();
        courierClient.createCourier(courier);
        ValidatableResponse response = courierClient.login(CourierCredentials.from(courier));
        id = response.extract().path("id");
        authorization.authSuccess(response);
    }

    @Test
    @DisplayName("Авторизация без логина")
    public void authWithoutLoginTest() {
        courier = CourierGenerator.getRandomWithoutLogin();
        courierClient.createCourier(courier);
        ValidatableResponse response = courierClient.login(CourierCredentials.from(courier));
        authorization.authFail(response);
    }

    @Test
    @DisplayName("Авторизация без пароля")
    public void authWithoutPasswordTest() {
        courier = CourierGenerator.getRandomWithoutPassword();
        courierClient.createCourier(courier);
        ValidatableResponse response = courierClient.login(CourierCredentials.from(courier));
        authorization.authFail(response);
    }

    @Test
    @DisplayName("Авторизация под несуществующей учетной записью")
    public void authWithNullUserTest() {
        courier.setLogin("Tom");
        courier.setPassword("82934042");
        ValidatableResponse response = courierClient.login(CourierCredentials.from(courier));
        authorization.authWithNullUser(response);
    }

    @After
    public void cleanUp() {
        if (id != 0) {
            courierClient.deleteCourier(id);
        }
    }
}
