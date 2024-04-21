package com.shimady.ticketbookingapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Setter
    @OneToMany(mappedBy = "booking", fetch = FetchType.LAZY)
    private List<Seat> seats;

    @Setter
    @OneToMany(mappedBy = "booking", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Passenger> passengers = new ArrayList<>();

    public void addAllPassengers(List<Passenger> passengers) {
        passengers.forEach((passenger) -> passenger.setBooking(this));
        this.passengers.addAll(passengers);
    }
}
