package com.finbots.repositories;

import com.finbots.models.bot.Bot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BotRepository extends JpaRepository<Bot, Long> {
    Optional<Bot> findByTicker(String ticker);
}