package ru.pssbd.fonds.dto.input;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.*;



@Data
@RequiredArgsConstructor
public class UserInput {

    @NotNull
    @Min(1)
    @Max(5)
    private Short role;
    @NotBlank
    @Size(min = 8,max = 70)
    private String fio;
    @NotBlank
    @Email
    @Size (min = 8,max = 50)
    private String login;
    @NotBlank
    @Size (min = 8,max = 70)
    private String password;
    @NotBlank
    @Size (min = 8,max = 70)
    private String confirmPassword;
    @NotBlank
    private String phone;
    @NotBlank(message = "Введите код с картинки")
    private String captcha;

    private Boolean canLogin;

    private String mailCode;


}
