package com.example.kirayeka.DemoStructure;

public class Wishlist {
    private  String ItemId,ItemName,ItemRent,ItemMenuId,ItemImage,ItemOff5,ItemDetails,PhoneNo;

    public Wishlist(String itemId, String itemName, String itemRent, String itemMenuId, String itemImage, String itemOff5, String itemDetails, String userPhone) {
        ItemId = itemId;
        ItemName = itemName;
        ItemRent = itemRent;
        ItemMenuId = itemMenuId;
        ItemImage = itemImage;
        ItemOff5 = itemOff5;
        ItemDetails = itemDetails;
        PhoneNo = userPhone;
    }

    public Wishlist() {

    }

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemRent() {
        return ItemRent;
    }

    public void setItemRent(String itemRent) {
        ItemRent = itemRent;
    }

    public String getItemMenuId() {
        return ItemMenuId;
    }

    public void setItemMenuId(String itemMenuId) {
        ItemMenuId = itemMenuId;
    }

    public String getItemImage() {
        return ItemImage;
    }

    public void setItemImage(String itemImage) {
        ItemImage = itemImage;
    }

    public String getItemOff5() {
        return ItemOff5;
    }

    public void setItemOff5(String itemOff5) {
        ItemOff5 = itemOff5;
    }

    public String getItemDetails() {
        return ItemDetails;
    }

    public void setItemDetails(String itemDetails) {
        ItemDetails = itemDetails;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String userPhone) {
        PhoneNo = userPhone;
    }
}
