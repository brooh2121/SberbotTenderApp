package com.sberbot.app;

import com.sberbot.app.service.BotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SberAstBotApplication implements CommandLineRunner {

	@Autowired
    BotService botService;

	public static void main(String[] args) {
		SpringApplication.run(SberAstBotApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		botService.getAuction();
	}
}
