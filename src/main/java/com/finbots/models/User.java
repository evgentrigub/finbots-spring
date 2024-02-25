package com.finbots.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "users") // The table name is "users" by convention
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column()
    private String google;

    @Column(nullable = false)
    private String password;

    @Column()
    private String tinkoffToken;

    // Constructors, getters and setters
}

