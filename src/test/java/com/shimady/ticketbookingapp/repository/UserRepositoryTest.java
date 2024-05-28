package com.shimady.ticketbookingapp.repository;

import com.shimady.ticketbookingapp.model.Booking;
import com.shimady.ticketbookingapp.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    private Long userId;
    private final String username = "username";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    public void setUp() {
        User user1 = new User();
        User user2 = new User();
        Booking booking = new Booking();
        user1.setUsername(username);
        user1.addBooking(booking);

        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(booking);

        entityManager.flush();

        userId = user1.getId();
    }

    @Test
    public void testFindById() {
        Optional<User> userOptional = userRepository.findById(userId);

        assertThat(userOptional.isPresent()).isTrue();

        User user = userOptional.get();
        assertThat(user.getId()).isEqualTo(userId);
    }

    @Test
    public void testFindByUsername() {
        Optional<User> userOptional = userRepository.findByUsername(username);

        assertThat(userOptional.isPresent()).isTrue();

        User user = userOptional.get();
        assertThat(user.getUsername()).isEqualTo(username);
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

    @Test
    public void testFindALlFetchBookings() {
        List<User> users = userRepository.findAllFetchBookings();

        assertThat(users).hasSize(2);

        User user = users.get(0);

        assertThat(user.getId()).isEqualTo(userId);
        assertThat(user.getBookings()).hasSize(1);
    }
}
