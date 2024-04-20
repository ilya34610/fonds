package ru.pssbd.fonds.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.pssbd.fonds.dto.input.DonationTypeInput;
import ru.pssbd.fonds.dto.output.DonationTypeOutput;
import ru.pssbd.fonds.mappers.DonationTypeMapper;
import ru.pssbd.fonds.service.DonationTypeService;

@RestController
@RequestMapping("/donation_types")
@RequiredArgsConstructor
public class DonationTypeEndpoint {

    private final DonationTypeService service;
    private final DonationTypeMapper mapper;


    //Открытие справочника
    @GetMapping
    @ResponseBody
    public ModelAndView donationTypes() {
        ModelAndView mav = new ModelAndView("donation_type/donation_types");
        mav.addObject("donation_types", service.getAllElem());
        return mav;
    }

    //переход на страницу добавление
    @GetMapping("/add")
    public ModelAndView add() {
        ModelAndView mav = new ModelAndView("donation_type/donation_type");
        mav.addObject("donation_type", new DonationTypeOutput());
        return mav;
    }

    //переход на страницу редактирования
    @GetMapping("/{id}")
    public ModelAndView edit(@PathVariable short id) {
        ModelAndView mav = new ModelAndView("donation_type/donation_type");
        mav.addObject("donation_type", service.getElemById(id));
        return mav;
    }

    //сохранение
    @PostMapping
    public String add(@RequestBody DonationTypeInput input) {
        service.save(mapper.fromInput(input));
        return "redirect:/donation_types";
    }

    //удаление
    @DeleteMapping("{id}")
    @ResponseBody
    public String remove(@PathVariable short id) {
        service.deleteById(id);
        return "redirect:/donation_types";
    }

    //изменить
    @PutMapping("{id}")
    @ResponseBody
    public void edit(@PathVariable Short id, @Validated @RequestBody DonationTypeInput input) {
        service.putById(id, input);
    }

}
