package domain;

import lombok.Data;

@Data
public class RegistrationDto {
    public  RegistrationDto(String login, String password, String status)
    {
        this.login = login;
        this.password = password;
        this.status = status;
    }

    private String login;
    private String password;
    private String status;
}
