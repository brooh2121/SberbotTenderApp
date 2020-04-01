import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import java.util.List;

import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

public class GoogleTest {

    @Test
    void GoogleSearch() {
        open("https://www.google.ru/?gws_rd=cr");
        element(byName("q")).setValue("сбербанк аст").pressEnter();
    }

    @Test
    void SberSearch() {
        open("https://www.sberbank-ast.ru/purchaseList.aspx");
        element(byId("searchInput")).setValue("Электронный аукцион осаго").pressEnter();
        /*
        ElementsCollection strs = elements(byClassName("es-el-type-name"));
        for(SelenideElement element : strs) {
            System.out.println(element.shouldBe(Condition.visible));
        }
        */
        //element((byId("searchInput"))).setValue("электронный аукцион осаго").pressEnter();
        //ElementsCollection auctionCodes = elements(byXpath("'.es-el-code-term'"));
        //System.out.println(auctionCodes.size());

        SelenideElement selenideElement = element(byXpath("//*[@id='resultTable']"));
        selenideElement.shouldBe(Condition.visible);
        List<String> publicDates = selenideElement.findAll(byCssSelector("span[content='leaf:PublicDate']")).texts();

        System.out.println(publicDates.size());


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
        /*
        for(SelenideElement element : auctionCodes) {
            System.out.println(element.shouldBe(Condition.visible));
        }
         */
    }
}
