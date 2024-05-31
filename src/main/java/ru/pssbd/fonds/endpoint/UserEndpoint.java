package ru.pssbd.fonds.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.pssbd.fonds.dto.input.UserInput;
import ru.pssbd.fonds.dto.output.UserOutput;
import ru.pssbd.fonds.mappers.UserMapper;
import ru.pssbd.fonds.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserEndpoint {

    private final UserService service;
    private final UserMapper mapper;


    //Открытие справочника
    @GetMapping
    @ResponseBody
    public ModelAndView users() {
        ModelAndView mav = new ModelAndView("user/users");
        mav.addObject("users", service.getAllElem());
        return mav;
    }

    //переход на страницу добавление
    @GetMapping("/add")
    public ModelAndView add() {
        ModelAndView mav = new ModelAndView("user/user");
        mav.addObject("user", new UserOutput());
        return mav;
    }

    //переход на страницу редактирования
    @GetMapping("/{id}")
    public ModelAndView edit(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView("user/user");
        mav.addObject("user", service.getElemById(id));
        return mav;
    }

    //сохранение
    @PostMapping
    @ResponseBody
    public String add(@Validated @RequestBody UserInput input) {
        service.save(mapper.fromInput(input));
        return "redirect:/users";
    }

    //удаление
    @DeleteMapping("{id}")
    @ResponseBody
    public String remove(@PathVariable Integer id) {
        service.deleteById(id);
        return "redirect:/users";
    }

    //изменить
    @PutMapping("{id}")
    @ResponseBody
    public void edit(@PathVariable Integer id, @Validated @RequestBody UserInput input) {
        service.putById(id, input);
    }

}
