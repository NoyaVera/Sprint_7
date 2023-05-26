import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import order.Order;
import order.OrderClient;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private final OrderClient orderClient = new OrderClient();
    private final String[] color;
    private int track;

    public CreateOrderTest(String[] color) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "Выбранные цвета: {0} {1}")
    public static Object[][] getColour() {
        return new Object[][]{
                {new String[]{"GRAY"}},
                {new String[]{"BLACK"}},
                {new String[]{"GRAY", "BLACK"}},
                {new String[]{}}
        };
    }

    @Test
    @DisplayName("Создание заказа с выбором цвета")
    public void createOrderWithChoiceColors() {
        Order order = new Order(color);
        ValidatableResponse response = OrderClient.createOrder(order);
        track = response.extract().path("track");
        response.assertThat()
                .statusCode(SC_CREATED)
                .body("track", is(notNullValue()));

    }

    @After
    public void cleanUp() {
        orderClient.deleteOrder(track);
    }
}
