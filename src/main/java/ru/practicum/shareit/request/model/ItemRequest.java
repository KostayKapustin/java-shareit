package ru.practicum.shareit.request.model;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "requests")
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    Long id;

    @Column(name = "request_description")
    String description;

    @ManyToOne
    @JoinColumn(name = "requestor_id")
    User request;

    @Column(name = "request_created")
    LocalDateTime created;

    public ItemRequest(String description, User request, LocalDateTime created) {
        this.description = description;
        this.request = request;
        this.created = created;
    }

    public ItemRequest() {

    }
}
