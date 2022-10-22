package ru.practicum.shareit.item.model;

import lombok.*;
import javax.persistence.*;

import ru.practicum.shareit.user.model.User;

@Data
@Entity
@Table(name = "items")
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;
    @Column(name = "item_name")
    private String name;
    @Column(name = "item_description")
    private String description;
    @Column(name = "available")
    private Boolean available;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
}
