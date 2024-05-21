package com.finbots.controllers;

import com.finbots.models.bot.BotInfoDto;
import com.finbots.models.bot.BotRequestDto;
import com.finbots.models.bot.BotResponseDto;
import com.finbots.services.BotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bots")
public class BotController {

    @Autowired
    BotService botService;

    @PostMapping
    public BotResponseDto create(@AuthenticationPrincipal UserDetails userDetails, @RequestBody BotRequestDto botRequestDto) {
        return botService.create(userDetails, botRequestDto);
    }

    @GetMapping("/{ticker}")
    public BotInfoDto get(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String ticker) {
        return botService.get(userDetails, ticker);
    }

    @DeleteMapping("/{ticker}")
    public void delete(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String ticker) {
        botService.delete(userDetails, ticker);
    }

}