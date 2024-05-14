package com.shimady.ticketbookingapp.repository;

import com.shimady.ticketbookingapp.model.Booking;
import com.shimady.ticketbookingapp.model.Passenger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PassengerRepositoryTest {

    public final String firstName = "John";
    public final String lastName = "Doe";
    public final Long passportNumber = 123456789L;
    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    public void setUp() {
        Passenger passenger = new Passenger();
        passenger.setFirstName(firstName);
        passenger.setLastName(lastName);
        passenger.setPassportNumber(passportNumber);
        entityManager.persist(passenger);
        entityManager.flush();
    }

    @Test
    public void testFindByFirstNameAndLastNameAndPassportNumber() {
        Optional<Passenger> passengerOptional = passengerRepository.findByFirstNameAndLastNameAndPassportNumber(
                firstName,
                lastName,
                passportNumber
        );

        assertThat(passengerOptional.isPresent()).isTrue();
        Passenger passenger = passengerOptional.get();
        assertThat(passenger.getFirstName()).isEqualTo(firstName);
        assertThat(passenger.getLastName()).isEqualTo(lastName);
        assertThat(passenger.getPassportNumber()).isEqualTo(passportNumber);
    }

    @Test
    public void testSave() {
        Passenger passenger = new Passenger();
        passengerRepository.save(passenger);

        Passenger foundPassenger = entityManager.find(Passenger.class, passenger.getId());
        assertThat(foundPassenger).isEqualTo(passenger);
    }
}
