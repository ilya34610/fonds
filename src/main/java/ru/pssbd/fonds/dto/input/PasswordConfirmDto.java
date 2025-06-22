package ru.pssbd.fonds.dto.input;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PasswordConfirmDto {
    private String login;
    private String code;

    public PasswordConfirmDto(String login) {
        this.login = login;
    }
}
