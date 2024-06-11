package ru.pssbd.fonds.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.pssbd.fonds.service.CapitalSourceService;

@Controller
public class HomeController {

    private final CapitalSourceService service;

    @Autowired
    public HomeController(CapitalSourceService service) {
        this.service = service;
    }

    @GetMapping("/index")
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView("/index");

        mav.addObject("TopThreeDonators", service.getTopThreeDonator());

        return mav;

    }

//    @GetMapping("/capitalSourceGetAll")
//    public ModelAndView otherPage() {
//        ModelAndView mav = new ModelAndView("/CapitalSource/capitalSourceGetAll");
//        mav.addObject("capitalSources", service.getAllElem());
//        return mav;
//
//    }

    @GetMapping("/forTableInKP")
    public ModelAndView tableInKP() {
        ModelAndView mav = new ModelAndView("/forTableInKP");
//        mav.addObject("capitalSources", service.getAllElem());
        return mav;

    }

}
