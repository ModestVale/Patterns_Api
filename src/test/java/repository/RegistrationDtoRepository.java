package repository;

import com.github.javafaker.Faker;
import domain.RegistrationDto;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.experimental.UtilityClass;

import java.util.Locale;

import static io.restassured.RestAssured.given;

@UtilityClass
public class RegistrationDtoRepository {

    private static Faker faker = new Faker(new Locale("ru"));

    public static RegistrationDto getNewUser(String status) {
        RegistrationDto user = new RegistrationDto(
                faker.regexify("[A-Za-z]{4,10}"),
                faker.internet().password(),
                status
                );
        return user;
    }

    public static String getPassword() {
        return faker.internet().password();
    }


    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();


    public static RegistrationDto createUser(String status) {
        RegistrationDto userInfo = getNewUser(status);
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
