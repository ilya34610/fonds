//package ru.pssbd.fonds.dto.input;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class UserRegistrationInput {
//
//    @NotBlank
//    @Size(min = 4, max = 20)
//    @Pattern(regexp = "^[a-zA-Z0-9_,!?-]+$",
//            message = "Логин должен содержать латиницу, цифры или _ , ! ? -")
//    private String username;
//
//    @NotBlank
//    @Size(min = 4, max = 30)
//    private String password;
//
//    @NotBlank
//    @Size(min = 4, max = 30)
//    private String confirmPassword;
//
//}
