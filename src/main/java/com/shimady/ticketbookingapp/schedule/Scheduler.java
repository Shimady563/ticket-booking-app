package com.shimady.ticketbookingapp.schedule;

import com.shimady.ticketbookingapp.model.User;
import com.shimady.ticketbookingapp.service.EmailService;
import com.shimady.ticketbookingapp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class Scheduler {

    private final UserService userService;
    private final EmailService emailService;

    @Autowired
    public Scheduler(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @Async
    @Scheduled(cron = "${scheduling.cron}")
    public void scheduleReminderEmail() {
        log.info("Running scheduled booking reminder");
        List<User> users = userService.getAllUsers();

        for (User user : users) {
            if (!user.getBookings().isEmpty()) {
                String message = buildMessage(user);
                emailService.sendEmail(user.getEmail(), "Bookings reminder", message);
            }
        }
    }

    private static String buildMessage(User user) {
        return """
                Dear Customer,

                This is a reminder for your upcoming bookings.

                You have\s""" +
                user.getBookings().size() +
                """
                         bookings.

                        For more information visit http://localhost:8080/bookings.

                        """ +
                "Thank you for choosing our service.\n";
    }
}
