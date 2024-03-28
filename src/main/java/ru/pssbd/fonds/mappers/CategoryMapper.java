package ru.pssbd.fonds.mappers;

import org.springframework.stereotype.Service;
import ru.pssbd.fonds.dto.input.CategoryInput;
import ru.pssbd.fonds.dto.output.CategoryOutput;
import ru.pssbd.fonds.entity.CategoryEntity;

@Service
public class CategoryMapper {

    public CategoryEntity fromInput(CategoryInput input, CategoryEntity entity) {
        entity.setName(input.getName());
        return entity;
    }

    public CategoryOutput toOutput(CategoryEntity entity) {
        return new CategoryOutput(entity.getId(), entity.getName());
    }

    public CategoryEntity fromInput(CategoryInput input) {
        return new CategoryEntity(input.getName());
    }
}
