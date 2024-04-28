package com.shimady.ticketbookingapp.repository;

import com.shimady.ticketbookingapp.model.Passenger;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface PassengerRepository extends ListCrudRepository<Passenger, Long> {

    Optional<Passenger> findByFirstNameAndLastNameAndPassportNumber(String firstName, String lastName, Long passportNumber);
}
