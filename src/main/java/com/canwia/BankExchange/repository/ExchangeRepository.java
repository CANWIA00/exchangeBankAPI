package com.canwia.BankExchange.repository;

import com.canwia.BankExchange.model.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.UUID;

@Repository
public interface ExchangeRepository extends JpaRepository<Exchange,UUID> {

    List<Exchange> findAllByAccountId(UUID accountId);
}
