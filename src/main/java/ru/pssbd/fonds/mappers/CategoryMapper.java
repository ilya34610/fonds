package ru.pssbd.fonds.mappers;

import org.springframework.stereotype.Component;
import ru.pssbd.fonds.dto.input.CategoryInput;
import ru.pssbd.fonds.dto.output.CategoryOutput;
import ru.pssbd.fonds.entity.CategoryEntity;

@Component
public class CategoryMapper {

    public CategoryEntity fromInput(CategoryInput input, CategoryEntity entity) {
        entity.setName(input.getName());
        return entity;
    }

    public CategoryOutput toOutput(CategoryEntity entity) {
        CategoryOutput output = new CategoryOutput();
        output.setId(entity.getId());
        output.setName(entity.getName());
        return output;
    }

    public CategoryEntity fromInput(CategoryInput input) {
        return fromInput(input, new CategoryEntity());
    }
}
