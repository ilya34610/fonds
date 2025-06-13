package ru.pssbd.fonds.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import ru.pssbd.fonds.dto.input.UserInput;
import ru.pssbd.fonds.dto.output.UserOutput;
import ru.pssbd.fonds.entity.UserEntity;
import ru.pssbd.fonds.mappers.UserMapper;
import ru.pssbd.fonds.service.AuthenticationService;
import ru.pssbd.fonds.service.UserService;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserEndpoint {

    private final UserService userService;
    private final UserMapper mapper;
    private final AuthenticationService authenticationService;


    //Открытие справочника
    @GetMapping
    @ResponseBody
    public ModelAndView users(HttpSession session) {
        ModelAndView mav = new ModelAndView("user/users");
        mav.addObject("users", userService.getAllElemWithCanLogin());
        mav.addObject("registrationRequestUsers", userService.getAllElemWithCanNotLogin());

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
        ModelAndView mav = new ModelAndView("user/user");
        mav.addObject("user", new UserOutput());
        return mav;
    }

    //переход на страницу редактирования
    @GetMapping("/{id}")
    public ModelAndView edit(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView("user/user");
        mav.addObject("user", userService.getElemById(id));
        return mav;
    }

    //сохранение
    @PostMapping
    @ResponseBody
    public String add(@Validated @RequestBody UserInput input) {
        userService.save(mapper.fromInput(input));
        return "redirect:/users";
    }

    //удаление
    @DeleteMapping("{id}")
    @ResponseBody
    public String remove(@PathVariable Integer id) {
        userService.deleteById(id);
        return "redirect:/users";
    }

    //изменить
    @PutMapping("{id}")
    @ResponseBody
    public void edit(@PathVariable Integer id, @Validated @RequestBody UserInput input) {
        userService.putById(id, input);
    }

    //принять запрос на регистрацию
    @PostMapping("acceptReg/{id}")
    public RedirectView acceptRegistration(@PathVariable Integer id) {
        UserEntity userEntity = userService.get(id);
        userEntity.setCanLogin(true);
        userService.save(userEntity);
        authenticationService.sendEmail("Вашу заявку на регистрацию одобрили.", mapper.toOutput(userEntity));

        return new RedirectView("/users", true);
    }

    //отклонить запрос на регистрацию
    @PostMapping("rejectReg/{id}")
    public RedirectView rejectRegistration(@PathVariable Integer id) {
        userService.deleteById(id);

        return new RedirectView("/users", true);
    }

}
