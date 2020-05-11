package com.sberbot.app;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.sberbot.app.config.FlywayOracleConfiguration;
import com.sberbot.app.config.OracleDataSourceConfig;
import com.sberbot.app.dao.BotAppOracleDao;
import com.sberbot.app.dao.impl.BotAppOracleDaoImpl;
import com.sberbot.app.service.BotService;
import org.flywaydb.core.Flyway;
import org.openqa.selenium.UnhandledAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.env.Environment;
import static com.codeborne.selenide.Selenide.*;

import java.time.LocalDateTime;

@ComponentScan(excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {FlywayOracleConfiguration.class})})
//@ComponentScan(excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,value = {OracleDataSourceConfig.class, BotAppOracleDao.class, FlywayOracleConfiguration.class, BotAppOracleDaoImpl.class})})
@SpringBootApplication//(exclude ={OracleDataSourceConfig.class})//(exclude = {OracleDataSourceConfig.class})
public class SberAstBotApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(SberAstBotApplication.class.getSimpleName());

	private int counter = 0;

	@Autowired
    BotService botService;

	@Autowired
	Environment environment;

	public static void main(String[] args) {
		SpringApplication.run(SberAstBotApplication.class, args);

	}

	@Override
	public void run(String... args) throws InterruptedException{
		botService.enterSberAuction();
		Thread.sleep(500);
		for(;;) {
			try {

				if(counter == Integer.parseInt(environment.getProperty("bot.browser.reloadingInterval"))) {
					logger.info("Вызываем обнуление счетчика и перезагрузку браузера");
					counter = callBrowserReload(counter);
				}
				counter++;
				logger.info("счетчик пезагрузки браузера равен :" + counter);
				logger.info("Запускаем бота в" + " " + LocalDateTime.now());
				logger.info("OracleDB healthCheck " + botService.getHelthCheckOracle());
				System.out.println("Бот запущен в " + LocalDateTime.now());
				SelenideElement selenideElement;
				try{
					selenideElement = botService.seachOption();
				}catch (UnhandledAlertException e) {
					confirm();
					selenideElement = null;
				}
				if(selenideElement!=null) {
					logger.info("Проверяем есть ли новые аукционы, если да - забираем");
					if(botService.checkMaxAuctionPublicDate(selenideElement)) {
						logger.info("Новых аукционов пока что нет - готовимся к спячке");
					}else {
						botService.getAuction(selenideElement);
					}
					System.out.println("Бот остановлен в " + LocalDateTime.now());
					Thread.sleep(Long.parseLong(environment.getProperty("bot.sleeping.interval")));
				}
				logger.info("не удалось получить таблицу с тендерами, попробуем повторить в следующей итерации");
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public int callBrowserReload(int counter) {
		closeWebDriver();
		botService.enterSberAuction();
		return counter = 0;
	}
}
