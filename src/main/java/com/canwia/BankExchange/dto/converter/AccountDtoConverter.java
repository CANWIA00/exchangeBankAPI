package com.canwia.BankExchange.dto.converter;

import com.canwia.BankExchange.dto.AccountDto;
import com.canwia.BankExchange.model.Account;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class AccountDtoConverter {



    public AccountDto convertFrom(Account account){
        return new AccountDto(
                account.getId(),
                account.getUser().getId(),
                account.getCurrency(),
                account.getCurrencyCode(),
                account.getBalance(),
                account.getCreatedAt()
        );
    }


    public List<AccountDto> convertFrom(List<Account> accounts){
        return accounts.stream().map(this::convertFrom).toList();
    }
}
