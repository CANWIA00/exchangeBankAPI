package com.canwia.BankExchange.service;

import com.canwia.BankExchange.dto.AccountDto;
import com.canwia.BankExchange.dto.converter.AccountDtoConverter;
import com.canwia.BankExchange.dto.requests.CreateAccountRequest;
import com.canwia.BankExchange.model.Account;
import com.canwia.BankExchange.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountDtoConverter accountDtoConverter;

    public AccountService(AccountRepository accountRepository, AccountDtoConverter accountDtoConverter) {
        this.accountRepository = accountRepository;
        this.accountDtoConverter = accountDtoConverter;
    }

    public AccountDto createAccount(CreateAccountRequest createAccountRequest) {

        Account account = new Account();
        account.setCustomerId(UUID.randomUUID());
        account.setBalance(BigDecimal.valueOf(0));
        account.setCurrency(createAccountRequest.getCurrency());
        account.setCurrencyCode(createAccountRequest.getCurrencySymbol());
        account.setCreatedAt(LocalDateTime.now());

        accountRepository.save(account);

        return accountDtoConverter.convertFrom(account);
    }

    protected Optional<Account> findAccountById(String AccountId) {
        return accountRepository.findById(UUID.fromString(AccountId));
    }



}
