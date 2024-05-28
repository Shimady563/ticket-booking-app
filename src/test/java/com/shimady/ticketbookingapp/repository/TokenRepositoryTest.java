package com.shimady.ticketbookingapp.repository;

import com.shimady.ticketbookingapp.model.Token;
import com.shimady.ticketbookingapp.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TokenRepositoryTest {

    private Long tokenId;
    private final String tokenBody = "token";

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    public void setUp() {
        User user = new User();
        Token token1 = new Token(tokenBody, user);

        entityManager.persist(token1);
        entityManager.persist(user);

        entityManager.flush();

        tokenId = token1.getId();
    }

    @Test
    public void testFindByToken() {
        Optional<Token> tokenOptional = tokenRepository.findByToken(tokenBody);

        assertThat(tokenOptional.isPresent()).isTrue();

        Token token = tokenOptional.get();

        assertThat(token.getToken()).isEqualTo(tokenBody);
    }

    @Test
    public void testSave() {
        Token token = new Token();
        tokenRepository.save(token);

        Token token1 = entityManager.find(Token.class, token.getId());
        assertThat(token1).isEqualTo(token);
    }

    @Test
    public void testDelete() {
        tokenRepository.deleteById(tokenId);

        Optional<Token> tokenOptional = tokenRepository.findById(tokenId);

        assertThat(tokenOptional.isPresent()).isFalse();
    }
}
