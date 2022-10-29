package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exeption.BadRequestException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.shareit.booking.model.BookingStatus.APPROVED;
import static ru.practicum.shareit.booking.model.BookingStatus.WAITING;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTest {

    @Autowired
    private BookingService bookingService;
    @Autowired
    private UserService userService;
    @Autowired
    private ItemService itemService;

    private final User user = new User();
    private final User user2 = new User();
    private final User anotherUser = new User();
    private final Item item1 = new Item();
    private final Item item2 = new Item();
    private final Booking booking1 = new Booking();
    private final Booking booking2 = new Booking();
    private final Booking booking3 = new Booking();
    private final Booking booking4 = new Booking();

    @BeforeEach
    public void reparationTest() {
        user.setName("user");
        user.setEmail("user@user.com");
        userService.addUser(user);

        user2.setName("user1");
        user2.setEmail("user1@user.com");
        userService.addUser(user2);

        anotherUser.setName("another");
        anotherUser.setEmail("another@user.com");
        userService.addUser(anotherUser);

        item1.setOwner(user);
        item1.setName("item1 item1");
        item1.setDescription("item1 description");
        item1.setAvailable(true);
        itemService.addItem(item1);

        item2.setOwner(user);
        item2.setName("item2 item2");
        item2.setDescription("item2 description");
        item2.setAvailable(true);
        itemService.addItem(item2);

        booking1.setItem(item1);
        booking1.setBooker(user2);
        booking1.setStart(LocalDateTime.of(2025, 1, 11, 11, 0));
        booking1.setEnd(LocalDateTime.of(2025, 2, 11, 11, 0));
        booking1.setStatus(WAITING);

        booking2.setItem(item2);
        booking2.setBooker(user2);
        booking2.setStart(LocalDateTime.of(2022, 1, 1, 10, 0));
        booking2.setEnd(LocalDateTime.of(2023, 1, 10, 10, 0));
        booking2.setStatus(WAITING);

        booking3.setItem(item2);
        booking3.setBooker(user2);
        booking3.setStart(LocalDateTime.of(2022, 1, 1, 10, 0));
        booking3.setEnd(LocalDateTime.of(2022, 1, 10, 10, 0));
        booking3.setStatus(APPROVED);

        booking4.setItem(item2);
        booking4.setBooker(user2);
        booking4.setStart(LocalDateTime.of(2023, 2, 1, 10, 0));
        booking4.setEnd(LocalDateTime.of(2023, 2, 10, 10, 0));
        booking4.setStatus(WAITING);
    }

    @Test
    @DirtiesContext
    @Transactional
    void addBookingTest() {
        assertThat(bookingService.getAllBookingsForOwner(user, "ALL", 0, 10)
                        .size(),
                equalTo(0));
        assertThat(bookingService.getAllBookingsForBooker(user2, "ALL", 0, 10)
                        .size(),
                equalTo(0));
        assertThat(bookingService.getAllBookingsForBooker(anotherUser, "ALL", 0, 10)
                        .size(),
                equalTo(0));
        bookingService.addNewBooking(booking1);
        assertThat(bookingService.getAllBookingsForOwner(user, "ALL", 0, 10)
                        .size(),
                equalTo(1));
        assertThat(bookingService.getAllBookingsForBooker(user2, "ALL", 0, 10)
                        .size(),
                equalTo(1));
        assertThat(bookingService.getAllBookingsForBooker(anotherUser, "ALL", 0, 10)
                        .size(),
                equalTo(0));
        assertThat(bookingService.getAllBookingsForOwner(anotherUser, "ALL", 0, 10)
                        .size(),
                equalTo(0));
        assertThrows(BadRequestException.class, () ->
                bookingService.getAllBookingsForBooker(user2, "TEST", 0, 10));
        assertThrows(Exception.class, () -> bookingService.getAllBookingsForOwner
                (anotherUser, "ALL11", 0, 10));

    }

    @Test
    @DirtiesContext
    @Transactional
    void approvalBookingByIdTest() {
        bookingService.addNewBooking(booking1);
        assertThat(bookingService.getAllBookingsForBooker(user2, "ALL", 0, 10)
                        .get(0)
                        .getStatus(),
                equalTo(WAITING));
        bookingService.approvalBookingById(1L, 1L, true);
        assertThrows(ResponseStatusException.class, () -> bookingService.approvalBookingById
                (1L, 100L, true));

        assertThrows(ResponseStatusException.class, () -> bookingService.approvalBookingById
                (1L, 1L, false));

        assertThat(bookingService.getAllBookingsForBooker(user2, "ALL", 0, 10)
                        .get(0)
                        .getStatus(),
                equalTo(APPROVED));
        assertThat(bookingService.getAllBookingsForBooker(user2, "FUTURE", 0, 10)
                        .size(),
                equalTo(1));
        assertThat(bookingService.getAllBookingsForBooker(user2, "REJECTED", 0, 10)
                        .size(),
                equalTo(0));
        assertThat(bookingService.getAllBookingsForBooker(user2, "WAITING", 0, 10)
                        .size(),
                equalTo(0));
        assertThat(bookingService.getAllBookingsForBooker(user2, "CURRENT", 0, 10)
                        .size(),
                equalTo(0));
        assertThat(bookingService.getAllBookingsForBooker(user2, "PAST", 0, 10)
                        .size(),
                equalTo(0));
    }

    @Test
    @DirtiesContext
    @Transactional
    void getBookingByIdTest() {
        bookingService.addNewBooking(booking1);
        assertThat(bookingService.getBookingById(1L, 1L)
                        .getId(),
                equalTo(1L));
        assertThrows(ResponseStatusException.class, () -> bookingService.getBookingById(2L, 1L));
    }

    @Test
    @DirtiesContext
    @Transactional
    void getLastBookingForOwnerTest() {
        bookingService.addNewBooking(booking1);
        bookingService.addNewBooking(booking4);
        assertThat(bookingService.getLastBookingForOwner(item1, user, 1L),
                nullValue());
    }

    @Test
    @DirtiesContext
    @Transactional
    void getNextBookingForOwnerTest() {
        bookingService.addNewBooking(booking1);
        bookingService.addNewBooking(booking4);
        assertThat(bookingService.getNextBookingForOwner(item1, user, 1L)
                        .getId(),
                equalTo(1L));
        assertThat(bookingService.getNextBookingForOwner(item1, user, 2L),
                nullValue());
        assertThat(bookingService.getAllBookingsForOwner(user, "ALL", 0, 10)
                        .size(),
                equalTo(2));
        assertThat(bookingService.getAllBookingsForOwner(user, "FUTURE", 0, 10)
                        .size(),
                equalTo(2));
        assertThat(bookingService.getAllBookingsForOwner(user, "WAITING", 0, 10)
                        .size(),
                equalTo(2));
        assertThat(bookingService.getAllBookingsForOwner(user, "REJECTED", 0, 10)
                        .size(),
                equalTo(0));
        assertThat(bookingService.getAllBookingsForOwner(user, "CURRENT", 0, 10)
                        .size(),
                equalTo(0));
        assertThat(bookingService.getAllBookingsForOwner(user, "PAST", 0, 10)
                        .size(),
                equalTo(0));
        assertThrows(Exception.class, () -> bookingService.getAllBookingsForOwner
                (user, "PAST11", 0, 10));
    }

    @Test
    @DirtiesContext
    @Transactional
    void checkBookingByBookerTest() {
        bookingService.addNewBooking(booking1);
        assertThat(bookingService.checkBookingByBooker(item1, user2),
                equalTo(false));
    }

    @Test
    @DirtiesContext
    void checkingConstructor() {
        Booking booking10 = new Booking(1L,
                LocalDateTime.of(2025, 1, 11, 11, 0),
                LocalDateTime.of(2025, 2, 11, 11, 0),
                item1,
                user2,
                WAITING);
        assertThat(bookingService.addNewBooking(booking10)
                        .getId(),
                equalTo(1L));
        assertThat(bookingService.addNewBooking(booking10)
                .getStatus(),
                equalTo(WAITING));
    }

    @Test
    @DirtiesContext
    void checkAddNewBooking() {
        Booking booking20 = new Booking(1L,
                LocalDateTime.of(2026, 1, 11, 11, 0),
                LocalDateTime.of(2025, 2, 11, 11, 0),
                item1,
                user2,
                WAITING);
        Booking booking30 = new Booking(1L,
                LocalDateTime.now(),
                LocalDateTime.of(2025, 2, 11, 11, 0),
                item1,
                user2,
                WAITING);
        Booking booking40 = new Booking(1L,
                LocalDateTime.of(2025, 1, 11, 11, 0),
                LocalDateTime.of(2025, 2, 11, 11, 0),
                item1,
                user,
                WAITING);
        assertThrows(ResponseStatusException.class, () -> bookingService.addNewBooking(booking20));
        assertThrows(ResponseStatusException.class, () -> bookingService.addNewBooking(booking30));
        assertThrows(ResponseStatusException.class, () -> bookingService.addNewBooking(booking40));
    }
}
