package order;

import courier.Client;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends Client {
    @Step("Создание нового заказа")
    public static ValidatableResponse createOrder(Order order) {
        return given().log().all()
                .spec(Client.getSpec())
                .when()
                .post(PATH_ORDER)
                .then();
    }

    @Step("Получение списка заказов")
    public ValidatableResponse orderList() {
        return given().log().all()
                .spec(Client.getSpec())
                .when()
                .get(PATH_ORDER)
                .then();
    }

    @Step("Удаление заказа")
    public ValidatableResponse deleteOrder(int track) {
        return given().log().all()
                .spec(Client.getSpec())
                .body(track)
                .when()
                .put(PATH_ORDER_CANCEL)
                .then();
    }
}
