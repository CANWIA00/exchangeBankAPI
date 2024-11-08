package com.canwia.BankExchange.service;

import com.canwia.BankExchange.dto.AccountDto;
import com.canwia.BankExchange.dto.converter.AccountDtoConverter;
import com.canwia.BankExchange.dto.requests.CreateAccountRequest;
import com.canwia.BankExchange.model.Account;
import com.canwia.BankExchange.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
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

    protected Optional<Account> findAccountById(UUID accountId) {
        Optional<Account> account = accountRepository.findById(accountId);
        return account;
    }


    protected void updateAccount(Account account, BigDecimal newBalance) {
        account.setBalance(newBalance);
        accountRepository.save(account);
    }
}
