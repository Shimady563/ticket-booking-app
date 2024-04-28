package com.shimady.ticketbookingapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@Getter
@Entity
@Table(
        name = "passenger",
        uniqueConstraints = @UniqueConstraint(
                name = "passenger_unique_fn_ln_pn",
                columnNames = {"first_name, last_name, passport_number"}
        )
)
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "citizenship")
    private String citizenship;

    @Column(name = "passport_number")
    private Long passportNumber;

    @Column(name = "passport_expiry_date")
    private LocalDate passportExpiryDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Passenger passenger)) return false;
        return Objects.equals(firstName, passenger.firstName)
                && Objects.equals(lastName, passenger.lastName)
                && Objects.equals(birthDate, passenger.birthDate)
                && Objects.equals(citizenship, passenger.citizenship)
                && Objects.equals(passportNumber, passenger.passportNumber)
                && Objects.equals(passportExpiryDate, passenger.passportExpiryDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, birthDate, citizenship, passportNumber, passportExpiryDate);
    }
}
