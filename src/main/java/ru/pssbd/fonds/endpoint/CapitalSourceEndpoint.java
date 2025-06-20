package ru.pssbd.fonds.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.pssbd.fonds.dto.input.CapitalSourceInput;
import ru.pssbd.fonds.dto.output.CapitalSourceOutput;
import ru.pssbd.fonds.dto.output.CurrencyTypeOutput;
import ru.pssbd.fonds.dto.output.DonationTypeOutput;
import ru.pssbd.fonds.entity.UserEntity;
import ru.pssbd.fonds.mappers.CapitalSourceMapper;
import ru.pssbd.fonds.mappers.UserMapper;
import ru.pssbd.fonds.service.*;

import javax.servlet.http.HttpSession;
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
    public ModelAndView capitalSources( @RequestParam(value = "fondName", required = false) String fondName, HttpSession session) {
        ModelAndView mav = new ModelAndView("capital_source/capital_sources");

        //функицонал при котором под определенной ролью подгружаются разные данные
//        String username = authentication.getName();
//        UserOutput currentRoleOutput = userService.getRoleByLogin(username);

//        if (currentRoleOutput.getRole().getName().equals("DONATER")) {
//            mav.addObject("capital_sources", service.getAllElemForCurrentDonater(currentRoleOutput));
//
//            // по идее это надо будет вернуть
////            mav.addObject("capitalSource_fonds", fondService.getAllFondsForCurrentDonater(currentRoleOutput));
//        } else {
////            mav.addObject("capitalSource_fonds", service.searchCapitalSources(fondName));
//            mav.addObject("capitalSources", service.getAllCapitalSource());
//            mav.addObject("donationTypes", donationTypeService.getAllElem());
//
//        }

        mav.addObject("capitalSources", service.getAllCapitalSource());
        mav.addObject("donationTypes", donationTypeService.getAllElem());
        mav.addObject("sum_capitalSource_for_query", service.sumOnDonationType());


        UserEntity user = (UserEntity) session.getAttribute("currentUser");
        String role = userService.getUserByLogin(user.getLogin())
                .orElseThrow()
                .getRole()
                .getName();
        mav.addObject("role", role);


        return mav;
    }

    //переход на страницу добавление
    @GetMapping("/add")
    public ModelAndView add() {
        ModelAndView mav = new ModelAndView("capital_source/capital_source");
        CapitalSourceOutput newOutput = new CapitalSourceOutput();
        newOutput.setDonationDate(LocalDate.now());


        List<CurrencyTypeOutput> currencyTypeOutputList = currencyTypeService.getAllElem();
        mav.addObject("currencyTypes", currencyTypeOutputList);

        List<DonationTypeOutput> donationTypeOutputList = donationTypeService.getAllElem();
        mav.addObject("donationTypes", donationTypeOutputList);

        // убрал
//        String username = authentication.getName();
//        UserOutput currentRoleOutput = userService.getRoleByLogin(username);

//        mav.addObject("currentUser", userService.getElemById(currentRoleOutput.getId()));

        mav.addObject("fonds", fondService.getAllElem());

        mav.addObject("capitalSource", newOutput);
        return mav;
    }

    //переход на страницу редактирования
    @GetMapping("/{id}")
    public ModelAndView edit(@PathVariable short id) {
        ModelAndView mav = new ModelAndView("capital_source/capital_source");
        mav.addObject("capitalSource", service.getElemById(id));
        mav.addObject("fonds", fondService.getAllElem());
        mav.addObject("donationTypes", donationTypeService.getAllElem());
        mav.addObject("currencyTypes", currencyTypeService.getAllElem());




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
        service.deleteById(id);
        return "redirect:/capital_sources";
    }

    //изменить
    @PutMapping("{id}")
    @ResponseBody
    public void edit(@PathVariable BigInteger id, @Validated @RequestBody CapitalSourceInput input) {
        service.putById(id, input);
    }

    //для запросов
    @GetMapping("/capital_source_for_requests")
    @ResponseBody
    public ModelAndView capitalSources() {
        ModelAndView mav = new ModelAndView("capital_source/capital_source_for_requests");

        mav.addObject("forLeftJoin", service.getElemForLeftJoin());

        mav.addObject("forRightJoin", service.getElemForRightJoin());


        return mav;
    }

    @GetMapping("/searchByFond")
    public List<CapitalSourceOutput> searchCapitalSources(@RequestParam(value = "fondName", required = false) String fondName) {
        return service.searchCapitalSources(fondName);
    }


}
