package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoItemOrUser;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.booking.model.BookingStatus.APPROVED;
import static ru.practicum.shareit.booking.model.BookingStatus.WAITING;


@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private BookingService bookingService;
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ItemService itemService;

    @MockBean
    private UserService userService;

    private final User user = new User();
    private final User user1 = new User();
    private final Item item1 = new Item();
    private final Item item2 = new Item();
    private final Booking booking1 = new Booking();
    private final Booking booking2 = new Booking();
    private BookingDto bookingDto1 = new BookingDto();
    private BookingDtoItemOrUser bookingDtoItemOrUser = new BookingDtoItemOrUser();

    @BeforeEach
    public void reparationTest() {

        user.setId(1L);
        user.setName("user");
        user.setEmail("user@user.com");

        user1.setId(2L);
        user1.setName("user1");
        user1.setEmail("user1@user.com");

        item1.setId(1L);
        item1.setOwner(user);
        item1.setName("item1 item1");
        item1.setDescription("item1 description");
        item1.setAvailable(true);

        item2.setId(2L);
        item2.setOwner(user);
        item2.setName("item2 item2");
        item2.setDescription("item2 description");
        item2.setAvailable(true);

        booking1.setId(1L);
        booking1.setItem(item1);
        booking1.setBooker(user1);
        booking1.setStart(LocalDateTime.of(2025,1,11,11,0));
        booking1.setEnd(LocalDateTime.of(2025,2,11,11,0));
        booking1.setStatus(WAITING);

        booking2.setId(1L);
        booking2.setItem(item1);
        booking2.setBooker(user1);
        booking1.setStart(LocalDateTime.of(2025,1,11,11,0));
        booking1.setEnd(LocalDateTime.of(2025,2,11,11,0));
        booking2.setStatus(APPROVED);

        bookingDto1 = BookingMapper.toBookingDto(booking1);
        bookingDtoItemOrUser = BookingMapper.toBookingDtoItemOrUser(booking1);
        bookingDtoItemOrUser.setItem(ItemMapper.toItemDtoBooking(booking1.getItem()));
        bookingDtoItemOrUser.setBooker(UserMapper.toUserDtoBooking(booking1.getBooker()));

    }

    @Test
    void addBookingTest() throws Exception {
        when(bookingService.addNewBooking(any()))
                .thenReturn(bookingDtoItemOrUser);
        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDtoItemOrUser))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id",1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("WAITING"))
                .andExpect(jsonPath("$.start").exists())
                .andExpect(jsonPath("$.end").exists())
                .andExpect(jsonPath("$.item.id").exists())
                .andExpect(jsonPath("$.booker.id").exists());
    }

    @Test
    void approvalBookingByIdTest() throws Exception {
        when(bookingService.approvalBookingById(1L,1L,true))
                .thenReturn(booking2);
        mvc.perform(patch("/bookings/1?approved=true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id",1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void getBookingByIdTest() throws Exception {
        when(bookingService.getBookingById(1L,1L))
                .thenReturn(booking1);
        mvc.perform(get("/bookings/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id",1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("WAITING"))
                .andExpect(jsonPath("$.start").exists())
                .andExpect(jsonPath("$.end").exists());
    }

    @Test
    void getAllBookingsForBookerTest() throws Exception {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking1);
        when(bookingService.getAllBookingsForBooker(user1,"ALL",0,10))
                .thenReturn(bookings);
        mvc.perform(get("/bookings?state=ALL")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id",1L))
                .andExpect(status().isOk());
    }

    @Test
    void getAllBookingsForOwnerTest() throws Exception {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking1);
        when(bookingService.getAllBookingsForOwner(user,"ALL",0,10))
                .thenReturn(bookings);
        mvc.perform(get("/bookings/owner?state=ALL")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id",1L))
                .andExpect(status().isOk());
    }

    @Test
    void checkingConstructor() throws Exception {
        BookingDto bookingDto = new BookingDto(

        );
    }
}
