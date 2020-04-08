package com;

import com.sberbot.app.model.AuctionModel;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class SuperTest {

    @Test
    void testoftests() {
        AuctionModel auctionModel = new AuctionModel();
        auctionModel.setAuctionNumber("0307100007520000005");
        auctionModel.setTenderName("Прочие работы, услуги (страхование ОСАГО)");
        auctionModel.setOrgName("ПЕЧОРСКОЕ УПРАВЛЕНИЕ ФЕДЕРАЛЬНОЙ СЛУЖБЫ ПО ЭКОЛОГИЧЕСКОМУ, ТЕХНОЛОГИЧЕСКОМУ И АТОМНОМУ НАДЗОРУ");
        auctionModel.setPublicDate("31.03.2020 08:51");
        auctionModel.setSum("59 976.82");
        AuctionModel auctionModel1 = new AuctionModel();
        auctionModel1.setAuctionNumber("0307100007520000005");
        auctionModel1.setTenderName("Прочие работы, услуги");
        auctionModel1.setOrgName("ПЕЧОРСКОЕ УПРАВЛЕНИЕ ФЕДЕРАЛЬНОЙ СЛУЖБЫ ПО ЭКОЛОГИЧЕСКОМУ, ТЕХНОЛОГИЧЕСКОМУ И АТОМНОМУ НАДЗОРУ");
        auctionModel1.setPublicDate("31.03.2020 08:51");
        auctionModel1.setSum("59 976.82");
        List<AuctionModel> auctionModels = new ArrayList<>();
        auctionModels.add(auctionModel);
        auctionModels.add(auctionModel1);

        for(AuctionModel auctionModelFromList : auctionModels) {
            System.out.println(auctionModelFromList.getTenderName().contains("ОСАГО"));
        }
    }
}
