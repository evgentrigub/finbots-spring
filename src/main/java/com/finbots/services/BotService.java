package com.finbots.services;

import com.finbots.models.bot.Bot;
import com.finbots.models.bot.BotRequestDto;
import com.finbots.models.bot.BotResponseDto;
import com.finbots.repositories.BotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BotService {

    @Autowired
    BotRepository botRepository;

    public BotResponseDto create(BotRequestDto botRequestDto) {
        // implementation
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public Bot get(Long id) {
        // implementation
        throw new UnsupportedOperationException("Not implemented yet");

    }

    public BotResponseDto update(Long id, BotRequestDto botRequestDto) {
        // implementation
        throw new UnsupportedOperationException("Not implemented yet");

    }

    public void delete(Long id) {
        // implementation
        throw new UnsupportedOperationException("Not implemented yet");

    }
}