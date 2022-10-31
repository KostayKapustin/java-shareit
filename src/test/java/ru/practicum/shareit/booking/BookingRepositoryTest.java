package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.practicum.shareit.booking.model.BookingStatus.APPROVED;
import static ru.practicum.shareit.booking.model.BookingStatus.WAITING;

@DataJpaTest
public class BookingRepositoryTest {
    @Autowired
    private BookingStorage bookingStorage;
    @Autowired
    private ItemStorage itemStorage;
    @Autowired
    private UserStorage userStorage;

    private final User user = new User();
    private final User user2 = new User();
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
        userStorage.save(user);

        user2.setName("user2");
        user2.setEmail("user2@user.com");
        userStorage.save(user2);

        item1.setOwner(user);
        item1.setName("item1 item1");
        item1.setDescription("item1 description");
        item1.setAvailable(true);
        itemStorage.save(item1);

        item2.setOwner(user2);
        item2.setName("item2 item2");
        item2.setDescription("item2 description");
        item2.setAvailable(true);
        itemStorage.save(item2);

        booking1.setItem(item1);
        booking1.setBooker(user2);
        booking1.setStart(LocalDateTime.of(2025,1,11,11,0));
        booking1.setEnd(LocalDateTime.of(2025,2,11,11,0));
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
    void findAllByBookerOrderByStartDescTest() {
        assertThat(bookingStorage.findAll().size(), equalTo(0));
        bookingStorage.save(booking1);
        assertThat(bookingStorage.findAllByBookerOrderByStartDesc(user2)
                .size(), equalTo(1));
        assertThat(bookingStorage.findAllByBookerOrderByStartDesc(user2)
                .get(0).getItem().getName(), equalTo("item1 item1"));
    }

    @Test
    @DirtiesContext
    void findAllByBookerAndStatusOrderByStartDescTest() {
        assertThat(bookingStorage
                        .findAll()
                        .size(),
                equalTo(0));
        bookingStorage.save(booking1);
        assertThat(bookingStorage
                        .findAllByBookerAndStatusOrderByStartDesc(user2, WAITING)
                        .size(),
                equalTo(1));
        assertThat(bookingStorage
                        .findAllByBookerAndStatusOrderByStartDesc(user2, WAITING)
                        .get(0).getItem().getName(),
                equalTo("item1 item1"));
    }

    @Test
    @DirtiesContext
    void findAllByBookerAndStartAfterOrderByStartDescTest() {
        bookingStorage.save(booking1);
        assertThat(bookingStorage
                        .findAllByBookerAndStartAfterOrderByStartDesc(user2, LocalDateTime.now())
                        .get(0).getItem().getName(),
                equalTo("item1 item1"));
    }

    @Test
    @DirtiesContext
    void findAllByBookerAndEndBeforeOrderByStartDescTest() {
        bookingStorage.save(booking1);
        assertThat(bookingStorage
                        .findAllByBookerAndEndBeforeOrderByStartDesc(user2, LocalDateTime.now())
                        .size(),
                equalTo(0));
        assertThat(bookingStorage
                        .findAllByBookerAndEndBeforeOrderByStartDesc(user2,
                                LocalDateTime.of(2024, 1, 10, 10, 0))
                        .size(),
                equalTo(0));
    }

    @Test
    @DirtiesContext
    void findAllByBookerAndStartBeforeAndEndAfterOrderByStartDescTest() {
        bookingStorage.save(booking1);
        bookingStorage.save(booking2);
        assertThat(bookingStorage
                        .findAllByBookerAndStartBeforeAndEndAfterOrderByStartDesc(user2,
                                LocalDateTime.now(),
                                LocalDateTime.of(2023, 1, 10, 10, 0))
                        .size(),
                equalTo(0));
        assertThat(bookingStorage
                        .findAllByBookerAndStartBeforeAndEndAfterOrderByStartDesc(user2,
                                LocalDateTime.now(), LocalDateTime.now())
                        .size(),
                equalTo(1));

    }

    @Test
    @DirtiesContext
    void findTop1ByItemAndBookerAndStatusAndStartBeforeTest() {
        bookingStorage.save(booking1);
        bookingStorage.save(booking4);
        assertThat(bookingStorage.findAll().size(), equalTo(2));
        assertThat(bookingStorage
                        .findTop1ByItemAndBookerAndStatusAndStartBefore(item2,
                                user2,
                                APPROVED,
                                LocalDateTime.now()),
                nullValue());
    }

    @Test
    @DirtiesContext
    void findAllByItemOwnerOrderByStartDescTest() {
        bookingStorage.save(booking1);
        bookingStorage.save(booking4);
        assertThat(bookingStorage.findAllByItemOwnerOrderByStartDesc(user, PageRequest.of(0, 10)),
                notNullValue());
    }

    @Test
    @DirtiesContext
    void findAllByItemOwnerAndStatusOrderByStartDescTest() {
        bookingStorage.save(booking1);
        bookingStorage.save(booking4);
        assertThat(bookingStorage.findAllByItemOwnerAndStatusOrderByStartDesc(user, WAITING),
                notNullValue());
    }

    @Test
    @DirtiesContext
    void findAllByItemOwnerAndStartAfterOrderByStartDescTest() {
        bookingStorage.save(booking1);
        bookingStorage.save(booking4);
        assertThat(bookingStorage.findAllByItemOwnerAndStartAfterOrderByStartDesc(user, LocalDateTime.now()),
                notNullValue());
    }

    @Test
    @DirtiesContext
    void findAllByItemOwnerAndEndBeforeOrderByStartDescTest() {
        bookingStorage.save(booking1);
        bookingStorage.save(booking4);
        assertThat(bookingStorage.findAllByItemOwnerAndEndBeforeOrderByStartDesc(user, LocalDateTime.now()),
                notNullValue());
    }

    @Test
    @DirtiesContext
    void findAllByItemOwnerAndStartBeforeAndEndAfterOrderByStartDescTest() {
        bookingStorage.save(booking1);
        bookingStorage.save(booking4);
        assertThat(bookingStorage
                        .findAllByItemOwnerAndStartBeforeAndEndAfterOrderByStartDesc(user,
                                LocalDateTime.now(), LocalDateTime.now()),
                notNullValue());
    }

    @Test
    @DirtiesContext
    void findTop1ByItemAndItemOwnerAndStartAfterOrderByStartAscTest() {
        bookingStorage.save(booking1);
        bookingStorage.save(booking4);
        assertThat(bookingStorage
                        .findTopOneByItemAndItemOwnerAndStartAfterOrderByStartAsc(item1, user,
                                LocalDateTime.now()),
                notNullValue());
    }

    @Test
    @DirtiesContext
    void findTop1ByItemAndItemOwnerAndEndBeforeOrderByEndDescTest() {
        bookingStorage.save(booking1);
        bookingStorage.save(booking4);
        assertThat(bookingStorage
                        .findTopOneByItemAndItemOwnerAndStartAfterOrderByStartAsc(item1, user,
                                LocalDateTime.now()),
                notNullValue());
    }
}
