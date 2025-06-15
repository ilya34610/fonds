package ru.pssbd.fonds.dto;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.*;

@Data
@RequiredArgsConstructor
public class UserRegistrationDto {

    @NotNull
    @Min(1)
    @Max(5)
    private Short role;
    @NotBlank
    @Size (min = 8,max = 70)
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

}
