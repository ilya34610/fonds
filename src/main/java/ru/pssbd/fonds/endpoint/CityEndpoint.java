package ru.pssbd.fonds.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.pssbd.fonds.dto.input.CityInput;
import ru.pssbd.fonds.dto.output.CityOutput;
import ru.pssbd.fonds.dto.output.CountryOutput;
import ru.pssbd.fonds.mappers.CityMapper;
import ru.pssbd.fonds.service.CityService;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/cities")
@RequiredArgsConstructor
public class CityEndpoint {

    private final CityService service;
    private final CityMapper mapper;


    //Открытие справочника
    @GetMapping
    @ResponseBody
    public ModelAndView cityTypes() {
        ModelAndView mav = new ModelAndView("city/cities");
        mav.addObject("cities", service.getAllElem());
        return mav;
    }

    //переход на страницу добавление
    @GetMapping("/add")
    public ModelAndView add() {
        ModelAndView mav = new ModelAndView("city/city");
        mav.addObject("city", new CityOutput());
        List<CountryOutput> countryList = service.getAllCountries();
        mav.addObject("countries", countryList);
        return mav;
    }

    //переход на страницу редактирования
    @GetMapping("/{id}")
    public ModelAndView edit(@PathVariable short id) {
        ModelAndView mav = new ModelAndView("city/city");
        mav.addObject("city", service.getElemById(id));
        List<CountryOutput> countryList = service.getAllCountries();
        mav.addObject("countries", countryList);

        mav.addObject("selectedCountry", service.getElemById(id).getCountry());
        return mav;
    }

    //сохранение
    @PostMapping
    public String add(@RequestBody CityInput input) {
        service.save(mapper.fromInput(input));
        return "redirect:/cities";
    }

    //удаление
    @DeleteMapping("{id}")
    @ResponseBody
    public String remove(@PathVariable BigInteger id) {
        service.deleteById(id);
        return "redirect:/cities";
    }

    //изменить
    @PutMapping("{id}")
    @ResponseBody
    public void edit(@PathVariable BigInteger id, @Validated @RequestBody CityInput input) {
        service.putById(id, input);
    }

}
