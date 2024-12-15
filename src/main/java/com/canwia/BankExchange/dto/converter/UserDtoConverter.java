package com.canwia.BankExchange.dto.converter;

import com.canwia.BankExchange.dto.UserDto;
import com.canwia.BankExchange.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserDtoConverter {

    public UserDto convertFrom(User user) {
        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setSurname(user.getSurname());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setCreationDate(user.getCreationDate());
        return userDto;
    }
}
