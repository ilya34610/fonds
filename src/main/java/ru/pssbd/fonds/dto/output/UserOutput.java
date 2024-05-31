package ru.pssbd.fonds.dto.output;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserOutput {
    private Integer id;
    private RoleOutput role;
    private String fio;
    private String login;
    private String password;
    private String phone;
    private Boolean canLogin;

    public UserOutput(Integer id, RoleOutput role, String fio, String login, String password, String phone, Boolean canLogin) {
        this.id = id;
        this.role = role;
        this.fio = fio;
        this.login = login;
        this.password = password;
        this.phone = phone;
        this.canLogin = canLogin;
    }

}
