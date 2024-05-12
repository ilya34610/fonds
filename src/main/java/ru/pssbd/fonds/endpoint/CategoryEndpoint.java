package ru.pssbd.fonds.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.pssbd.fonds.dto.input.CategoryInput;
import ru.pssbd.fonds.dto.output.CategoryOutput;
import ru.pssbd.fonds.mappers.CategoryMapper;
import ru.pssbd.fonds.service.CategoryService;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryEndpoint {

    private final CategoryService service;
    private final CategoryMapper mapper;

    //Открытие справочника
    @GetMapping
    @ResponseBody
    public ModelAndView categories() {
        ModelAndView mav = new ModelAndView("category/categories");
        mav.addObject("categories", service.getAllElem());
        return mav;
    }

    //переход на страницу добавление
    @GetMapping("/add")
    public ModelAndView add() {
        ModelAndView mav = new ModelAndView("category/category");
        mav.addObject("category", new CategoryOutput());
        return mav;
    }

    //переход на страницу редактирования
    @GetMapping("/{id}")
    public ModelAndView edit(@PathVariable short id) {
        ModelAndView mav = new ModelAndView("category/category");
        mav.addObject("category", service.getElemById(id));
        return mav;
    }

    //сохранение
    @PostMapping
    public String add(@RequestBody CategoryInput input) {
        service.save(mapper.fromInput(input));
        return "redirect:/categories";
    }

    //удаление
    @DeleteMapping("{id}")
    @ResponseBody
    public String remove(@PathVariable short id) {
        service.deleteById(id);
        return "redirect:/categories";
    }

    //изменить
    @PutMapping("{id}")
    @ResponseBody
    public void edit(@PathVariable Short id, @Validated @RequestBody CategoryInput input) {
        service.putById(id, input);
    }

}
