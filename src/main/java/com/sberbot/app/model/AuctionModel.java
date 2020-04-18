package com.sberbot.app.model;

import java.util.Objects;

public class AuctionModel {
    private String auctionNumber;
    private String orgName;
    private String tenderName;
    private String tenderType;
    private String publicDate;
    private String tenderBegDate;
    private String tenderEndDate;
    private String sum;

    public AuctionModel() {
    }

    public AuctionModel(String auctionNumber, String orgName, String tenderName, String tenderType, String publicDate, String tenderBegDate, String tenderEndDate, String sum) {
        this.auctionNumber = auctionNumber;
        this.orgName = orgName;
        this.tenderName = tenderName;
        this.tenderType = tenderType;
        this.publicDate = publicDate;
        this.tenderBegDate = tenderBegDate;
        this.tenderEndDate = tenderEndDate;
        this.sum = sum;
    }

    public String getAuctionNumber() {
        return auctionNumber;
    }

    public void setAuctionNumber(String auctionNumber) {
        this.auctionNumber = auctionNumber;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getTenderName() {
        return tenderName;
    }

    public void setTenderName(String tenderName) {
        this.tenderName = tenderName;
    }

    public String getTenderType() {
        return tenderType;
    }

    public void setTenderType(String tenderType) {
        this.tenderType = tenderType;
    }

    public String getPublicDate() {
        return publicDate;
    }

    public void setPublicDate(String publicDate) {
        this.publicDate = publicDate;
    }

    public String getTenderBegDate() {
        return tenderBegDate;
    }

    public void setTenderBegDate(String tenderBegDate) {
        this.tenderBegDate = tenderBegDate;
    }

    public String getTenderEndDate() {
        return tenderEndDate;
    }

    public void setTenderEndDate(String tenderEndDate) {
        this.tenderEndDate = tenderEndDate;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuctionModel that = (AuctionModel) o;
        return auctionNumber.equals(that.auctionNumber) &&
                orgName.equals(that.orgName) &&
                tenderName.equals(that.tenderName) &&
                publicDate.equals(that.publicDate) &&
                sum.equals(that.sum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(auctionNumber, orgName, tenderName, publicDate, sum);
    }
}
