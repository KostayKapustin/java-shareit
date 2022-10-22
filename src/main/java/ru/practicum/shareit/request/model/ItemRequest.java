package ru.practicum.shareit.request.model;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "requests")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private long id;
    @Column(name = "request_description")
    private String description;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User requestor;
    @Column(name = "request_created")
    private LocalDate created;
}
