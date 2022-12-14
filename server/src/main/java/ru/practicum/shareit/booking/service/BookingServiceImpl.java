package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoItemOrUser;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exeption.BadRequestException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingStorage bookingStorage;

    @Override
    @Transactional
    public BookingDtoItemOrUser addNewBooking(Booking booking) {
        checkAddNewBooking(booking);
        bookingStorage.save(booking);
        BookingDtoItemOrUser bookingDtoItemOrUser =
                BookingMapper.toBookingDtoItemOrUser(booking);
        bookingDtoItemOrUser.setItem(ItemMapper.toItemDtoBooking(booking.getItem()));
        bookingDtoItemOrUser.setBooker(UserMapper.toUserDtoBooking(booking.getBooker()));
        return bookingDtoItemOrUser;
    }

    @Override
    @Transactional
    public Booking approvalBookingById(Long bookingId, Long ownerId, Boolean approved) {
        Booking booking = bookingStorage.findById(bookingId).get();
        if (!booking.getItem().getOwner().getId().equals(ownerId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND);
        }
        if (booking.getStatus().equals(BookingStatus.APPROVED)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST);
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        bookingStorage.save(booking);
        return booking;
    }

    @Override
    @Transactional(readOnly = true)
    public Booking getBookingById(Long bookingId, Long userId) {
        try {
            Booking booking = bookingStorage.findById(bookingId).get();
            Long booker = booking.getBooker().getId();
            Long owner = booking.getItem().getOwner().getId();
            if (!userId.equals(booker) && !userId.equals(owner)) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND);
            }
            return booking;
        } catch (NoSuchElementException exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> getAllBookingsForBooker(User booker, String state, int from, int size) {
        try {
            BookingState stateEnum = BookingState.valueOf(state);
            switch (stateEnum) {
                case ALL:
                    return bookingStorage
                            .findAllByBookerOrderByStartDesc(booker,
                                    PageRequest.of(from / size,size)).getContent();
                case FUTURE:
                    return bookingStorage
                            .findAllByBookerAndStartAfterOrderByStartDesc(booker,LocalDateTime.now());
                case WAITING:
                case REJECTED:
                    return bookingStorage
                            .findAllByBookerAndStatusOrderByStartDesc(booker,BookingStatus.valueOf(state));
                case CURRENT:
                    return bookingStorage
                            .findAllByBookerAndStartBeforeAndEndAfterOrderByStartDesc(booker,
                                    LocalDateTime.now(),
                                    LocalDateTime.now());
                case PAST:
                    return bookingStorage.findAllByBookerAndEndBeforeOrderByStartDesc(booker,LocalDateTime.now());
            }
        } catch (Exception e) {
            throw new BadRequestException(String.format("{\"error\": \"Unknown state: %s\" }", state));
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> getAllBookingsForOwner(User owner, String state, int from, int size) {
        try {
            BookingState stateEnum = BookingState.valueOf(state);
            switch (stateEnum) {
                case ALL:
                    return bookingStorage
                            .findAllByItemOwnerOrderByStartDesc(owner,
                                    PageRequest.of(from / size, size));
                case FUTURE:
                    return bookingStorage
                            .findAllByItemOwnerAndStartAfterOrderByStartDesc(owner, LocalDateTime.now());
                case WAITING:
                case REJECTED:
                    return bookingStorage
                            .findAllByItemOwnerAndStatusOrderByStartDesc(owner, BookingStatus.valueOf(state));
                case CURRENT:
                    return bookingStorage
                            .findAllByItemOwnerAndStartBeforeAndEndAfterOrderByStartDesc(owner,
                                    LocalDateTime.now(),
                                    LocalDateTime.now());
                case PAST:
                    return bookingStorage.findAllByItemOwnerAndEndBeforeOrderByStartDesc(owner, LocalDateTime.now());
            }
        } catch (Exception e) {
            throw new BadRequestException(String.format("{\"error\": \"Unknown state: %s\" }", state));
        }
        return null;
    }

    @Override
    public BookingDto getLastBookingForOwner(Item item, User owner, Long userId) {
        if (owner.getId().equals(userId)) {
            return BookingMapper.toBookingDto(bookingStorage
                    .findTopOneByItemAndItemOwnerAndEndBeforeOrderByEndDesc(item, owner, LocalDateTime.now()));
        } else {
            return null;
        }
    }

    @Override
    public BookingDto getNextBookingForOwner(Item item, User owner, Long userId) {
        if (owner.getId().equals(userId)) {
            return BookingMapper.toBookingDto(bookingStorage
                    .findTopOneByItemAndItemOwnerAndStartAfterOrderByStartAsc(item, owner, LocalDateTime.now()));
        } else {
            return null;
        }
    }

    @Override
    public Boolean checkBookingByBooker(Item item, User booker) {
        return bookingStorage.findTop1ByItemAndBookerAndStatusAndStartBefore(item,
                booker, BookingStatus.APPROVED, LocalDateTime.now()) != null;
    }

    public void checkAddNewBooking(Booking booking) {
        if (!booking.getItem().getAvailable()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "???????????????? ???? ????, ?????? item ???????????????? ?????? ????????????????????????, ???? ????????????");
        }

        if (booking.getStart().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "???????????????? ???? ????, ?????? ?????????? ???????????? ?? ??????????????, ???? ????????????!");
        }

        if (booking.getStart().isAfter(booking.getEnd())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "???????????????? ???? ????, ?????? ?????????? ???????????? ???????????? ?????????????? ????????????????????, ???? ????????????!");
        }

        if (booking.getBooker() == booking.getItem().getOwner()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "???????????????? ???? ????, ?????? ???????????? ???? ???? ??????????????????, ???? ????????????!");
        }
    }
}
