package com.finbots.models.bot;

import com.finbots.models.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bots")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String broker;
    @ElementCollection
    private List<String> operations;
    private LocalDateTime createdDate;
    private String status;
    private String strategy;
    private String ticker;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // getters and setters
    public Bot(String broker, String status, String strategy, String ticker, User user) {
        this.ticker = ticker;
        this.broker = broker;
        this.strategy = strategy;
        this.status = status;
        this.user = user;
        this.createdDate = LocalDateTime.now();
    }
}