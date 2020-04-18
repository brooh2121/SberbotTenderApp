import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.*;

public class GoogleTest {

    @Test
    void DummiTest() {
        String str = "<span>Сбербанк-АСТ</span>";
        System.out.println(str);
    }

    @Test
    void GoogleSearch() {
        open("https://www.google.ru/?gws_rd=cr");
        element(byName("q")).setValue("сбербанк аст").pressEnter();
        SelenideElement selenideElement  = element(byText("Сбербанк-АСТ"));
        Assertions.assertEquals("<span>Сбербанк-АСТ</span>",selenideElement.toString());
    }

    @Test
    void SberSearch() {
        open("https://www.sberbank-ast.ru/purchaseList.aspx");
        element(byId("searchInput")).setValue("Электронный аукцион осаго").pressEnter();

        //SelenideElement selenideElement = element(byXpath("//*[@id='resultTable']"));
        SelenideElement selenideElement = element(byId("resultTable"));
        selenideElement.shouldBe(Condition.visible);
        List<String> publicDates = selenideElement.findAll(byCssSelector("span[content='leaf:PublicDate']")).texts();

        for(String publicDate : publicDates) {
            System.out.println(publicDate);
        }
        List<String> auctionCodes = selenideElement.findAll(byClassName("es-el-code-term")).texts();
        for(String auctionCode : auctionCodes) {
            System.out.println(auctionCode);
        }
        List<String> orgNames = selenideElement.findAll(byClassName("es-el-org-name")).texts();
        for(String orgname : orgNames) {
            System.out.println(orgname);
        }
        List<String> tenderNames = selenideElement.findAll(byClassName("es-el-name")).texts();
        for(String tenderName : tenderNames) {
            System.out.println(tenderName);
        }
        List<String> tenrersSums = selenideElement.findAll(byClassName("es-el-amount")).texts();
        for (String tendersSum : tenrersSums) {
            System.out.println(tendersSum);
        }
        Assertions.assertNotNull(selenideElement);
    }

    @Test
    public void castTenderSumToInt() {
        open("https://www.sberbank-ast.ru/purchaseList.aspx");
        element(byId("searchInput")).setValue("осаго").pressEnter();
        SelenideElement selenideElement = element(byId("resultTable"));
        selenideElement.shouldBe(Condition.visible);
        String tenderSum = selenideElement.find(byClassName("es-el-amount")).text();
        System.out.println(tenderSum.replace(" ",""));
        double result = Double.parseDouble(tenderSum.replace(" ",""));
        System.out.println(result);
    }

    @Test
    public void tenderType() {
        open("https://www.sberbank-ast.ru/purchaseList.aspx");
        element(byId("searchInput")).setValue("осаго").pressEnter();
        SelenideElement selenideElement = element(byId("resultTable"));
        selenideElement.shouldBe(Condition.visible);
        String tenderType = selenideElement.find(byClassName("es-el-type-name")).text();
        System.out.println(tenderType);
    }

    //пока не работает
    @Test
    public void linkButton() throws InterruptedException{
        open("https://www.sberbank-ast.ru/purchaseList.aspx");
        element(byId("searchInput")).setValue("осаго").pressEnter();
        SelenideElement selenideElement = element(byId("resultTable"));
        selenideElement.shouldBe(Condition.visible);
        selenideElement.find(byValue("Просмотр")).click();
        //selenideElement.shouldBe(Condition.visible);
        //System.out.println(url());
        //selenideElement.find(byClassName("link-button")).click();
        //System.out.println(url());
        //System.out.println(linkButtonUrl);
    }

    @Test
    public void castTenderPublicDate() {
        open("https://www.sberbank-ast.ru/purchaseList.aspx");
        element(byId("searchInput")).setValue("осаго").pressEnter();
        SelenideElement selenideElement = element(byId("resultTable"));
        selenideElement.shouldBe(Condition.visible);
        String publicDate = selenideElement.find(byCssSelector("span[content='leaf:PublicDate']")).text();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        LocalDateTime ldt = LocalDateTime.parse(publicDate,df);
        System.out.println(ldt);
    }

    @Test
    public void castTenderBegDate() {
        open("https://www.sberbank-ast.ru/purchaseList.aspx");
        element(byId("searchInput")).setValue("осаго").pressEnter();
        SelenideElement selenideElement = element(byId("resultTable"));
        selenideElement.shouldBe(Condition.visible);
        String bidBegDate = selenideElement.find(byCssSelector("span[content='leaf:RequestStartDate']")).text();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        LocalDateTime bdt = LocalDateTime.parse(bidBegDate,df);
        System.out.println(bdt);
    }

    @Test
    public void castTenderEndDate() {
        open("https://www.sberbank-ast.ru/purchaseList.aspx");
        element(byId("searchInput")).setValue("осаго").pressEnter();
        SelenideElement selenideElement = element(byId("resultTable"));
        selenideElement.shouldBe(Condition.visible);
        String bidEndDate =  selenideElement.find(byCssSelector("span[content='leaf:RequestDate']")).text();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        LocalDateTime bdt = LocalDateTime.parse(bidEndDate,df);
        System.out.println(bdt);
    }

    //div.span[content='leaf:EndDate']
    @Test
    public void castTenderEndPlanDate() {
        open("https://www.sberbank-ast.ru/purchaseList.aspx");
        element(byId("searchInput")).setValue("осаго").pressEnter();
        SelenideElement selenideElement = element(byId("resultTable"));
        selenideElement.shouldBe(Condition.visible);
        SelenideElement tr = selenideElement.find(byXpath("tbody/tr[1]/td[3]/table/tbody/tr[4]/td[2]/div/span[content='leaf:EndDate']"));
        System.out.println(tr.toString());
        //String endPlanDate = selenideElement.find(byCssSelector("class-last")).text();
        //DateTimeFormatter df = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        //LocalDateTime edt = LocalDateTime.parse(endPlanDate,df);
        //System.out.println(edt);
    }
}
