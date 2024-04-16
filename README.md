# Ticket Booking App

## Overview

A restfull web application of ticket booking service. The core features include:
- Account creation for faster ticket purchases,
- Search for one-way flight tickets,
- Search for two-way flight tickets,
- Ticket booking and prepayment option

## Api description

GET /search/one-way - Search for one-way flight tickets information

    request parameters - Inforamation about departure date, 
    departure and arrival airports 
    type of seat, number of passengers
    
    response body - Information about departure and arrival 
    time, airport and cities,
    seat type and id for potential booking,
    overall tickets price,
    estimated time for flight

GET /search/two-way - Search for two-way flight tickets information

    request parameters - Inforamation about departure date,
    departure date for return flight
    departure and arrival airports
    type of seat, number of passengers
    
    response body - Information about departure and arrival 
    time, airport and cities for both flights,
    seat type and id for potential booking,
    overall tickets price,
    estimated time for flight

GET /seats - Get all seats with concrete type for given flight

    request parameters - Flight id and seat type

    repsonse body - Information about seats
    id (for further booking), serial number, price and type of every seat
    booking assosiated with every seat
    to determin if the seat is already booked

## Technologies used

- Application is based on Spring and Spring Boot
- For data access I use PostgreSql, Hibernate and Spring Data JPA
- For testing I use Spring's test options

## Future plans

After completing implementation of core futures, 
I plan to create api documentation with swagger.
I will be adding more api description
as I implement more endpoints.
I also plan to add features 
to search and book tickets for hotels
in addition to flight tickets booking.