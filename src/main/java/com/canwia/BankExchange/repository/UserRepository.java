package com.canwia.BankExchange.repository;

import com.canwia.BankExchange.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository <User, String> {

    Optional<User> findByEmail(String email);
}
