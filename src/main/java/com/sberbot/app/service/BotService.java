package com.sberbot.app.service;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import com.sberbot.app.dao.BotAppDao;
import com.sberbot.app.dao.BotAppOracleDao;
import com.sberbot.app.model.AuctionModel;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.rmi.UnexpectedException;
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
    BotAppOracleDao botAppOracleDao;

    @Autowired
    Environment environment;

    public void enterSberAuction() {
        try {
            System.setProperty("webdriver.chrome.driver", environment.getProperty("webdriver.path"));
            System.setProperty("selenide.browser", "Chrome");
            Configuration.reopenBrowserOnFail = true;
            Configuration.browserCapabilities.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
            logger.info("Переходим на сайт сбербанк-аст");
            open("https://www.sberbank-ast.ru/purchaseList.aspx");
            executeJavaScript("select = document.getElementById('headerPagerSelect');\n" +
                    "var opt = document.createElement('option');\n" +
                    "opt.value = 5;\n" +
                    "opt.innerHTML = 5;\n" +
                    "select.appendChild(opt);");
            element(byId("headerPagerSelect")).selectOptionByValue("5");

            element(byXpath("//*[@id=\"filters\"]/div/table/tbody/tr[1]/td[2]/button[1]")).shouldBe(Condition.visible).click(); // фильтры добавить изменить
            element(byXpath("//*[@id=\"expandAdditionalFilters\"]")).shouldBe(Condition.visible).click(); // еще фильтры
            element(byXpath("//*[@id=\"additionalFilters\"]/tbody/tr[8]/td[2]/table/tbody/tr/td[1]/input")).shouldBe(Condition.visible).click(); // отрасль
            element(byXpath("/html/body/form/div[7]/div/div/div[13]/div/div/div[2]/div/table/tbody/tr[20]/td[1]/input")).click(); // услуги в непроизводественной сфере
            element(byXpath("//*[@id=\"shortDictionaryModal\"]/div/div/div[3]/input")).click(); // выбрать
            element(byId("searchInput")).setValue("страхование").pressEnter(); // фильтр по значению в поле ввода

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public SelenideElement seachOption() {
        element(byId("searchInput")).setValue("страхование").pressEnter();
        try {
            ExpectedConditions.alertIsPresent();
            Alert alert = switchTo().alert();
            logger.info("Выскочил alert с текстом "  + alert.getText());
            alert.accept();
        }catch (NoAlertPresentException e) {
            logger.error("Alert not found");
        }
        SelenideElement selenideElement = element(byId("resultTable"));
        selenideElement.shouldBe(Condition.visible);
        return selenideElement;

    }

    public Boolean checkMaxAuctionPublicDate(SelenideElement selenideElement) throws Exception {
        String maxStringPublicDate = selenideElement.find(byCssSelector("span[content='leaf:PublicDate']")).text();
        String dateFromDao = botAppDao.getMaxPublicDate();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        DateTimeFormatter df1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssX");
        LocalDateTime ldt;
        LocalDateTime ldt1;
        if (StringUtils.hasText(dateFromDao) & StringUtils.hasText(maxStringPublicDate) & !dateFromDao.equals("null")) {
            ldt = LocalDateTime.parse(maxStringPublicDate, df);
            ldt1 = LocalDateTime.parse(dateFromDao, df1);
            return ldt.equals(ldt1);
        } else return false;
    }

    public void getAuction(SelenideElement selenideElement) {
        try {
            List<AuctionModel> auctionModels = new ArrayList<>();
            List<AuctionModel> auctionModelsFromDao = botAppDao.getAllAutions();

            getListOfCodes(selenideElement, auctionModels);
            getListOfPublicDates(selenideElement, auctionModels);
            getListOfOrgNames(selenideElement, auctionModels);
            getListOfTenderNames(selenideElement, auctionModels);
            getListOfTenderSums(selenideElement, auctionModels);
            getListOfTenderTypes(selenideElement, auctionModels);
            getListOfTenderBegDates(selenideElement, auctionModels);
            getListOfTenderEndDates(selenideElement, auctionModels);
            getListOfTenderStatuses(selenideElement,auctionModels);

            //Set<AuctionModel> uniqueAuctionModels = new HashSet<>(auctionModels);
            //Set<AuctionModel> uniqueAuctionModelsFromDao = new HashSet<>(auctionModelsFromDao);

            for (AuctionModel auctionModel : auctionModels) {
                if (!auctionModel.getAuctionNumber().contains("element is not attached to the page document")
                        & !auctionModel.getTenderName().contains("element is not attached to the page document")
                        & !auctionModel.getOrgName().contains("element is not attached to the page document")
                        & !auctionModel.getPublicDate().contains("element is not attached to the page document")
                        & !auctionModel.getSum().contains("element is not attached to the page document")
                        & !auctionModel.getTenderType().contains("element is not attached to the page document")
                        & !auctionModel.getTenderBegDate().contains("element is not attached to the page document")
                        & !auctionModel.getTenderEndDate().contains("element is not attached to the page document")
                        & !auctionModel.getTenderStatus().contains("element is not attached to the page document")
                        & !auctionModel.getTenderName().equals("null")
                        & !auctionModel.getOrgName().equals("null")
                        & !auctionModel.getPublicDate().equals("null")
                        & !auctionModel.getSum().equals("null")
                        & !auctionModel.getTenderType().equals("null")
                        & !auctionModel.getTenderBegDate().equals("null")
                        & !auctionModel.getTenderEndDate().equals("null")
                        & !auctionModel.getTenderStatus().equals("null")
                ) {
                    if (!auctionModelsFromDao.contains(auctionModel)) {
                        logger.info(auctionModelsFromDao.toString());
                        logger.info(auctionModel.toString());
                        logger.info("Загружаем в базу данных новый аукцион "
                                + auctionModel.getAuctionNumber() + " "
                                + auctionModel.getTenderName() + " "
                                + auctionModel.getOrgName() + " "
                                + auctionModel.getPublicDate() + " "
                                + auctionModel.getSum() + " "
                                + auctionModel.getTenderType() + " "
                                + auctionModel.getTenderBegDate() + " "
                                + auctionModel.getTenderEndDate() + " "
                                + auctionModel.getTenderStatus()
                        );

                        botAppDao.addAuction(auctionModel);
                        botAppOracleDao.addAuctionModelToOracle(botAppOracleDao.getOraTenderSequence(), auctionModel);

                    } else {
                        logger.info("Аукцион с номером " + auctionModel.getAuctionNumber() + " уже есть в базе данных");
                    }
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }/*finally {
            closeWebDriver();
            logger.info("Закрыли окно браузера");
        }*/
    }

    public String getHelthCheckOracle() {
        return botAppOracleDao.getHelthCheck();
    }

    private List<AuctionModel> getListOfCodes(SelenideElement selenideElement, List<AuctionModel> auctionModels) {
        List<String> auctionCodes = selenideElement.findAll(byClassName("es-el-code-term")).texts();
        for (String auctionCode : auctionCodes) {
            logger.info("Дополняем коллекцию новым аукционом с номером " + auctionCode + " но остальные параметры пока null");
            auctionModels.add(new AuctionModel(auctionCode, "null", "null", "null", "null", "null", "null", "null","null"));
        }
        logger.info("Общий размер коллекции номеров" + auctionCodes.size());
        return auctionModels;
    }

    private List<AuctionModel> getListOfPublicDates(SelenideElement selenideElement, List<AuctionModel> auctionModels) {
        List<String> publicDates = selenideElement.findAll(byCssSelector("span[content='leaf:PublicDate']")).texts();
        logger.info("По порядку с первого элемента коллекции заполняем дату публикации");
        for (int i = 0; i < publicDates.size(); i++) {
            auctionModels.get(i).setPublicDate(publicDates.get(i));
            //logger.info(publicDates.get(i));
        }
        logger.info("Общий размер коллекции дат публикации " + publicDates.size());
        return auctionModels;
    }

    private List<AuctionModel> getListOfOrgNames(SelenideElement selenideElement, List<AuctionModel> auctionModels) {
        List<String> orgNames = selenideElement.findAll(byClassName("es-el-org-name")).texts();
        logger.info("По порядку с первого элемента коллекции заполняем наименования организаций");
        for (int i = 0; i < orgNames.size(); i++) {
            auctionModels.get(i).setOrgName(orgNames.get(i));
            //logger.info(orgNames.get(i));
        }
        logger.info("Общий размер коллекции наименований организаций " + orgNames.size());
        return auctionModels;
    }

    private List<AuctionModel> getListOfTenderNames(SelenideElement selenideElement, List<AuctionModel> auctionModels) {
        List<String> tenderNames = selenideElement.findAll(byClassName("es-el-name")).texts();
        logger.info("По порядку с первого элемента коллекции заполняем наименования тендеров");
        for (int i = 0; i < tenderNames.size(); i++) {
            auctionModels.get(i).setTenderName(tenderNames.get(i));
            //logger.info(tenderNames.get(i));
        }
        logger.info("Общий размер коллекции наименований тендеров " + tenderNames.size());
        return auctionModels;
    }

    private List<AuctionModel> getListOfTenderSums(SelenideElement selenideElement, List<AuctionModel> auctionModels) {
        List<String> tenrersSums = selenideElement.findAll(byClassName("es-el-amount")).texts();
        logger.info("По порядку с первого элемента коллекции заполняем суммы тендеров");
        for (int i = 0; i < tenrersSums.size(); i++) {
            auctionModels.get(i).setSum(tenrersSums.get(i));
            //logger.info(tenrersSums.get(i));
        }
        logger.info("Общий размер коллекции сум тендеров " + tenrersSums.size());
        return auctionModels;
    }

    private List<AuctionModel> getListOfTenderTypes(SelenideElement selenideElement, List<AuctionModel> auctionModels) {
        List<String> tenderTypes = selenideElement.findAll(byClassName("es-el-type-name")).texts();
        logger.info("По порядку с первого элемента коллекции заполняем типы тендеров");
        for (int i = 0; i < tenderTypes.size(); i++) {
            auctionModels.get(i).setTenderType(tenderTypes.get(i));
            logger.info(tenderTypes.get(i));
        }
        logger.info("Общий размер коллекции типов тендеров " + tenderTypes.size());
        return auctionModels;
    }

    private List<AuctionModel> getListOfTenderBegDates(SelenideElement selenideElement, List<AuctionModel> auctionModels) {
        List<String> tenderBegDates = selenideElement.findAll(byCssSelector("span[content='leaf:RequestStartDate']")).texts();
        logger.info("По порядку с первого элемента коллекции заполняем даты начала тендеров");
        for (int i = 0; i < tenderBegDates.size(); i++) {
            auctionModels.get(i).setTenderBegDate(tenderBegDates.get(i));
            logger.info(tenderBegDates.get(i));
        }
        logger.info("Общий размер коллекции дат начала тендера " + tenderBegDates.size());
        return auctionModels;
    }

    private List<AuctionModel> getListOfTenderEndDates(SelenideElement selenideElement, List<AuctionModel> auctionModels) {
        List<String> tenderEndDates = selenideElement.findAll(byCssSelector("span[content='leaf:RequestDate']")).texts();
        logger.info("По порядку с первого элемента коллекции заполняем даты окончания тендеров");
        for (int i = 0; i < tenderEndDates.size(); i++) {
            auctionModels.get(i).setTenderEndDate(tenderEndDates.get(i));
            logger.info(tenderEndDates.get(i));
        }
        logger.info("Общий размер коллекции дат окончания тендера " + tenderEndDates.size());
        return auctionModels;
    }

    private List<AuctionModel> getListOfTenderStatuses(SelenideElement selenideElement,List<AuctionModel> auctionModels) {
        List<String> tenderStatuses = selenideElement.findAll(byCssSelector("div.es-el-state-name.PurchStateName")).texts();
        logger.info("По порядку с первого элемента коллекции заполняем статусы тендеров");
        for (int i = 0; i < tenderStatuses.size(); i++) {
            auctionModels.get(i).setTenderStatus(tenderStatuses.get(i));
        }
        logger.info("Общий размер коллекции статусов тендеров " + tenderStatuses.size());
        return  auctionModels;
    }

}
