package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoItemOrUser;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        if (booking != null) {
            BookingDto bookingDto = new BookingDto();
            bookingDto.setId(booking.getId());
            bookingDto.setStart(booking.getStart());
            bookingDto.setEnd(booking.getEnd());
            bookingDto.setStatus(booking.getStatus());
            bookingDto.setItemId(booking.getItem().getId());
            bookingDto.setBookerId(booking.getBooker().getId());
            return bookingDto;
        } else {
            return null;
        }
    }

    public static Booking toBooking(BookingDto bookingDto, Item item, User booker) {
        Booking booking = new Booking();
        booking.setId(bookingDto.getId());
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setStatus(bookingDto.getStatus());
        booking.setItem(item);
        booking.setBooker(booker);
        return booking;
    }

    public static BookingDtoItemOrUser toBookingDtoItemOrUser(Booking booking) {
        BookingDtoItemOrUser bookingDtoItemOrUser = new BookingDtoItemOrUser();
        bookingDtoItemOrUser.setId(booking.getId());
        bookingDtoItemOrUser.setStart(booking.getStart());
        bookingDtoItemOrUser.setEnd(booking.getEnd());
        bookingDtoItemOrUser.setStatus(booking.getStatus());
        return bookingDtoItemOrUser;
    }
}
