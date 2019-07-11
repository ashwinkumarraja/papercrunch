package com.example.deerg.papercrunch;


public class DataModel {


    String name;
    int id_;
    int image;
    int image2;
    String name_second;

    public DataModel(String name,  int id_, int image,int image2, String name_second) {
        this.name = name;
        this.id_ = id_;
        this.image=image;
        this.image2=image2;
        this.name_second = name_second;
    }


    public String getName() {
        return name;
    }




    public int getImage() {
        return image;
    }
    public int getImage2() {
        return image2;
    }

    public int getId() {
        return id_;
    }

    public String getName_second() {
        return name_second;
    }
}