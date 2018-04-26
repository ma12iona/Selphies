package com.example.lenovo.selphies;

public class ProfileFiller {
    private String image, description;

    public ProfileFiller(){

    }

    public ProfileFiller(String image, String description){
        this.image = image;
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.image = description;
    }
}
