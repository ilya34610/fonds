package ru.pssbd.fonds.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.pssbd.fonds.dto.input.CountryInput;
import ru.pssbd.fonds.dto.output.CountryOutput;
import ru.pssbd.fonds.mappers.CountryMapper;
import ru.pssbd.fonds.service.CountryService;

@RestController
@RequestMapping("/countries")
@RequiredArgsConstructor
public class CountryEndpoint {

    private final CountryService service;
    private final CountryMapper mapper;

    //Открытие справочника
    @GetMapping
    @ResponseBody
    public ModelAndView countries() {
        ModelAndView mav = new ModelAndView("country/countries");
        mav.addObject("countries", service.getAllElem());
        return mav;
    }

    //переход на страницу добавление
    @GetMapping("/add")
    public ModelAndView add() {
        ModelAndView mav = new ModelAndView("country/country");
        mav.addObject("country", new CountryOutput());
        return mav;
    }

    //переход на страницу редактирования
    @GetMapping("/{id}")
    public ModelAndView edit(@PathVariable short id) {
        ModelAndView mav = new ModelAndView("country/country");
        mav.addObject("country", service.getElemById(id));
        return mav;
    }

    //сохранение
    @PostMapping
    public String add(@RequestBody CountryInput input) {
        service.save(mapper.fromInput(input));
        return "redirect:/countries";
    }

    //удаление
    @DeleteMapping("{id}")
    @ResponseBody
    public String remove(@PathVariable short id) {
        service.deleteById(id);
        return "redirect:/countries";
    }

    //изменить
    @PutMapping("{id}")
    @ResponseBody
    public void edit(@PathVariable Short id, @Validated @RequestBody CountryInput input) {
        service.putById(id, input);
    }
}
