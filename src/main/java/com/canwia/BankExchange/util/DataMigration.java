package com.canwia.BankExchange.util;

import com.canwia.BankExchange.model.Account;
import com.canwia.BankExchange.repository.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class DataMigration implements CommandLineRunner {

    private final AccountRepository accountRepository;

    public DataMigration(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        accountRepository.deleteAll();

        Account account = new Account();
        account.setCurrency("Zloty");
        account.setCurrencyCode("PLN");
        account.setBalance(new BigDecimal("5000.00"));
        account.setCreatedAt(LocalDateTime.now());

        Account account2 = new Account();
        account2.setCurrency("Dollar");
        account2.setCurrencyCode("USD");
        account2.setBalance(new BigDecimal("30.00"));
        account2.setCreatedAt(LocalDateTime.now());


        accountRepository.save(account);
        accountRepository.save(account2);

    }
}
