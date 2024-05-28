package ru.pssbd.fonds.dto.input;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleInput {

    @NotNull
    private String name;

}
