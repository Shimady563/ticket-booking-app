package com.shimady.ticketbookingapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "airport")
public class Airport {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "code", nullable = false)
    private String code;

    @OneToMany(mappedBy = "sourceAirport", fetch = FetchType.LAZY)
    private Set<Flight> departingFlights;

    @OneToMany(mappedBy = "destinationAirport", fetch = FetchType.LAZY)
    private Set<Flight> arrivingFlights;

    public void addDepartingFlight(Flight flight) {
        departingFlights.add(flight);
        flight.setSourceAirport(this);
    }

    public void removeDepartingFlight(Flight flight) {
        departingFlights.remove(flight);
        flight.setSourceAirport(null);
    }

    public void addArrivalFlight(Flight flight) {
        arrivingFlights.add(flight);
        flight.setDestinationAirport(this);
    }

    public void removeArrivalFlight(Flight flight) {
        arrivingFlights.remove(flight);
        flight.setDestinationAirport(null);
    }
}
