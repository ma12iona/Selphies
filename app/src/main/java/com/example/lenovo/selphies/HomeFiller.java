package com.example.lenovo.selphies;

public class HomeFiller {
    private String username, desc, image;

    public HomeFiller(){

    }

    public HomeFiller(String username, String desc, String image, int endorse){
        this.username = username;
        this.desc = desc;
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }

    public String getImage() {
        return image;
    }

    public String getUsername() {
        return username;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
