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


    @GetMapping("/user")
    public ResponseEntity<List<AccountDto>> getAllAccountByUserId() {
        return ResponseEntity.ok(accountService.getAllAccount());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccount(@PathVariable String id){
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AccountDto> addMoneyAccount(@PathVariable String id){
        return ResponseEntity.ok(accountService.addMoneyAccount(id));
    }


    @PostMapping
    public ResponseEntity<AccountDto> createAccount(@RequestBody CreateAccountRequest createAccountRequest){
        return ResponseEntity.ok(accountService.createAccount(createAccountRequest));
    }

    //ToCreate initial PLN account
    @PostMapping("/pln")
    public ResponseEntity<AccountDto> createPlnAccount(){
        return ResponseEntity.ok(accountService.createPlnAccount());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable String id){
        return ResponseEntity.ok(accountService.deleteAccountById(id));
    }

}
