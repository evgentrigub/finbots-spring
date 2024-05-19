package com.finbots.controllers;

import com.finbots.models.bot.Bot;
import com.finbots.models.bot.BotRequestDto;
import com.finbots.models.bot.BotResponseDto;
import com.finbots.services.BotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bots")
public class BotController {

    @Autowired
    BotService botService;

    @PostMapping
    public BotResponseDto create(@RequestBody BotRequestDto botRequestDto) {
        return botService.create(botRequestDto);
    }

    @GetMapping("/{id}")
    public Bot get(@PathVariable Long id) {
        return botService.get(id);
    }

    @PutMapping("/{id}")
    public BotResponseDto update(@PathVariable Long id, @RequestBody BotRequestDto botRequestDto) {
        return botService.update(id, botRequestDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        botService.delete(id);
    }
}