import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.After;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.*;

public class GoogleTest {

    @Autowired
    Environment environment;

    @BeforeAll
   static void setBrowserDriver() {
        System.setProperty("webdriver.chrome.driver", "C:\\App\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        WebDriverRunner.setWebDriver(driver);
    }

    @AfterAll
    static void closeBrowserDriver() {
        WebDriverRunner.closeWebDriver();
    }

    @Test
    void GoogleSearch() throws Exception {
        open("https://www.google.ru/?gws_rd=cr");
        element(byName("q")).setValue("сбербанк аст").pressEnter();
        SelenideElement selenideElement  = element(byText("Сбербанк-АСТ"));
        Assertions.assertEquals("<span>Сбербанк-АСТ</span>",selenideElement.toString());
    }

    @Test
    void SberSearch() throws Exception{
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

    @Test
    public void addMoreFilters() throws InterruptedException{
        open("https://www.sberbank-ast.ru/purchaseList.aspx");
        element(byId("searchInput")).setValue("страхование").pressEnter();
        element(byXpath("//*[@id=\"filters\"]/div/table/tbody/tr[1]/td[2]/button[1]")).click();
        element(byCssSelector("span[id=\"expandAdditionalFilters\"]")).click();
        element(byXpath("//*[@id=\"additionalFilters\"]/tbody/tr[8]/td[2]/table/tbody/tr/td[1]/input")).click();
        SelenideElement elem = element(byId("shortDictionaryModal"));
        SelenideElement table = elem.find(byCssSelector("table"));
        SelenideElement sl = (SelenideElement) table.findElements(byXpath("tbody/tr/td/input")).get(0);
        //String elem1 = table.find(byXpath("tbody/tr[1]")).toString();
        //String elem2 = table.find(byXpath("tbody/tr[2]")).toString();
        //System.out.println(elem1 + " " + elem2);
        Thread.sleep(20000);
    }

    @Test
    public void getTenderStatus () {
        open("https://www.sberbank-ast.ru/purchaseList.aspx");
        element(byId("searchInput")).setValue("осаго").pressEnter();
        SelenideElement selenideElement = element(byId("resultTable"));
        selenideElement.shouldBe(Condition.visible);
        String tenderStatus = selenideElement.find(byCssSelector("div.es-el-state-name.PurchStateName")).text();
        System.out.println(tenderStatus);
        //content="leaf:purchStateName
    }

    @Test
    public void getTenderStatusCollection() throws InterruptedException{
        open("https://www.sberbank-ast.ru/purchaseList.aspx");
        executeJavaScript("select = document.getElementById('headerPagerSelect');\n" +
                "var opt = document.createElement('option');\n" +
                "opt.value = 5;\n" +
                "opt.innerHTML = 5;\n" +
                "select.appendChild(opt);");
        element(byId("headerPagerSelect")).selectOptionByValue("5");
        //element(byId("headerPagerSelect")).waitUntil(Condition.selectedText("5"),500);
        element(byId("searchInput")).setValue("осаго").pressEnter();
        SelenideElement selenideElement = element(byId("resultTable"));
        //selenideElement.shouldBe(Condition.visible);
        List<String> statuses = selenideElement.findAll(byCssSelector("div.es-el-state-name.PurchStateName")).texts();
        System.out.println(statuses.size());
        /*
        for (String status : statuses) {
            System.out.println(status);
        }
         */
        Thread.sleep(10000);
    }
}
