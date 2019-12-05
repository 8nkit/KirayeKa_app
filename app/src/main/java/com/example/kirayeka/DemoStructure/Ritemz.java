package com.example.kirayeka.DemoStructure;


public class Ritemz {
    private String Name, Image, Details, rent, Off5, mcId, ItemId, AvailabilityFlag;

    public Ritemz() {
    }

    public Ritemz(String name, String image, String details, String rent, String off5, String McId, String itemId, String availabilityFlag) {
        Name = name;
        Image = image;
        Details = details;
        this.rent = rent;
        Off5 = off5;
        mcId = McId;
        ItemId = itemId;
        AvailabilityFlag = availabilityFlag;
    }



    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
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

    public String getmcId() {
        return mcId;
    }

    public void setmcId(String McId) {
        mcId = McId;
    }

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }

    public String getAvailabilityFlag() {
        return AvailabilityFlag;
    }

    public void setAvailabilityFlag(String availabilityFlag) {
        AvailabilityFlag = availabilityFlag;
    }
}
