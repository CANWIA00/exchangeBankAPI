package com.canwia.BankExchange.util;

import com.canwia.BankExchange.model.Account;
import com.canwia.BankExchange.repository.AccountRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/*
@Component
public class DataMigration  implements CommandLineRunner{

    private final AccountRepository accountRepository;

    public DataMigration(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @PostConstruct
    public void init() {
        Account account = new Account();
        account.setUserId(UUID.fromString("1b38e8e4-944f-424b-8317-fb49c62e6ae3"));
        account.setCurrency("Zloty");
        account.setCurrencyCode("PLN");
        account.setBalance(new BigDecimal("5000.00"));
        account.setCreatedAt(LocalDateTime.now());

        Account account2 = new Account();
        account2.setUserId(UUID.fromString("1b38e8e4-944f-424b-8317-fb49c62e6ae3"));
        account2.setCurrency("Dollar");
        account2.setCurrencyCode("USD");
        account2.setBalance(new BigDecimal("30.00"));
        account2.setCreatedAt(LocalDateTime.now());



        accountRepository.save(account);
        accountRepository.save(account2);
    }


    @PreDestroy
    public void cleanUp() {
        // Delete all data when the application shuts down
        accountRepository.deleteAll();
    }


    @Override
    public void run(String... args) throws Exception {
        System.out.println("***********************************");
        List<Account> list = accountRepository.findAll();

        list.forEach(System.out::println);

    }
}*/
