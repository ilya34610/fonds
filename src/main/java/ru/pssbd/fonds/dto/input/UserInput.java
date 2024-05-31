package ru.pssbd.fonds.dto.input;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class UserInput {

    @NotNull
    private Short role;

    @NotNull
    private String fio;

    @NotNull
    private String login;

    @NotNull
    private String password;

    @NotNull
    private String phone;

    @NotNull
    private Boolean canLogin;


}
