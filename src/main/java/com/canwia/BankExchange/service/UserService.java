package com.canwia.BankExchange.service;

import com.canwia.BankExchange.auth.AuthUtil;
import com.canwia.BankExchange.dto.UserDto;
import com.canwia.BankExchange.dto.converter.UserDtoConverter;
import com.canwia.BankExchange.model.User;
import com.canwia.BankExchange.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserDtoConverter userDtoConverter;

    protected User getUserByToken(String userName) {
        return userRepository.findByEmail(userName).orElseThrow(()->new UsernameNotFoundException("User can not found exception!"));
    }


    public UserDto findUser() {
        User user = getUserByToken(AuthUtil.handleRequest());
        return userDtoConverter.convertFrom(user);
    }


}
