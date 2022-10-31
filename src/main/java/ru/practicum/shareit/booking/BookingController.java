package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoItemOrUser;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;


@RequiredArgsConstructor
@Slf4j
@Validated
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final ItemService itemService;
    private final UserService userService;

    @PostMapping
    public BookingDtoItemOrUser addNewBooking(@RequestBody BookingDto bookingDto,
                                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        Item item = itemService.getItemById(bookingDto.getItemId());
        User booker = userService.getUserById(userId);
        bookingDto.setStatus(BookingStatus.WAITING);
        Booking booking = BookingMapper.toBooking(bookingDto, item, booker);
        BookingDtoItemOrUser bookingDtoItemOrUser =
               bookingService.addNewBooking(booking);
        return bookingDtoItemOrUser;
    }

    @PatchMapping(value = "/{bookingId}")
    public Booking approvalBookingById(@PathVariable Long bookingId,
                                       @RequestParam Boolean approved,
                                       @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.approvalBookingById(bookingId, userId, approved);
    }

    @GetMapping(value = "/{bookingId}")
    public BookingDtoItemOrUser getBookingById(@PathVariable Long bookingId,
                                               @RequestHeader("X-Sharer-User-Id") Long userId) {
        Booking booking = bookingService.getBookingById(bookingId, userId);
        BookingDtoItemOrUser bookingDtoItemOrUser =
                BookingMapper.toBookingDtoItemOrUser(booking);
        bookingDtoItemOrUser.setItem(ItemMapper.toItemDtoBooking(booking.getItem()));
        bookingDtoItemOrUser.setBooker(UserMapper.toUserDtoBooking(booking.getBooker()));
        return bookingDtoItemOrUser;
    }

    @GetMapping()
    public List<Booking> getAllBookingsForBooker(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @RequestParam(defaultValue = "ALL") String state,
                                                 @RequestParam(defaultValue = "1") @PositiveOrZero int from,
                                                 @RequestParam(defaultValue = "10") @Positive int size) {
        User booker = userService.getUserById(userId);
        return bookingService.getAllBookingsForBooker(booker, state, from, size);
    }

    @GetMapping(value = "/owner")
    public List<Booking> getAllBookingsForOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestParam(defaultValue = "ALL") String state,
                                                @RequestParam(defaultValue = "1") @PositiveOrZero int from,
                                                @RequestParam(defaultValue = "10") @Positive int size) {
        User owner = userService.getUserById(userId);
        return bookingService.getAllBookingsForOwner(owner, state, from, size);
    }
}
