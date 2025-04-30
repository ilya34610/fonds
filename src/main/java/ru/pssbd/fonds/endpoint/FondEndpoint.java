package ru.pssbd.fonds.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.pssbd.fonds.dto.input.CapitalSourceInput;
import ru.pssbd.fonds.dto.input.FondCitizenInput;
import ru.pssbd.fonds.dto.input.FondInput;
import ru.pssbd.fonds.dto.output.*;
import ru.pssbd.fonds.entity.FondExpenseEntity;
import ru.pssbd.fonds.mappers.FondMapper;
import ru.pssbd.fonds.repository.CitizensFondsRepository;
import ru.pssbd.fonds.repository.FondExpenseRepository;
import ru.pssbd.fonds.service.CitizenService;
import ru.pssbd.fonds.service.CityService;
import ru.pssbd.fonds.service.FondService;
import ru.pssbd.fonds.service.UserService;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/fonds")
@RequiredArgsConstructor
public class FondEndpoint {

    private final FondService service;
    private final CitizenService citizenService;
    private final FondMapper mapper;
    private final CityService cityService;
    private final UserService userService;

    private final CitizensFondsRepository citizensFondsRepository;
    private final FondExpenseRepository fondExpenseRepository;

    //Открытие справочника
    @GetMapping
    @ResponseBody
    public ModelAndView capitalSources() {
        ModelAndView mav = new ModelAndView("fond/fonds");

        //убрал
//        String username = authentication.getName();
//        UserOutput currentRoleOutput = userService.getRoleByLogin(username);
//        mav.addObject("currentUser", userService.getElemById(currentRoleOutput.getId()));


//        if (currentRoleOutput.getRole().getName().equals("STAFF")) {
//            mav.addObject("fonds", service.getAllElemForCurrentDonater(currentRoleOutput));
//
////            mav.addObject("capitalSource_fonds", fondService.getAllFondsForCurrentDonater(currentRoleOutput));
//        } else {
//        }
        mav.addObject("fonds", service.getAllElem());


        return mav;
    }

    @GetMapping("/distribution")
    @ResponseBody
    public ModelAndView distribution() {
        ModelAndView mav = new ModelAndView("fond/fonds_citizens");
        // убрал
//        String username = authentication.getName();
//        UserOutput currentRoleOutput = userService.getRoleByLogin(username);
//        mav.addObject("currentUser", userService.getElemById(currentRoleOutput.getId()));

        mav.addObject("citizens", citizenService.getAllElem());
        mav.addObject("fonds", service.getAllElem());

        mav.addObject("requestWithCase", service.getElemRequestWithCase());

        return mav;
    }

    //    переход на страницу добавление
    @GetMapping("/add")
    public ModelAndView add() {
        ModelAndView mav = new ModelAndView("fond/fond");
        mav.addObject("users", userService.getAllElemByRoleStaff());
        mav.addObject("cities", cityService.getAllElem());

        FondOutput newOutput = new FondOutput();
        mav.addObject("fond", newOutput);
        return mav;
    }


    //переход на страницу редактирования
    @GetMapping("/{id}")
    public ModelAndView edit(@PathVariable BigInteger id) {
        ModelAndView mav = new ModelAndView("fond/fond");
        mav.addObject("fond", service.getElemById(id));
        List<CityOutput> cityList = cityService.getAllElem();
        mav.addObject("cities", cityList);

        List<UserOutput> userList = userService.getAllElem();
        mav.addObject("users", userList);
//        mav.addObject("selectedCity", service.getElemById(id));
        return mav;
    }

    //сохранение
    @PostMapping
    public String add(@Validated @RequestBody FondInput input) {
        service.save(mapper.fromInput(input));
        return "redirect:/fonds";
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
    public void edit(@PathVariable BigInteger id, @Validated @RequestBody FondInput input) {
        service.putById(id, input);
    }


    @PostMapping("/fond_citizen")
    public ResponseEntity<?> distribution(@Validated @RequestBody FondCitizenInput input) {

        FondOutput fondOutput = service.getElemById(new BigInteger(input.getFondIds().toString()));
        CitizenOutput citizenOutput = citizenService.getElemById(input.getCitizenIds());

        BigDecimal fondSum = fondOutput.getSum();
        BigDecimal citizenSum = citizenOutput.getSum();
        BigDecimal sumDonat = input.getSum();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        Map<String, String> responseBody = new HashMap<>();

        if (fondSum.compareTo(citizenSum) <= 0 || fondSum.compareTo(sumDonat) <= 0) {
            responseBody.put("message", "Недостаточно средств в фонде \"" + fondOutput.getName() + "\"");
            return ResponseEntity.ok().headers(headers).body(responseBody);

        } else if (sumDonat.compareTo(citizenSum) > 0) {
            responseBody.put("message", "Запрашиваемая сумма больше той, которая требуется гражданину: " + citizenOutput.getUser().getFio());
            return ResponseEntity.ok().headers(headers).body(responseBody);

        } else {
            service.minusBalance(sumDonat, fondOutput.getId());

            citizenService.minusBalance(sumDonat, citizenOutput.getId());

            citizensFondsRepository.saveTransaction(citizenOutput.getId(), fondOutput.getId());

            FondExpenseEntity newEntity = new FondExpenseEntity(sumDonat, citizenService.get(citizenOutput.getId()));
            fondExpenseRepository.save(newEntity);

            fondExpenseRepository.saveTransaction(fondOutput.getId(), newEntity.getId().intValue());

            responseBody.put("message", "Сумма успешно переведена");
            responseBody.put("redirectUrl", "/fonds");
            return ResponseEntity.ok().headers(headers).body(responseBody);

        }

//        service.save(mapper.fromInput(input));
//        return "redirect:/fonds_citizens";
    }

}
