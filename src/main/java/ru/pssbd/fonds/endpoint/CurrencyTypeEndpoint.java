package ru.pssbd.fonds.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.pssbd.fonds.dto.input.CurrencyTypeInput;
import ru.pssbd.fonds.dto.output.CurrencyTypeOutput;
import ru.pssbd.fonds.mappers.CurrencyTypeMapper;
import ru.pssbd.fonds.service.CurrencyTypeService;

@RestController
@RequestMapping("/currency_types")
@RequiredArgsConstructor
public class CurrencyTypeEndpoint {

    private final CurrencyTypeService service;
    private final CurrencyTypeMapper mapper;

    //Открытие справочника
    @GetMapping
    @ResponseBody
    public ModelAndView donationTypes() {
        ModelAndView mav = new ModelAndView("currency_type/currency_types");
        mav.addObject("currency_types", service.getAllElem());


//        mav.addObject("requestWithGroupBy", service.getElemForGroupBy());
        return mav;
    }

    //переход на страницу добавление
    @GetMapping("/add")
    public ModelAndView add() {
        ModelAndView mav = new ModelAndView("currency_type/currency_type");
        mav.addObject("currency_type", new CurrencyTypeOutput());
        return mav;
    }

    //переход на страницу редактирования
    @GetMapping("/{id}")
    public ModelAndView edit(@PathVariable short id) {
        ModelAndView mav = new ModelAndView("currency_type/currency_type");
        mav.addObject("currency_type", service.getElemById(id));
        return mav;
    }

    //сохранение
    @PostMapping
    public String add(@RequestBody CurrencyTypeInput input) {
        service.save(mapper.fromInput(input));
        return "redirect:/currency_types";
    }

    //удаление
    @DeleteMapping("{id}")
    @ResponseBody
    public String remove(@PathVariable short id) {
        service.deleteById(id);
        return "redirect:/currency_types";
    }

    //изменить
    @PutMapping("{id}")
    @ResponseBody
    public void edit(@PathVariable Short id, @Validated @RequestBody CurrencyTypeInput input) {
        service.putById(id, input);
    }


    @GetMapping("/count_of_currency_types")
    @ResponseBody
    public int countOfCurrencyType() {
        return service.getAllElem().size();
    }

}
