package ru.practicum.shareit.user.model;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.Email;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(name = "user_name")
    private String name;
    @Email
    @Column(name = "user_email")
    private String email;
}


