package courier;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

public class Authorization {

    @Step("Успешная авторизация")
    public int authSuccess(ValidatableResponse response) {
        return response.assertThat()
                .statusCode(SC_OK)
                .body("id", greaterThan(0))
                .extract()
                .path("id");
    }


    @Step("Ошибка: недостаточно данных")
    public void authFail(ValidatableResponse response) {
        response.assertThat()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Step("Ошибка: учетной записи не существует")
    public void authWithNullUser(ValidatableResponse response) {
        response.assertThat()
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }
}
