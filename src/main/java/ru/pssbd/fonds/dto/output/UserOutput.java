package ru.pssbd.fonds.dto.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data

public class UserOutput {
    private Integer id;
    private RoleOutput role;
    private String fio;
    private String login;
    private String password;
    private String phone;
    private Boolean canLogin;
    private String mailCode;

    public UserOutput(Integer id, RoleOutput role, String fio, String login, String password, String phone, Boolean canLogin, String mailCode) {
        this.id = id;
        this.role = role;
        this.fio = fio;
        this.login = login;
        this.password = password;
        this.phone = phone;
        this.canLogin = canLogin;
        this.mailCode=mailCode;
    }
    public UserOutput(){

    }

}
