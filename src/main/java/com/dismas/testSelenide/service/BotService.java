package com.dismas.testSelenide.service;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import com.dismas.testSelenide.AuctionModel;
import com.google.gson.internal.$Gson$Preconditions;
import org.openqa.selenium.By;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

@Service
public class BotService {

    public void getAuction () {
        //Configuration.timeout=6000;
        //Configuration.browser="firefox";
        //open("https://www.google.ru/?gws_rd=cr");
        //element(byName("q")).setValue("сбербанк аст").pressEnter();
        //elements(byClassName("l")).findBy(text("Закупки по 44-ФЗ")).click();
        //sleep(3000);
        open("https://www.sberbank-ast.ru/purchaseList.aspx");
        element(byId("searchInput")).setValue("осаго").pressEnter();
        element(byClassName("es-el-source-term")).shouldHave(text("Госзакупки по 44-ФЗ")).click();
        element(byId("footerPagerSelect")).selectOptionContainingText("20");
        sleep(1000);
        SelenideElement selenideElement = element(byXpath("//*[@id='resultTable']"));

        List<AuctionModel> auctionModels = new ArrayList<>();

        List<String> auctionCodes = selenideElement.findAll(byClassName("es-el-code-term")).texts();
        /*
        for(int i = 0; i < auctionCodes.size(); i++) {
            //auctionCodes.add(new AuctionModel(auctionCodes.get(i),"","",""));
            auctionModels.add(new AuctionModel(auctionCodes.get(i),"null","null","null","null"));
        }
        */

        for(String auctionCode : auctionCodes) {
            auctionModels.add(new AuctionModel(auctionCode,"null","null","null","null"));
        }

        List<String> publicDates = selenideElement.findAll(byCssSelector("span[content='leaf:PublicDate']")).texts();

        for(int i=0; i < publicDates.size(); i ++) {
            auctionModels.get(i).setPublicDate(publicDates.get(i));
        }

        List<String> orgNames = selenideElement.findAll(byClassName("es-el-org-name")).texts();

        for(int i=0; i < orgNames.size();i++) {
            auctionModels.get(i).setOrgName(orgNames.get(i));
        }

        List<String> tenderNames = selenideElement.findAll(byClassName("es-el-name")).texts();

       for(int i=0; i < tenderNames.size();i++) {
           auctionModels.get(i).setTenderName(tenderNames.get(i));
       }

        List<String> tenrersSums = selenideElement.findAll(byClassName("es-el-amount")).texts();

       for (int i=0;i < tenrersSums.size(); i++) {
           auctionModels.get(i).setSum(tenrersSums.get(i));
       }

        for(AuctionModel auctionModelFromList : auctionModels) {
            System.out.println(auctionModelFromList.getAuctionNumber() + " " + auctionModelFromList.getPublicDate() + " " + auctionModelFromList.getOrgName() + " " + auctionModelFromList.getTenderName() + " " + auctionModelFromList.getSum());
        }
    }
}
