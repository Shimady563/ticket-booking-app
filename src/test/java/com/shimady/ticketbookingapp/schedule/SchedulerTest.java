package com.shimady.ticketbookingapp.schedule;

import com.shimady.ticketbookingapp.model.Booking;
import com.shimady.ticketbookingapp.model.User;
import com.shimady.ticketbookingapp.service.EmailService;
import com.shimady.ticketbookingapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class SchedulerTest {

    @Mock
    private UserService userService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private Scheduler scheduler;

    @Test
    public void testScheduleReminder() {
        User user1 = new User();
        User user2 = new User();
        Booking booking1 = new Booking();
        Booking booking2 = new Booking();
        user1.addBooking(booking1);
        user2.addBooking(booking2);

        given(userService.getAllUsers()).willReturn(List.of(user1, user2));

        scheduler.scheduleReminderEmail();

        then(emailService).should(times(2))
                .sendEmail(
                        any(),
                        eq("Bookings reminder"),
                        any()
                );
    }
}
