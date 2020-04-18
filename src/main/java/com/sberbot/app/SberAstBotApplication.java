package com.sberbot.app;

import com.codeborne.selenide.SelenideElement;
import com.sberbot.app.config.OracleDataSourceConfig;
import com.sberbot.app.service.BotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import java.time.LocalDateTime;

//@ComponentScan(excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,value = {OracleDataSourceConfig.class})})
@SpringBootApplication//(exclude ={OracleDataSourceConfig.class})//(exclude = {OracleDataSourceConfig.class})
public class SberAstBotApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(SberAstBotApplication.class.getSimpleName());

	@Autowired
    BotService botService;

	public static void main(String[] args) {
		SpringApplication.run(SberAstBotApplication.class, args);

	}

	@Override
	public void run(String... args) {
		botService.enterSberAuction();
		for(;;) {
			try {
				logger.info("Запускаем бота в" + " " + LocalDateTime.now());
				logger.info("OracleDB healthCheck " + botService.getHelthCheckOracle());
				System.out.println("Бот запущен в " + LocalDateTime.now());
				SelenideElement selenideElement = botService.seachOption();
				logger.info("Проверяем есть ли новые аукционы, если да - забираем");
				if(botService.checkMaxAuctionPublicDate(selenideElement)) {
					logger.info("Новых аукционов пока что нет - готовимся к спячке");
				}else {
					botService.getAuction(selenideElement);
				}
				System.out.println("Бот остановлен в " + LocalDateTime.now());
				Thread.sleep(1000);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
