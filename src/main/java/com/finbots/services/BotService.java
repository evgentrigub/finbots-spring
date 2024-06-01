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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
public class BotService {

    @Autowired
    BotRepository botRepository;

    @Autowired
    UserService userService;

    @Autowired
    JobServiceClient jobServiceClient;

    public BotResponseDto createCronJobDemo(String param) {
        try {
            var status = this.jobServiceClient.createJob(param);
            return new BotResponseDto(param, status);
        }     catch (Exception e) {
            log.error("Error creating cron job: {}", e.getMessage());
            return new BotResponseDto(param, "error");
        }
    }

    @Transactional
    public BotResponseDto create(UserDetails userDetails, BotRequestDto botRequestDto) {
        User user = userService.getUserByEmail(userDetails.getUsername());

        botRepository.findByTicker(botRequestDto.getTicker()).ifPresent(b -> {
            log.error("Bot with this ticker already exists: {}", botRequestDto.getTicker());
            throw new BadRequestException("Bot with this ticker already exists");
        });

        Bot bot = Bot.builder()
                .ticker(botRequestDto.getTicker())
                .strategy(botRequestDto.getStrategy())
                .broker("default")
                .status("active")
                .user(user)
                .createdDate(java.time.LocalDateTime.now())
                .build();

        try {
            botRepository.save(bot);
            log.info("Bot created: {}", bot.getTicker());
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating bot");
        }

        return new BotResponseDto(bot.getTicker(), "success");
    }

    @Transactional
    public BotInfoDto get(UserDetails userDetails, String ticker) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        log.info("User: {}, Ticker: {}", user.getId(), ticker);

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
        log.info("Bot deleted: {}", ticker);
    }
}