import com.codeborne.selenide.Condition;
import com.codeborne.selenide.WebDriverRunner;
import domain.RegistrationDto;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.RegistrationDtoRepository;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthTest {
    private static RegistrationDtoRepository repository;

    private RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();


    RegistrationDto createUser(String status) {
        RegistrationDto userInfo = repository.GetNewUser(status);
        given()
                .spec(requestSpec)
                .body(userInfo)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);

        return  userInfo;
    }

    @Test
    public void shouldNotLoginIfUserNotRegistered()
    {
        RegistrationDto userInfo = repository.GetNewUser("active");

        $("[name='login']").setValue(userInfo.login);
        $("[name='password']").setValue(userInfo.password);
        $("[data-test-id='action-login']").click();

        String text = $("[data-test-id='error-notification']").getValue();
        $("[data-test-id='error-notification']")
                .shouldBe(Condition.visible, Duration.ofSeconds(5))
                .shouldHave(Condition.text("Ошибка\nОшибка! Неверно указан логин или пароль"));
    }

    @Test
    public void shouldNotLoginIfUserBlocked()
    {
        RegistrationDto userInfo = createUser("blocked");

        $("[name='login']").setValue(userInfo.login);
        $("[name='password']").setValue(userInfo.password);
        $("[data-test-id='action-login']").click();

        $("[data-test-id='error-notification']")
                .shouldBe(Condition.visible, Duration.ofSeconds(5))
                .shouldHave(Condition.text("Ошибка\nОшибка! Пользователь заблокирован"));
    }

    @Test
    public void shouldNotLoginIfPasswordNotValid()
    {
        RegistrationDto userInfo = createUser("blocked");

        $("[name='login']").setValue(userInfo.login);
        $("[name='password']").setValue(userInfo.password+"123");
        $("[data-test-id='action-login']").click();

        $("[data-test-id='error-notification']")
                .shouldBe(Condition.visible, Duration.ofSeconds(5))
                .shouldHave(Condition.text("Ошибка\nОшибка! Неверно указан логин или пароль"));
    }

    @Test
    public void shouldLoginIfUserRegistered()
    {
        RegistrationDto userInfo = createUser("active");

        $("[name='login']").setValue(userInfo.login);
        $("[name='password']").setValue(userInfo.password);
        $("[data-test-id='action-login']").click();

        String currentUrl = WebDriverRunner.getWebDriver().getCurrentUrl();
        assertEquals("http://localhost:9999/dashboard", currentUrl);
    }

    @BeforeEach
    public void openBrowser() {
        open("http://localhost:9999");
    }

    @BeforeAll
    public static void init() {
       repository = new RegistrationDtoRepository();
    }


}
