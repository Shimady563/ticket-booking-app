package com.shimady.ticketbookingapp.repository;


import com.shimady.ticketbookingapp.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends ListCrudRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query("select u " +
            "from User u " +
            "left join fetch u.bookings")
    List<User> findAllFetchBookings();
}
