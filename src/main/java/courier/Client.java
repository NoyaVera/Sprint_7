package courier;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class Client {
    protected static final String BASE_URL = "https://qa-scooter.praktikum-services.ru/";
    protected static final String PATH_COURIER = "api/v1/courier";
    protected static final String PATH_LOGIN = "api/v1/courier/login";

    protected static RequestSpecification getSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(BASE_URL)
                .build();
    }
}
