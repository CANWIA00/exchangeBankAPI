package com.canwia.BankExchange.service;

import com.canwia.BankExchange.auth.AuthUtil;
import com.canwia.BankExchange.dto.AccountDto;
import com.canwia.BankExchange.dto.converter.AccountDtoConverter;
import com.canwia.BankExchange.dto.requests.CreateAccountRequest;
import com.canwia.BankExchange.exception.CustomException;
import com.canwia.BankExchange.model.Account;
import com.canwia.BankExchange.model.Currency;
import com.canwia.BankExchange.model.User;
import com.canwia.BankExchange.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.UUID.fromString;

@Slf4j
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountDtoConverter accountDtoConverter;
    private final CurrencyService currencyService;
    private final UserService userService;

    public AccountService(AccountRepository accountRepository, AccountDtoConverter accountDtoConverter, CurrencyService currencyService, UserService userService) {
        this.accountRepository = accountRepository;
        this.accountDtoConverter = accountDtoConverter;
        this.currencyService = currencyService;
        this.userService = userService;
    }

    public AccountDto createAccount(CreateAccountRequest createAccountRequest) {

        User user = userService.getUserByToken(AuthUtil.handleRequest());
        Currency currency = currencyService.findCurrencyById(createAccountRequest.getCurrencyCode());
        Account account = new Account();
        account.setUser(user);
        account.setBalance(BigDecimal.valueOf(0));
        account.setCurrency(currency.getName());
        account.setCurrencyCode(currency.getCode());
        account.setCreatedAt(LocalDateTime.now());

        accountRepository.save(account);

        return accountDtoConverter.convertFrom(account);
    }

    public AccountDto createPlnAccount() {
        User user = userService.getUserByToken(AuthUtil.handleRequest());
        Account account = new Account();
        account.setUser(user);
        account.setBalance(BigDecimal.valueOf(0));
        account.setCreatedAt(LocalDateTime.now());
        account.setCurrency("Zloty");
        account.setCurrencyCode("PLN");
        return accountDtoConverter.convertFrom(accountRepository.save(account));
    }

    public AccountDto getAccountById(String id) {
        Account account= accountRepository.findById(fromString(id)).orElseThrow(()->new CustomException("Account not found Exception:[AccountService]:A_1"));
        return accountDtoConverter.convertFrom(account);
    }


    public List<AccountDto> getAllAccount() {
        User user = userService.getUserByToken(AuthUtil.handleRequest());

        List<Account> accountList = accountRepository.findAllByUserId(user.getId());
        if(accountList.isEmpty()){
            throw new CustomException("Account not found Exception:[AccountService]:A_2");
        }

        return accountDtoConverter.convertFrom(accountList);
    }

    public String deleteAccountById(String id) {
        Optional<Account> account = findAccountById(fromString(id));
        User user = userService.getUserByToken(AuthUtil.handleRequest());


        if(account.isPresent() &&account.get().getUser().getId().equals(user.getId())){
            accountRepository.deleteById(UUID.fromString(id));
            return "Account deleted successfully";
        }else{
            throw new CustomException("Account not found Exception:[AccountService]:A_3");
        }


    }

    public AccountDto addMoneyAccount(String id) {
        Account account = accountRepository.findById(fromString(id)).orElseThrow(() -> new CustomException("Account not found Exception:[AccountService]:A_1"));
        account.setBalance(account.getBalance().add(BigDecimal.valueOf(1000)));
        return accountDtoConverter.convertFrom(accountRepository.save(account));
    }

    /************* util methods ****************/
    protected Optional<Account> findAccountById(UUID accountId) {
        return accountRepository.findById(accountId);
    }


    protected void updateAccount(Account account, BigDecimal newBalance) {
        account.setBalance(newBalance);
        accountRepository.save(account);
    }

    protected List<Account> getAllAccountByUser(){
        User user = userService.getUserByToken(AuthUtil.handleRequest());

        List<Account> accountList = accountRepository.findAllByUserId(user.getId());
        if(accountList.isEmpty()){
            throw new CustomException("Account not found Exception:[AccountService]:A_2");
        }
        return accountList;
    }



}
