package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemStorage extends JpaRepository<Item, Long> {

    List<Item> findAllByOwnerOrderByIdAsc(User owner);
}
