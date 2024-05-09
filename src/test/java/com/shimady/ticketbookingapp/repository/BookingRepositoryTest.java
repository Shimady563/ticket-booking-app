package com.shimady.ticketbookingapp.repository;

import com.shimady.ticketbookingapp.model.Booking;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testSave() {
        Booking booking = new Booking();
        bookingRepository.save(booking);

        Booking foundBooking = entityManager.find(Booking.class, booking.getId());
        assertThat(foundBooking).isEqualTo(booking);
    }
}
