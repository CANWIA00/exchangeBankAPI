package com.canwia.BankExchange.controller;


import com.canwia.BankExchange.dto.AccountDto;
import com.canwia.BankExchange.dto.requests.CreateAccountRequest;
import com.canwia.BankExchange.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }


    //****** TODO Valid userId
    @GetMapping("/user/{id}")
    ResponseEntity<List<AccountDto>> getAllAccountByUserId(@PathVariable String id) {
        return ResponseEntity.ok(accountService.getAllAccount(id));
    }

    @GetMapping("/{id}")
    ResponseEntity<AccountDto> getAccount(@PathVariable String id){
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @PostMapping
    ResponseEntity<AccountDto> createAccount(@RequestBody CreateAccountRequest createAccountRequest){
        return ResponseEntity.ok(accountService.createAccount(createAccountRequest));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteAccount(@PathVariable String id){
        return ResponseEntity.ok(accountService.deleteAccountById(id));
    }

}
