package com.example.kirayeka.DemoStructure;

import java.util.List;


public class dataSource {
    private String phone;
    private String name;
    private String address;
    private String total;
    private String status;
    private List<RentReq> items;
    private boolean partial = false;

    public dataSource() {
    }

    public dataSource(String phone, String name, String address, String total, List<RentReq> items) {
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.total = total;
        this.items = items;
        this.status="0"; //Default is 0, 0:Placed, 1: Shipping, 2: Shipped
    }

    public boolean isPartial() {
        return partial;
    }

    public void setPartial(boolean partial) {
        this.partial = partial;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<RentReq> getItems() {
        return items;
    }

    public void setItems(List<RentReq> items) {
        this.items = items;
    }
}
