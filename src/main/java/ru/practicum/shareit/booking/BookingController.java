package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoItemOrUser;
import ru.practicum.shareit.booking.maper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import java.util.List;


@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final ItemService itemService;
    private final UserService userService;

    @PostMapping
    public BookingDtoItemOrUser add(@RequestBody BookingDto bookingDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        Item item = itemService.getItemById(bookingDto.getItemId());
        User booker = userService.getUserById(userId);
        bookingDto.setStatus(BookingStatus.WAITING);
        Booking booking = BookingMapper.toBooking(bookingDto,item,booker);
        BookingDtoItemOrUser bookingDtoItemOrUser =
                BookingMapper.toBookingDtoItemOrUser(bookingService.addNewBooking(booking));
        bookingDtoItemOrUser.setItem(ItemMapper.toItemDtoBooking(item));
        bookingDtoItemOrUser.setBooker(UserMapper.toUserDtoBooking(booker));
        return bookingDtoItemOrUser;
    }

    @PatchMapping(value = "/{bookingId}")
    public Booking approvalBookingById(@PathVariable Long bookingId,
                                       @RequestParam Boolean approved,
                                       @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.approvalBookingById(bookingId,userId,approved);
    }

    @GetMapping(value = "/{bookingId}")
    public BookingDtoItemOrUser getBookingById(@PathVariable Long bookingId,
                                  @RequestHeader("X-Sharer-User-Id") Long userId) {
        Booking booking = bookingService.getBookingById(bookingId,userId);
        BookingDtoItemOrUser bookingDtoItemOrUser =
                BookingMapper.toBookingDtoItemOrUser(booking);
        bookingDtoItemOrUser.setItem(ItemMapper.toItemDtoBooking(booking.getItem()));
        bookingDtoItemOrUser.setBooker(UserMapper.toUserDtoBooking(booking.getBooker()));
        return bookingDtoItemOrUser;
    }

    @GetMapping(value = "")
    public List<Booking> getAllBookingsForBooker(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                      @RequestParam(defaultValue = "ALL") String state) {
        User booker = userService.getUserById(userId);
        return bookingService.getAllBookingsForBooker(booker,state);
    }

    @GetMapping(value = "/owner")
    public List<Booking> getAllBookingsForOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @RequestParam(defaultValue = "ALL") String state) {
        User owner = userService.getUserById(userId);
        return bookingService.getAllBookingsForOwner(owner,state);
    }
}
