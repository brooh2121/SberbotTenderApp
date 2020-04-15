package com.sberbot.app.service;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.sberbot.app.dao.BotAppDao;
import com.sberbot.app.model.AuctionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

@Service
public class BotService {

    private static final Logger logger = LoggerFactory.getLogger(BotService.class.getSimpleName());

    @Autowired
    BotAppDao botAppDao;

    @Autowired
    Environment environment;

    public void getAuction () {
        try {
            System.setProperty("webdriver.chrome.driver", environment.getProperty("webdriver.path"));
            System.setProperty("selenide.browser", "Chrome");
            logger.info("Переходим на сайт сбербанк-аст");
            open("https://www.sberbank-ast.ru/purchaseList.aspx");
            element(byId("searchInput")).setValue("осаго").pressEnter();
            //element(byClassName("es-el-source-term")).shouldHave(text("Госзакупки по 44-ФЗ")).click();
            //element(byId("footerPagerSelect")).selectOptionContainingText("20");

            SelenideElement selenideElement = element(byXpath("//*[@id='resultTable']")).waitUntil(Condition.visible,6000);

            List<AuctionModel> auctionModels = new ArrayList<>();
            List<AuctionModel> auctionModelsFromDao = botAppDao.getAllAutions();

            getListOfCodes(selenideElement,auctionModels);
            getListOfPublicDates(selenideElement,auctionModels);
            getListOfOrgNames(selenideElement,auctionModels);
            getListOfTenderNames(selenideElement,auctionModels);
            getListOfTenderSums(selenideElement,auctionModels);

            Set<AuctionModel> uniqueAuctionModels = new HashSet<>(auctionModels);
            Set<AuctionModel> uniqueAuctionModelsFromDao = new HashSet<>(auctionModelsFromDao);

            for (AuctionModel auctionModel : uniqueAuctionModels) {
                if(!uniqueAuctionModelsFromDao.contains(auctionModel)) {
                    if(auctionModel.getTenderName().toUpperCase().contains("ОСАГО")) {
                        logger.info("Загружаем в базу данных новый аукцион " + auctionModel.getAuctionNumber());
                        botAppDao.addAuction(auctionModel);
                    }else {
                        logger.info("Попался АУКЦИОН не по ОСАГО с номером " + auctionModel.getAuctionNumber() + ", в БД не записываем");
                    }

                }else {
                   logger.info("Аукцион с номером " + auctionModel.getAuctionNumber() + " уже есть в базе данных");
                }

            }

        }catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString());
        }finally {
            closeWebDriver();
            logger.info("Закрыли окно браузера");
        }
    }

    private List<AuctionModel> getListOfCodes(SelenideElement selenideElement, List<AuctionModel> auctionModels) {
        List<String> auctionCodes = selenideElement.findAll(byClassName("es-el-code-term")).texts();
        for(String auctionCode : auctionCodes) {
            logger.info("Дополняем коллекцию новым аукционом с номером " + auctionCode + " но остальные параметры пока null");
            auctionModels.add(new AuctionModel(auctionCode,"null","null","null","null"));
        }
        return auctionModels;
    }

    private List<AuctionModel> getListOfPublicDates(SelenideElement selenideElement, List<AuctionModel> auctionModels) {
        List<String> publicDates = selenideElement.findAll(byCssSelector("span[content='leaf:PublicDate']")).texts();
        logger.info("По порядку с первого элемента коллекции заполняем дату публикации");
        for(int i=0; i < publicDates.size(); i ++) {
            auctionModels.get(i).setPublicDate(publicDates.get(i));
        }
        return auctionModels;
    }

    private List<AuctionModel> getListOfOrgNames(SelenideElement selenideElement,List<AuctionModel> auctionModels) {
        List<String> orgNames = selenideElement.findAll(byClassName("es-el-org-name")).texts();
        logger.info("По порядку с первого элемента коллекции заполняем наименования организаций");
        for(int i=0; i < orgNames.size();i++) {
            auctionModels.get(i).setOrgName(orgNames.get(i));
        }

        return auctionModels;
    }

    private List<AuctionModel> getListOfTenderNames(SelenideElement selenideElement,List<AuctionModel> auctionModels) {
        List<String> tenderNames = selenideElement.findAll(byClassName("es-el-name")).texts();
        logger.info("По порядку с первого элемента коллекции заполняем наименования тендеров");
        for(int i=0; i < tenderNames.size();i++) {
            auctionModels.get(i).setTenderName(tenderNames.get(i));
        }
        return auctionModels;
    }

    private List<AuctionModel> getListOfTenderSums(SelenideElement selenideElement,List<AuctionModel> auctionModels) {
        List<String> tenrersSums = selenideElement.findAll(byClassName("es-el-amount")).texts();
        logger.info("По порядку с первого элемента коллекции заполняем суммы тендеров");
        for (int i=0;i < tenrersSums.size(); i++) {
            auctionModels.get(i).setSum(tenrersSums.get(i));
        }
        return auctionModels;
    }

}
