package com.canwia.BankExchange.controller;

import com.canwia.BankExchange.dto.UserDto;
import com.canwia.BankExchange.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Service
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping()
    public ResponseEntity<UserDto> getUserByToken() {
        return ResponseEntity.ok(userService.findUser());
    }
}
