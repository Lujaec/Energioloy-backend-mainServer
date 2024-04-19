package com.example.backendmainserver.userU.presentation;

import com.example.backendmainserver.userU.application.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/join") //수정해야함
    public String join() {
        userService.join("jinwooo","pwpw");
        return "join success";
    }
}
