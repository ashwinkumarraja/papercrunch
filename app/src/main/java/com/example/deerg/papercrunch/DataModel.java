package com.example.deerg.papercrunch;


public class DataModel {


    String name;
    String version;
    int id_;
    int image;

    String name_second;

    public DataModel(String name, String version, int id_, int image, String name_second) {
        this.name = name;
        this.version = version;
        this.id_ = id_;
        this.image=image;
        this.name_second = name_second;
    }


    public String getName() {
        return name;
    }


    public String getVersion() {
        return version;
    }

    public int getImage() {
        return image;
    }

    public int getId() {
        return id_;
    }

    public String getName_second() {
        return name_second;
    }
}