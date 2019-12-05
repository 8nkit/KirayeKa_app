package com.example.kirayeka.DemoStructure;

public class ReturnReq {
private String rentid;
private String pickupaddress;
private String feedback1;

public  ReturnReq()
{}    
public ReturnReq(String pickupaddress, String feedback1) {
        this.pickupaddress = pickupaddress;
        this.feedback1 = feedback1;
    }

    public String getRentid() {
        return rentid;
    }

    public void setRentid(String rentid) {
        this.rentid = rentid;
    }

    public String getPickupaddress() {
        return pickupaddress;
    }

    public void setPickupaddress(String pickupaddress) {
        this.pickupaddress = pickupaddress;
    }

    public String getFeedback1() {
        return feedback1;
    }

    public void setFeedback1(String feedback1) {
        this.feedback1 = feedback1;
    }
}
