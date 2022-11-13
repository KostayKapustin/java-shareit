package ru.practicum.shareit.booking.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoItemOrUser;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface BookingService {
    BookingDtoItemOrUser addNewBooking(Booking booking);

    Booking approvalBookingById(Long bookingId, Long ownerId, Boolean approved);

    Booking getBookingById(Long bookingId, Long userId);

    @Transactional(readOnly = true)
    List<Booking> getAllBookingsForBooker(User booker, String state, int from, int size);

    @Transactional(readOnly = true)
    List<Booking> getAllBookingsForOwner(User owner, String state, int from, int size);

    BookingDto getLastBookingForOwner(Item item, User owner, Long userId);

    BookingDto getNextBookingForOwner(Item item, User owner, Long userId);

    Boolean checkBookingByBooker(Item item, User booker);
}
