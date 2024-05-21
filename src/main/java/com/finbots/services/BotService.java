package com.finbots.services;

import com.finbots.models.bot.Bot;
import com.finbots.models.bot.BotInfoDto;
import com.finbots.models.bot.BotRequestDto;
import com.finbots.models.bot.BotResponseDto;
import com.finbots.models.user.User;
import com.finbots.repositories.BotRepository;
import com.finbots.security.exceptions.BadRequestException;
import com.finbots.security.exceptions.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class BotService {

    @Autowired
    BotRepository botRepository;

    @Autowired
    UserService userService;

    public BotResponseDto create(UserDetails userDetails, BotRequestDto botRequestDto) {
        User user = userService.getUserByEmail(userDetails.getUsername());

        botRepository.findByTicker(botRequestDto.getTicker()).ifPresent(b -> {
            throw new BadRequestException("Bot with this ticker already exists");
        });

        Bot bot = new Bot();
        bot.setTicker(botRequestDto.getTicker());
        bot.setStrategy(botRequestDto.getStrategy());
        bot.setBroker("default");
        bot.setStatus("active");
        bot.setUser(user);


        try {
            botRepository.save(bot);
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating bot");
        }

        return new BotResponseDto(bot.getTicker());
    }

    @Transactional
    public BotInfoDto get(UserDetails userDetails, String ticker) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        return user.getBots().stream()
                .filter(b -> b.getTicker().equals(ticker))
                .findFirst()
                .map(BotInfoDto::new)
                .orElseThrow(() -> new NotFoundException("Bot not found"));
    }

    @Transactional
    public void delete(UserDetails userDetails, String ticker) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        String botId = user.getBots().stream()
                .filter(b -> b.getTicker().equals(ticker))
                .findFirst()
                .map(Bot::getId)
                .orElseThrow(() -> new NotFoundException("Bot not found"));

        botRepository.deleteById(botId);
    }
}