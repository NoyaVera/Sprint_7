package courier;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class CourierClient extends Client{
    @Step("Создаем курьера")
    public ValidatableResponse createCourier(Courier courier) {
        return given().log().all()
                .spec(Client.getSpec())
                .body(courier)
                .when()
                .post(PATH_COURIER)
                .then();
    }

    @Step("Логин курьера")
    public ValidatableResponse login(CourierCredentials credentials) {
        return given().log().all()
                .spec(Client.getSpec())
                .body(credentials)
                .when()
                .post(PATH_LOGIN)
                .then();
    }

    @Step("Пробуем получить id созданного курьера")
    public static int getCourierId(String login, String password) {
        Courier courierTest = new Courier(login, password, null);
        return given()
                .spec(getSpec())
                .body(courierTest)
                .post(PATH_LOGIN)
                .then().extract().path("id");
    }

    @Step("Удаляем созданного курьера")
    public void deleteCourier(int id) {
        given().log().all()
                .spec(Client.getSpec())
                .delete(PATH_COURIER + id)
                .then();
    }

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

}
