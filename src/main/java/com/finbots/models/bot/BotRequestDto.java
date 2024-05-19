package com.finbots.models.bot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BotRequestDto {
    private String ticker;
    private String strategy;
}