package com.shimady.ticketbookingapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "creation_time")
    private LocalDateTime creationTime = LocalDateTime.now();

    @OneToMany(mappedBy = "booking", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Seat> seats;

    @ManyToMany(mappedBy = "bookings", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Passenger> passengers;

    public void setPassengers(Set<Passenger> passengers) {
        passengers.forEach((passenger) -> passenger.addBooking(this));
        this.passengers = passengers;
    }

    public void setSeats(Set<Seat> seats) {
        seats.forEach(seat -> seat.setBooking(this));
        this.seats = seats;
    }
}
