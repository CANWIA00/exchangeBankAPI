package com.canwia.BankExchange.util;

import com.canwia.BankExchange.model.Account;
import com.canwia.BankExchange.model.Role;
import com.canwia.BankExchange.model.User;
import com.canwia.BankExchange.repository.AccountRepository;
import com.canwia.BankExchange.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Component
public class DataMigration  implements CommandLineRunner{

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataMigration(AccountRepository accountRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        // Create and save a new user first
        User user = new User();
        user.setName(UUID.randomUUID().toString());
        user.setName("Canwia");
        user.setSurname("Test");
        user.setEmail("test@test.com");
        user.setPassword(passwordEncoder.encode("123"));
        user.setRole(Role.USER);
        userRepository.saveAndFlush(user);  // Ensure user is saved

        // Create Zloty Account
        Account zlotyAccount = new Account();
        zlotyAccount.setUser(User.builder()
                        .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .build());
        zlotyAccount.setCurrency("Zloty");
        zlotyAccount.setCurrencyCode("PLN");
        zlotyAccount.setBalance(new BigDecimal("5000.00"));
        zlotyAccount.setCreatedAt(LocalDateTime.now());
        accountRepository.save(zlotyAccount);

        // Create Dollar Account
        Account dollarAccount = new Account();
        dollarAccount.setUser(User.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .build());
        dollarAccount.setCurrency("Dollar");
        dollarAccount.setCurrencyCode("USD");
        dollarAccount.setBalance(new BigDecimal("30.00"));
        dollarAccount.setCreatedAt(LocalDateTime.now());
        accountRepository.save(dollarAccount);

        System.out.println("User and accounts created successfully.");
    }


    @PreDestroy
    public void cleanUp() {
        // Delete all data when the application shuts down
        accountRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Override
    public void run(String... args) throws Exception {
        System.out.println("***********************************");
        List<Account> list = accountRepository.findAll();

        list.forEach(System.out::println);

    }
}
