import courier.Courier;
import courier.CourierClient;
import courier.CourierCredentials;
import courier.CourierGenerator;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.*;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.*;

public class CreateCourierTest {
    private final CourierClient courierClient = new CourierClient();
    private Courier courier;
    private int id;

    @Step("Ассерт успешного создания курьера")
    public void createCourierDone(ValidatableResponse response) {
        response.assertThat()
                .statusCode(SC_CREATED)
                .body("ok", is(true));
    }
    @Step("Ассерт создания курьера с неполными данными")
    public void createCourierFail(ValidatableResponse response) {
        response.assertThat()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
    @Step("Ассерт создания курьера с уже существующим логином")
    public void doubleLogin(ValidatableResponse response) {
        response.assertThat()
                .statusCode(SC_CONFLICT)
                .body("message", equalTo("Этот логин уже используется"));
    }

    @Test
    @DisplayName("Создание курьера")
    public void createCourierTest() {
        courier = CourierGenerator.getRandom();
        ValidatableResponse response = courierClient.createCourier(courier);
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        id = loginResponse.extract().path("id");
        createCourierDone(response);
    }
    @Test
    @DisplayName("Создание курьера без имени")
    public void createCourierWithoutFirstNameTest() {
        courier = CourierGenerator.getRandomWithoutFirstName();
        ValidatableResponse response = courierClient.createCourier(courier);
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        id = loginResponse.extract().path("id");
        createCourierFail(response);
    }
    @Test
    @DisplayName("Создание курьера без пароля")
    public void createCourierWithoutPasswordTest() {
        courier = CourierGenerator.getRandomWithoutPassword();
        ValidatableResponse response = courierClient.createCourier(courier);
        createCourierFail(response);
    }
    @Test
    @DisplayName("Создание курьера без логина")
    public void createCourierWithoutLoginTest() {
        courier = CourierGenerator.getRandomWithoutLogin();
        ValidatableResponse response = courierClient.createCourier(courier);
        createCourierFail(response);
    }
    @Test
    @DisplayName("Создание уже существующего курьера")
    public void sameCouriersCreateTest() {
        courier = CourierGenerator.getRandom();
        courierClient.createCourier(courier);
        ValidatableResponse response = courierClient.createCourier(courier);
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        id = loginResponse.extract().path("id");
        doubleLogin(response);
    }

    @After
    public void cleanUp() {
        if (id != 0) {
            courierClient.deleteCourier(id);
        }
    }

}
