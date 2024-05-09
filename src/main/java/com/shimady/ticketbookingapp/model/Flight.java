package com.shimady.ticketbookingapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "flight")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "departure_time")
    private LocalDateTime departureTime;

    @Column(name = "arrival_time")
    private LocalDateTime arrivalTime;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_airport_id")
    private Airport sourceAirport;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_airport_id")
    private Airport destinationAirport;

    @OneToMany(mappedBy = "flight", fetch = FetchType.LAZY)
    private Set<Seat> seats = new HashSet<>();

    public void addSeat(Seat seat) {
        seat.setFlight(this);
        seats.add(seat);
    }

    public Flight(
            LocalDateTime departureTime,
            LocalDateTime arrivalTime,
            Airport sourceAirport,
            Airport destinationAirport,
            Set<Seat> seats
    ) {
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.sourceAirport = sourceAirport;
        this.destinationAirport = destinationAirport;
        this.seats = seats;
    }
}
