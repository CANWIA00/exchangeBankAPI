package com.canwia.BankExchange.auth;

import com.canwia.BankExchange.auth.requests.LoginRequest;
import com.canwia.BankExchange.auth.requests.RegisterRequest;
import com.canwia.BankExchange.model.Role;
import com.canwia.BankExchange.model.User;
import com.canwia.BankExchange.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }


    public Token register(RegisterRequest registerRequest) {
        var user = User.builder()
                .name(registerRequest.getName())
                .surname(registerRequest.getSurname())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return Token.builder().token(jwtToken).build();
    }


    public Token login(LoginRequest loginRequest) {
        Authentication auth= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword()));
        var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(()->new UsernameNotFoundException("User not found!"));
        if(auth.isAuthenticated()){
            String jwtToken = jwtService.generateToken(user);
            return Token.builder().token(jwtToken).build();
        }else {
            throw new UsernameNotFoundException("invalid email " + user);
        }
    }
}
