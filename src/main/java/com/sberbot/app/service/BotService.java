package com.sberbot.app.service;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.sberbot.app.dao.BotAppDao;
import com.sberbot.app.model.AuctionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.swing.text.DateFormatter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

@Service
public class BotService {

    private static final Logger logger = LoggerFactory.getLogger(BotService.class.getSimpleName());

    @Autowired
    BotAppDao botAppDao;

    @Autowired
    Environment environment;

    public void enterSberAuction () {
        System.setProperty("webdriver.chrome.driver", environment.getProperty("webdriver.path"));
        System.setProperty("selenide.browser", "Chrome");
        logger.info("Переходим на сайт сбербанк-аст");
        open("https://www.sberbank-ast.ru/purchaseList.aspx");
        executeJavaScript("select = document.getElementById('headerPagerSelect');\n" +
                "var opt = document.createElement('option');\n" +
                "opt.value = 5;\n" +
                "opt.innerHTML = 5;\n" +
                "select.appendChild(opt);");
        element(byId("headerPagerSelect")).selectOptionByValue("5");
    }

    public SelenideElement seachOption() {
        element(byId("searchInput")).setValue("осаго").pressEnter();
        //element(byClassName("es-el-source-term")).shouldHave(text("Госзакупки по 44-ФЗ")).click();
        SelenideElement selenideElement = element(byId("resultTable"));
        selenideElement.shouldBe(Condition.visible);
        SelenideElement elem = selenideElement.find(byClassName("es-el-name"));
        elem.shouldHave(Condition.text("ОСАГО")).text().toUpperCase();
        return selenideElement;
    }

    public Boolean checkMaxAuctionPublicDate(SelenideElement selenideElement) throws Exception {
        String maxStringPublicDate = selenideElement.find(byCssSelector("span[content='leaf:PublicDate']")).text();
        String dateFromDao = botAppDao.getMaxPublicDate();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        DateTimeFormatter df1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssX");
        LocalDateTime ldt;
        LocalDateTime ldt1;
        if(StringUtils.hasText(dateFromDao)) {
            ldt = LocalDateTime.parse(maxStringPublicDate,df);
            ldt1 = LocalDateTime.parse(dateFromDao,df1);
            return ldt.equals(ldt1);
        } else return false;
        //String resultString = "Дата со страницы " + ldt + " и максимальная дата из БД " + ldt1 + " " + ldt.equals(ldt1);
        //return ldt.equals(ldt1);
    }

    public void getAuction (SelenideElement selenideElement) {
        try {
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
                        if(!auctionModel.getAuctionNumber().contains("element is not attached to the page document")
                        &!auctionModel.getTenderName().contains("element is not attached to the page document")
                        &!auctionModel.getOrgName().contains("element is not attached to the page document")
                        &!auctionModel.getPublicDate().contains("element is not attached to the page document")
                        &!auctionModel.getSum().contains("element is not attached to the page document")) {
                            logger.info("Загружаем в базу данных новый аукцион " + auctionModel.getAuctionNumber());
                            botAppDao.addAuction(auctionModel);
                        }
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
        }/*finally {
            closeWebDriver();
            logger.info("Закрыли окно браузера");
        }*/
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
            //logger.info(publicDates.get(i));
        }
        return auctionModels;
    }

    private List<AuctionModel> getListOfOrgNames(SelenideElement selenideElement,List<AuctionModel> auctionModels) {
        List<String> orgNames = selenideElement.findAll(byClassName("es-el-org-name")).texts();
        logger.info("По порядку с первого элемента коллекции заполняем наименования организаций");
        for(int i=0; i < orgNames.size();i++) {
            auctionModels.get(i).setOrgName(orgNames.get(i));
            //logger.info(orgNames.get(i));
        }

        return auctionModels;
    }

    private List<AuctionModel> getListOfTenderNames(SelenideElement selenideElement,List<AuctionModel> auctionModels) {
        List<String> tenderNames = selenideElement.findAll(byClassName("es-el-name")).texts();
        logger.info("По порядку с первого элемента коллекции заполняем наименования тендеров");
        for(int i=0; i < tenderNames.size();i++) {
            auctionModels.get(i).setTenderName(tenderNames.get(i));
            //logger.info(tenderNames.get(i));
        }
        return auctionModels;
    }

    private List<AuctionModel> getListOfTenderSums(SelenideElement selenideElement,List<AuctionModel> auctionModels) {
        List<String> tenrersSums = selenideElement.findAll(byClassName("es-el-amount")).texts();
        logger.info("По порядку с первого элемента коллекции заполняем суммы тендеров");
        for (int i=0;i < tenrersSums.size(); i++) {
            auctionModels.get(i).setSum(tenrersSums.get(i));
            //logger.info(tenrersSums.get(i));
        }
        return auctionModels;
    }

}
