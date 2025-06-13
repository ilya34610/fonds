package ru.pssbd.fonds.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.pssbd.fonds.entity.UserEntity;
import ru.pssbd.fonds.service.CapitalSourceService;
import ru.pssbd.fonds.service.UserService;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final CapitalSourceService service;
    private final UserService userService;


    @GetMapping("/index")
    public ModelAndView index(HttpSession session) {
        ModelAndView mav = new ModelAndView("/index");
        UserEntity user = (UserEntity) session.getAttribute("currentUser");
        String role = userService.getUserByLogin(user.getLogin())
                .orElseThrow()
                .getRole()
                .getName();
        mav.addObject("role", role);

        mav.addObject("TopThreeDonators", service.getTopThreeDonator());
        return mav;
    }

    @GetMapping("/capitalSourceGetAll")
    public ModelAndView otherPage() {
        ModelAndView mav = new ModelAndView("/CapitalSource/capitalSourceGetAll");
        mav.addObject("capitalSources", service.getAllElem());
        return mav;

    }

    @GetMapping("/forTableInKP")
    public ModelAndView tableInKP() {
        ModelAndView mav = new ModelAndView("/forTableInKP");
//        mav.addObject("capitalSources", service.getAllElem());
        return mav;

    }

}
