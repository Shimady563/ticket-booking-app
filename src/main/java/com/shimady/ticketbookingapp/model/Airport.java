package com.shimady.ticketbookingapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "Airport")
public class Airport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @NaturalId
    private String name;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "code", nullable = false)
    @NaturalId
    private String code;

    @OneToMany(mappedBy = "sourceAirport")
    private Set<Flight> departingFlights;

    @OneToMany(mappedBy = "destinationAirport")
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

    @Override
    public boolean equals(Object o) {
        return o instanceof Airport a
                && a.getName().equals(name)
                && a.getCode().equals(code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, code);
    }

    @Override
    public String toString() {
        return "Airport{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
