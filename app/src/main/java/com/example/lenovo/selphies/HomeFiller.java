package com.example.lenovo.selphies;

public class HomeFiller {
    private String username, desc, image;
    private Long endorse;

    public HomeFiller(){

    }

    public HomeFiller(String username, String desc, String image, Long endorse){
        this.username = username;
        this.desc = desc;
        this.image = image;
        this.endorse = endorse;
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

    public Long getEndorse() {
        return endorse;
    }

    public void setEndorse(Long endorse) {
        this.endorse = endorse;
    }
}
