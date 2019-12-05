package com.example.kirayeka.DemoStructure;
public class Grade {
    private String Name;
    private String Image;

    public Grade() {
    }

    public Grade(String name, String image) {
        Name = name;
        Image = image;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
