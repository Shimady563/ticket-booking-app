# Ticket Booking App

## Overview

A restfull web application of ticket booking service. 
The core features include:

- Account creation for faster ticket purchases,
- Search for one-way flight tickets,
- Search for two-way flight tickets,
- Ticket booking and reservation viewing,
- Email notifications about current reservations

Additional features include data validation, error handling.
Workflows on github are set so that 
tests are triggered on push and pull request to every branch
and app image is deployed to docker hub
after successful tests completion in main branch.

## Technologies used

- Application is based on Spring and Spring Boot
- For data access I use PostgreSql, Hibernate and Spring Data JPA
- For testing I use Spring Boot test options
- For security I use Spring Security
- For CI/CD I user Docker, GitHub Actions

## Future plans

After completing implementation of core futures,
I plan to create api documentation with swagger.
I will be adding more api description
as I implement more endpoints.

## Api description

### For all users

GET /tickets/one-way - Search for one-way flight tickets information

    request parameters - Inforamation about departure date, 
    departure and arrival airports 
    type of seat, number of passengers
    
    response body - Information about departure and arrival 
    time, airport and cities,
    seat type and id for potential booking,
    overall tickets price,
    estimated time for flight

GET /tickets/two-way - Search for two-way flight tickets information

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
    id (for further booking), serial number, 
    price and type of every seat
    booking assosiated with every seat
    to determin if the seat is already booked

POST /user/login - Login in your account

    request parameters - Username and password

    repsonse - Redirection to
    user info page
    or previous page

POST /user/signup - Create an account

    request parameters - Username, email, password

    repsonse - result message

### Only for authenticated users

GET /user - Get current user info

    no request parameters

    repsonse body - Current user info
    user id, username, email

PUT /user - Update current user

    request parameters - User id, new credentials

    repsonse body - Result message

GET /bookings - Get all bookings for current user

    request parameters - No request parameters

    repsonse body - Information about current user's bookings
    list of all seats that were booked,
    list of all passengers,
    creation date

POST /bookings/book - Create booking

    request parameters - Flight id and seat type

    repsonse body - Message about creation
    list of all seat ids,
    list of all passengers
