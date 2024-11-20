package com.canwia.BankExchange.service;

import com.canwia.BankExchange.dto.AccountDto;
import com.canwia.BankExchange.dto.converter.AccountDtoConverter;
import com.canwia.BankExchange.dto.requests.CreateAccountRequest;
import com.canwia.BankExchange.exception.CustomException;
import com.canwia.BankExchange.model.Account;
import com.canwia.BankExchange.model.Currency;
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

    public AccountService(AccountRepository accountRepository, AccountDtoConverter accountDtoConverter, CurrencyService currencyService) {
        this.accountRepository = accountRepository;
        this.accountDtoConverter = accountDtoConverter;
        this.currencyService = currencyService;
    }

    public AccountDto createAccount(CreateAccountRequest createAccountRequest) {

        Currency currency = currencyService.findCurrencyById(createAccountRequest.getCurrencyCode());
        //TODO bind the userId
        Account account = new Account();
        account.setUserId(UUID.randomUUID());
        account.setBalance(BigDecimal.valueOf(0));
        account.setCurrency(currency.getName());
        account.setCurrencyCode(currency.getCode());
        account.setCreatedAt(LocalDateTime.now());

        accountRepository.save(account);

        return accountDtoConverter.convertFrom(account);
    }

    public AccountDto getAccountById(String id) {
        Account account= accountRepository.findById(fromString(id)).orElseThrow(()->new CustomException("Account not found Exception:[AccountService]:A_1"));
        return accountDtoConverter.convertFrom(account);
    }


    public List<AccountDto> getAllAccount(String userId) {
        List<Account> accountList = accountRepository.findAllByUserId(fromString(userId));
        if(accountList.isEmpty()){
            throw new CustomException("Account not found Exception:[AccountService]:A_2");
        }

        return accountDtoConverter.convertFrom(accountList);
    }

    public String deleteAccountById(String id) {
        Optional<Account> account = findAccountById(fromString(id));

        if(account.isPresent()){
            accountRepository.deleteById(UUID.fromString(id));
            return "Account deleted successfully";
        }else{
            throw new CustomException("Account not found Exception:[AccountService]:A_3");
        }


    }

    /************* util methods ****************/
    protected Optional<Account> findAccountById(UUID accountId) {
        Optional<Account> account = accountRepository.findById(accountId);
        return account;
    }


    protected void updateAccount(Account account, BigDecimal newBalance) {
        account.setBalance(newBalance);
        accountRepository.save(account);
    }

}
