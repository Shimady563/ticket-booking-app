package com.shimady.ticketbookingapp.repository;

import com.shimady.ticketbookingapp.model.Booking;
import org.springframework.data.repository.ListCrudRepository;

public interface BookingRepository extends ListCrudRepository<Booking, Long> {
}
