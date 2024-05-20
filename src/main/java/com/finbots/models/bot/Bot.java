package com.finbots.models.bot;

import com.finbots.models.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "bots")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String ticker;

    private String broker;
//    @ElementCollection
//    private List<String> operations;
    private LocalDateTime createdDate = LocalDateTime.now();
    private String status;
    private String strategy;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}