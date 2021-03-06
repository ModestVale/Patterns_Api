import com.codeborne.selenide.Condition;
import com.codeborne.selenide.WebDriverRunner;
import domain.RegistrationDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.RegistrationDtoRepository;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthTest {
    @Test
    public void shouldNotLoginIfUserNotRegistered() {
        RegistrationDto userInfo = RegistrationDtoRepository.getNewUser("active");

        $("[name='login']").setValue(userInfo.getLogin());
        $("[name='password']").setValue(userInfo.getPassword());
        $("[data-test-id='action-login']").click();

        String text = $("[data-test-id='error-notification']").getValue();
        $("[data-test-id='error-notification']")
                .shouldBe(Condition.visible, Duration.ofSeconds(5))
                .shouldHave(Condition.text("Ошибка\nОшибка! Неверно указан логин или пароль"));
    }

    @Test
    public void shouldNotLoginIfUserBlocked() {
        RegistrationDto userInfo = RegistrationDtoRepository.createUser("blocked");

        $("[name='login']").setValue(userInfo.getLogin());
        $("[name='password']").setValue(userInfo.getPassword());
        $("[data-test-id='action-login']").click();

        $("[data-test-id='error-notification']")
                .shouldBe(Condition.visible, Duration.ofSeconds(5))
                .shouldHave(Condition.text("Ошибка\nОшибка! Пользователь заблокирован"));
    }

    @Test
    public void shouldNotLoginIfPasswordNotValid() {
        RegistrationDto userInfo = RegistrationDtoRepository.createUser("blocked");

        $("[name='login']").setValue(userInfo.getLogin());
        $("[name='password']").setValue(RegistrationDtoRepository.getPassword());
        $("[data-test-id='action-login']").click();

        $("[data-test-id='error-notification']")
                .shouldBe(Condition.visible, Duration.ofSeconds(5))
                .shouldHave(Condition.text("Ошибка\nОшибка! Неверно указан логин или пароль"));
    }

    @Test
    public void shouldNotLoginIfLoginNotValid() {
        RegistrationDto userInfo = RegistrationDtoRepository.createUser("active");

        $("[name='login']").setValue(RegistrationDtoRepository.getLogin());
        $("[name='password']").setValue(userInfo.getPassword());
        $("[data-test-id='action-login']").click();

        $("[data-test-id='error-notification']")
                .shouldBe(Condition.visible, Duration.ofSeconds(5))
                .shouldHave(Condition.text("Ошибка\nОшибка! Неверно указан логин или пароль"));
    }

    @Test
    public void shouldLoginIfUserRegistered() {
        RegistrationDto userInfo = RegistrationDtoRepository.createUser("active");

        $("[name='login']").setValue(userInfo.getLogin());
        $("[name='password']").setValue(userInfo.getPassword());
        $("[data-test-id='action-login']").click();

        String currentUrl = WebDriverRunner.getWebDriver().getCurrentUrl();
        assertEquals("http://localhost:9999/dashboard", currentUrl);
    }

    @BeforeEach
    public void openBrowser() {
        open("http://localhost:9999");
    }
}
