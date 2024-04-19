package com.example.backendmainserver.user.presentation;

import com.example.backendmainserver.user.application.UserService;
import com.example.backendmainserver.user.application.dto.request.JoinRequest;
import com.example.backendmainserver.user.application.dto.response.JoinResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "유저 API", description = "유저 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @Operation(summary = "회원가입 api")
    @PostMapping("/user")
    public JoinResponse join(@RequestBody JoinRequest req) {
        return userService.join(req);
    }

}
