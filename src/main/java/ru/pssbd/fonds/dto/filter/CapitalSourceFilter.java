package ru.pssbd.fonds.dto.filter;

import com.github.mikesafonov.specification.builder.starter.annotations.Like;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CapitalSourceFilter {

    @Like
    private String name;

    public CapitalSourceFilter(String name) {
        this.name = name;
    }
}
