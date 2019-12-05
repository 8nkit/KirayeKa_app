package com.example.kirayeka.DemoStructure;

public class RentReq {
    private String PhoneNo;
    private String ProductId;
    private String ProductName;
    private String Dayss;
    private String rent;
    private String Off5;
    private String Image;


    public RentReq() {
    }

    public RentReq(String userPhone, String productId, String productName, String dayss, String rent, String off5, String image) {
        PhoneNo = userPhone;
        ProductId = productId;
        ProductName = productName;
        Dayss = dayss;
        this.rent = rent;
        Off5 = off5;
        Image = image;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String userPhone) {
        PhoneNo = userPhone;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getDayss() {
        return Dayss;
    }

    public void setDayss(String dayss) {
        Dayss = dayss;
    }

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }

    public String getOff5() {
        return Off5;
    }

    public void setOff5(String off5) {
        Off5 = off5;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
