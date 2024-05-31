package ru.pssbd.fonds.endpoint;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.pssbd.fonds.service.CapitalSourceService;

@Controller
public class HomeController {

    private final CapitalSourceService service;

    public HomeController(CapitalSourceService service) {
        this.service = service;
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/capitalSourceGetAll")
    public ModelAndView otherPage() {
        ModelAndView mav = new ModelAndView("/CapitalSource/capitalSourceGetAll");
        mav.addObject("capitalSources", service.getAllElem());
        return mav;

    }

}
