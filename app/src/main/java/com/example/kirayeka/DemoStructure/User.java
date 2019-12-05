package com.example.kirayeka.DemoStructure;

public class User {
    private String Name;
    private String uid;
    private String Password;
    private String Phone;
    private String SQAns;

    public User() {
    }

    public User(String name,String UID, String password,String sqans) {
        Name = name;
        Password = password;
        SQAns=sqans;
        uid=UID;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPhone() {

        return Phone;
    }
public String getSQAns()
{
    return SQAns;
}
public void setSQAns(String sqanss){
        SQAns=sqanss;
}
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
