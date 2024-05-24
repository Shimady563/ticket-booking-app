package com.shimady.ticketbookingapp.repository;

import com.shimady.ticketbookingapp.model.Token;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokenRepository extends CrudRepository<Token, Long> {

    Optional<Token> findByToken(String token);
}
