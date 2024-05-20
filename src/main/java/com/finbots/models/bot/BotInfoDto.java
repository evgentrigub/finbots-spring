package com.finbots.models.bot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BotInfoDto {
    private String broker;
    private String status;
    private String strategy;
    private String ticker;
    private LocalDateTime createdDate;

    public BotInfoDto(Bot bot) {
        this.broker = bot.getBroker();
        this.status = bot.getStatus();
        this.strategy = bot.getStrategy();
        this.ticker = bot.getTicker();
        this.createdDate = bot.getCreatedDate();
    }
}