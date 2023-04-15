import courier.*;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

public class AuthorizationTest extends CourierClient{
    private final Authorization authorization = new Authorization();
    private Courier courier;
    private int id;

    @Test
    @DisplayName("Успешная авторизация")
    public void authDoneTest() {
        courier = CourierGenerator.getRandom();
        createCourier(courier);
        ValidatableResponse response = login(CourierCredentials.from(courier));
        id = response.extract().path("id");
        authorization.authSuccess(response);
    }

    @Test
    @DisplayName("Авторизация без логина")
    public void authWithoutLoginTest() {
        courier = CourierGenerator.getRandomWithoutLogin();
        createCourier(courier);
        ValidatableResponse response = login(CourierCredentials.from(courier));
        authorization.authFail(response);
    }

    @Test
    @DisplayName("Авторизация без пароля")
    public void authWithoutPasswordTest() {
        courier = CourierGenerator.getRandomWithoutPassword();
        createCourier(courier);
        ValidatableResponse response = login(CourierCredentials.from(courier));
        authorization.authFail(response);
    }

    @Test
    @DisplayName("Авторизация под несуществующей учетной записью")
    public void authWithNullUserTest() {
        courier.setLogin("Tom");
        courier.setPassword("82934042");
        ValidatableResponse response = login(CourierCredentials.from(courier));
        authorization.authWithNullUser(response);
    }

    @After
    public void cleanUp() {
        try {
            id = CourierClient.getCourierId(courier.getLogin(), courier.getPassword());
        } catch (Exception e) {
            if (id != 0) {
                deleteCourier(id);
            }
        }
    }
}
