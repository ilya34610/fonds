package ru.pssbd.fonds.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.pssbd.fonds.dto.input.CapitalSourceInput;
import ru.pssbd.fonds.dto.output.CapitalSourceOutput;
import ru.pssbd.fonds.dto.output.CurrencyTypeOutput;
import ru.pssbd.fonds.dto.output.DonationTypeOutput;
import ru.pssbd.fonds.dto.output.UserOutput;
import ru.pssbd.fonds.mappers.CapitalSourceMapper;
import ru.pssbd.fonds.mappers.UserMapper;
import ru.pssbd.fonds.service.*;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/capital_sources")
@RequiredArgsConstructor
public class CapitalSourceEndpoint {

    private final CapitalSourceService service;
    private final UserService userService;
    private final CurrencyTypeService currencyTypeService;
    private final DonationTypeService donationTypeService;
    private final FondService fondService;
    private final RoleService roleService;


    private final CapitalSourceMapper mapper;
    private final UserMapper userMapper;


    //Открытие справочника
    @GetMapping
    @ResponseBody
    public ModelAndView capitalSources(Authentication authentication) {
        ModelAndView mav = new ModelAndView("capital_source/capital_sources");
        String username = authentication.getName();
        UserOutput currentRoleOutput = userService.getRoleByLogin(username);

        if (currentRoleOutput.getRole().getName().equals("DONATER")) {
            mav.addObject("capital_sources", service.getAllElemForCurrentDonater(currentRoleOutput));

            mav.addObject("capitalSource_fonds", fondService.getAllFondsForCurrentDonater(currentRoleOutput));
        } else {
            mav.addObject("capitalSource_fonds", service.getAllCapitalSource());
        }
        return mav;
    }

    //переход на страницу добавление
    @GetMapping("/add")
    public ModelAndView add(Authentication authentication) {
        ModelAndView mav = new ModelAndView("capital_source/capital_source");
        CapitalSourceOutput newOutput = new CapitalSourceOutput();
        newOutput.setDonationDate(LocalDate.now());


        List<CurrencyTypeOutput> currencyTypeOutputList = currencyTypeService.getAllElem();
        mav.addObject("currencyTypes", currencyTypeOutputList);

        List<DonationTypeOutput> donationTypeOutputList = donationTypeService.getAllElem();
        mav.addObject("donationTypes", donationTypeOutputList);

        String username = authentication.getName();
        UserOutput currentRoleOutput = userService.getRoleByLogin(username);

//        if (currentRoleOutput.getRole().getName().equals("DONATER")) {
            mav.addObject("currentUser", userService.getElemById(currentRoleOutput.getId()));
//            newOutput.setUser(userService.getElemById(currentRoleOutput.getId()));


//        }
//        else {
//            mav.addObject("users", userService.getAllElem());
//        }
        mav.addObject("fonds", fondService.getAllElem());

        mav.addObject("capitalSource", newOutput);
        return mav;
    }

    //переход на страницу редактирования
    @GetMapping("/{id}")
    public ModelAndView edit(@PathVariable short id) {
        ModelAndView mav = new ModelAndView("capital_source/capital_source");
//        mav.addObject("capitalSource", service.getElemById(id));
//        List<CountryOutput> countryList = service.getAllCountries();
//        mav.addObject("countries", countryList);
//
//        mav.addObject("selectedCountry", service.getElemById(id).getCountry());
        return mav;
    }

    //сохранение
    @PostMapping
    public String add(@Validated @RequestBody CapitalSourceInput input) {
        service.save(mapper.fromInput(input));
        return "redirect:/capital_sources";
    }

    //удаление
    @DeleteMapping("{id}")
    @ResponseBody
    public String remove(@PathVariable BigInteger id) {
//        service.deleteById(id);
        return "redirect:/capital_sources";
    }

    //изменить
    @PutMapping("{id}")
    @ResponseBody
    public void edit(@PathVariable BigInteger id, @Validated @RequestBody CapitalSourceInput input) {
//        service.putById(id, input);
    }

}
