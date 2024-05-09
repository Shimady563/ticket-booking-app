package com.shimady.ticketbookingapp.repository;

import com.shimady.ticketbookingapp.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    private Long userId;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    public void setUp() {
        User user = new User();
        entityManager.persist(user);
        entityManager.flush();
        userId = user.getId();
    }

    @Test
    public void testFindById() {
        Optional<User> userOptional = userRepository.findById(userId);

        assertThat(userOptional.isPresent()).isTrue();

        User user = userOptional.get();
        assertThat(user.getId()).isEqualTo(userId);
    }

    @Test
    public void testSave() {
        User user = new User();
        entityManager.persist(user);
        entityManager.flush();
        Long id = user.getId();

        Optional<User> userOptional = userRepository.findById(id);

        assertThat(userOptional.isPresent()).isTrue();
        assertThat(userOptional.get().getId()).isEqualTo(id);
    }
}
