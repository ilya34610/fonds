package ru.pssbd.fonds.service;

import org.springframework.stereotype.Service;
import ru.pssbd.fonds.dto.input.CategoryInput;
import ru.pssbd.fonds.dto.output.CategoryOutput;
import ru.pssbd.fonds.entity.CategoryEntity;
import ru.pssbd.fonds.mappers.CategoryMapper;
import ru.pssbd.fonds.repository.CategoryRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    public CategoryService(CategoryRepository repository, CategoryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<CategoryOutput> getAllElem() {
        return repository.findAll().stream()
                .map(mapper::toOutput)
                .collect(Collectors.toList());
    }

    public CategoryOutput getElemById(int id) {
        return repository.findById((short) id)
                .map(mapper::toOutput)
                .orElseThrow(() -> new NoSuchElementException("Элемент с id " + id + " не найден"));
    }

    public CategoryEntity save(CategoryEntity entity) {
        return repository.save(entity);
    }


    public void deleteById(Short id) {
        repository.deleteById(id);
    }

    public void putById(Short id, CategoryInput input) {
        CategoryEntity entity = repository.findById(id)
                .map(existngEntity -> mapper.fromInput(input, existngEntity))
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + id));
        repository.save(entity);
    }

}
