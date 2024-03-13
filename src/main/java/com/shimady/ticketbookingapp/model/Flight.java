package com.shimady.ticketbookingapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "Flight")
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

    @Override
    public boolean equals(Object o) {
        return o instanceof Flight
                && ((Flight) o).getId().equals(id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Flight{" +
                "id=" + id +
                ", departureTime=" + departureTime +
                ", arrivalTime=" + arrivalTime +
                ", sourceAirport=" + sourceAirport +
                ", destinationAirport=" + destinationAirport +
                '}';
    }
}
