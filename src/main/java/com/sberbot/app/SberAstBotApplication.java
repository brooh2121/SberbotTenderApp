package com.sberbot.app;

import com.sberbot.app.service.BotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@SpringBootApplication
public class SberAstBotApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(SberAstBotApplication.class.getSimpleName());

	@Autowired
    BotService botService;

	public static void main(String[] args) {
		SpringApplication.run(SberAstBotApplication.class, args);

	}

	@Override
	public void run(String... args) {
		for(;;) {
			try {
				logger.info("Запускаем бота в" + " " + LocalDateTime.now());
				botService.getAuction();
				Thread.sleep(60*1000);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
