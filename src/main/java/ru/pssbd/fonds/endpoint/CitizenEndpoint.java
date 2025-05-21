package ru.pssbd.fonds.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.pssbd.fonds.dto.output.CitizenOutput;
import ru.pssbd.fonds.dto.output.UserOutput;
import ru.pssbd.fonds.entity.UserEntity;
import ru.pssbd.fonds.service.CitizenService;
import ru.pssbd.fonds.service.FondService;
import ru.pssbd.fonds.service.UserService;

import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/citizens")
@RequiredArgsConstructor
public class CitizenEndpoint {

    private final CitizenService service;
    private final UserService userService;
    private final CitizenService citizenService;
    private final FondService fondService;

    @GetMapping
    public List<CitizenOutput> getAllEntity() {
        return service.getAllElem();
    }

    @GetMapping("/{id}")
    public CitizenOutput getEntityById(@PathVariable BigInteger id) {
        return service.getElemById(id);
    }

    @GetMapping("/citizen_user")
    @ResponseBody
    public ModelAndView distribution(HttpSession session) {
        ModelAndView mav = new ModelAndView("citizen/citizen_user");

        UserEntity user = (UserEntity) session.getAttribute("currentUser");
        String role = userService.getUserByLogin(user.getLogin())
                .orElseThrow()
                .getRole()
                .getName();
        mav.addObject("role", role);


        //убрал
//        String username = authentication.getName();
//        UserOutput currentRoleOutput = userService.getRoleByLogin(username);
//        mav.addObject("currentUser", userService.getElemById(currentRoleOutput.getId()));

//        mav.addObject("currentCitizen", citizenService.getCitizenByUser(currentRoleOutput.getId()));
//
//        mav.addObject("currentFonds", fondService.getFondsByUser(currentRoleOutput.getId()));


        return mav;
    }

}
