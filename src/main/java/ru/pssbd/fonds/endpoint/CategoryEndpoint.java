package ru.pssbd.fonds.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pssbd.fonds.dto.output.CategoryOutput;
import ru.pssbd.fonds.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryEndpoint {

    private final CategoryService service;

    @GetMapping
    public List<CategoryOutput> getAllEntity() {
        return service.getAllElem();
    }

    @GetMapping("/{id}")
    public CategoryOutput getEntityById(@PathVariable int id) {
        return service.getElemById(id);
    }

}
