package ru.practicum.shareit.booking.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingStorage extends JpaRepository<Booking,Long> {

    Page<Booking> findAllByBookerOrderByStartDesc(
            User booker, Pageable pageable);

    List<Booking> findAllByBookerOrderByStartDesc(
            User booker);

    List<Booking> findAllByBookerAndStatusOrderByStartDesc(
            User booker,
            BookingStatus status);

    List<Booking> findAllByBookerAndStartAfterOrderByStartDesc(
            User booker,
            @Param("start") LocalDateTime searchDateTime);

    List<Booking> findAllByBookerAndEndBeforeOrderByStartDesc(
            User booker,
            @Param("end") LocalDateTime searchDateTime);

    List<Booking> findAllByBookerAndStartBeforeAndEndAfterOrderByStartDesc(
            User booker,
            @Param("start") LocalDateTime searchDateTime1,
            @Param("end") LocalDateTime searchDateTime2);

    Booking findTop1ByItemAndBookerAndStatusAndStartBefore(
            Item item,
            User booker,
            BookingStatus status,
            LocalDateTime start);

    List<Booking> findAllByItemOwnerOrderByStartDesc(
            User owner, Pageable pageable);

    List<Booking> findAllByItemOwnerAndStatusOrderByStartDesc(
            User owner,
            BookingStatus status);

    List<Booking> findAllByItemOwnerAndStartAfterOrderByStartDesc(
            User owner,
            @Param("start") LocalDateTime searchDateTime);

    List<Booking> findAllByItemOwnerAndEndBeforeOrderByStartDesc(
            User owner,
            @Param("end") LocalDateTime searchDateTime);

    List<Booking> findAllByItemOwnerAndStartBeforeAndEndAfterOrderByStartDesc(
            User owner,
            @Param("start") LocalDateTime searchDateTime1,
            @Param("end") LocalDateTime searchDateTime2);

    Booking findTopOneByItemAndItemOwnerAndStartAfterOrderByStartAsc(
            Item item,
            User owner,
            @Param("start") LocalDateTime searchDateTime);

    Booking findTopOneByItemAndItemOwnerAndEndBeforeOrderByEndDesc(
            Item item,
            User owner,
            @Param("end") LocalDateTime searchDateTime);
}

