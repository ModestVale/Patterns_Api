package repository;

import com.github.javafaker.Faker;
import domain.RegistrationDto;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class RegistrationDtoRepository {

    private Faker faker;

    public RegistrationDtoRepository() {
        faker = new Faker(new Locale("ru"));
    }

    public RegistrationDto getNewUser(String status) {
        RegistrationDto user = new RegistrationDto();
        user.login = faker.regexify("[A-Za-z]{4,10}");
        user.password = faker.internet().password();
        user.status = status;
        return user;
    }

    public String getPassword() {
        return faker.internet().password();
    }


    private RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();


    public RegistrationDto createUser(String status) {
        RegistrationDto userInfo = this.getNewUser(status);
        given()
                .spec(requestSpec)
                .body(userInfo)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);

        return userInfo;
    }
}
