package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface BookingService {
    Booking addNewBooking(Booking booking);

    Booking approvalBookingById(Long bookingId, Long ownerId, Boolean approved);

    Booking getBookingById(Long bookingId, Long userId);

    List<Booking> getAllBookingsForBooker(User booker, String state);

    List<Booking> getAllBookingsForOwner(User owner, String state);

    BookingDto getLastBookingForOwner(Item item, User owner, Long userId);

    BookingDto getNextBookingForOwner(Item item, User owner, Long userId);

    Boolean checkBookingByBooker(Item item, User booker);
}
