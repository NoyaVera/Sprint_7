import courier.Courier;
import courier.CourierClient;
import courier.CourierCredentials;
import courier.CourierGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.*;

public class CreateCourierTest extends CourierClient{
    private Courier courier;
    private int id;

    @Test
    @DisplayName("Создание курьера")
    public void createCourierTest() {
        courier = CourierGenerator.getRandom();
        ValidatableResponse response = createCourier(courier);
        ValidatableResponse loginResponse = login(CourierCredentials.from(courier));
        id = loginResponse.extract().path("id");
        createCourierDone(response);
    }
    @Test
    @DisplayName("Создание курьера без имени")
    public void createCourierWithoutFirstNameTest() {
        courier = CourierGenerator.getRandomWithoutFirstName();
        ValidatableResponse response = createCourier(courier);
        ValidatableResponse loginResponse = login(CourierCredentials.from(courier));
        id = loginResponse.extract().path("id");
        createCourierFail(response);
    }
    @Test
    @DisplayName("Создание курьера без пароля")
    public void createCourierWithoutPasswordTest() {
        courier = CourierGenerator.getRandomWithoutPassword();
        ValidatableResponse response = createCourier(courier);
        createCourierFail(response);
    }
    @Test
    @DisplayName("Создание курьера без логина")
    public void createCourierWithoutLoginTest() {
        courier = CourierGenerator.getRandomWithoutLogin();
        ValidatableResponse response = createCourier(courier);
        createCourierFail(response);
    }
    @Test
    @DisplayName("Создание уже существующего курьера")
    public void sameCouriersCreateTest() {
        courier = CourierGenerator.getRandom();
        createCourier(courier);
        ValidatableResponse response = createCourier(courier);
        ValidatableResponse loginResponse = login(CourierCredentials.from(courier));
        id = loginResponse.extract().path("id");
        doubleLogin(response);
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
