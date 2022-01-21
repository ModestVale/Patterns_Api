package repository;

import com.github.javafaker.Faker;
import domain.RegistrationDto;

import java.util.Locale;

public class RegistrationDtoRepository {

    private Faker faker;

    public  RegistrationDtoRepository()
    {
        faker = new Faker(new Locale("ru"));
    }

    public RegistrationDto GetNewUser(String status)
    {
        RegistrationDto user = new RegistrationDto();
        user.login = faker.regexify("[A-Za-z]{4,10}");
        user.password = faker.internet().password();
        user.status = status;
        return  user;
    }
}
