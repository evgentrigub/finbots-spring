package com.finbots.models.user;

import com.finbots.models.bot.Bot;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(255)")
    private String email;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String password;

    @Column(columnDefinition = "VARCHAR(255)")
    private String tinkoffToken;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bot> bots;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}

