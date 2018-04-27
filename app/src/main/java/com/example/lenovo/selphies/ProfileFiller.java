package com.example.lenovo.selphies;

public class ProfileFiller {
    private String image, desc;

    public ProfileFiller(){

    }

    public ProfileFiller(String image, String description){
        this.image = image;
        this.desc = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
