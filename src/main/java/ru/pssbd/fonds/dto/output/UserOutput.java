package ru.pssbd.fonds.dto.output;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserOutput {
    private Integer id;
    private RoleOutput role;
    private String fio;
    private String login;
    private String password;
    private String phone;
}
