package com.example.lenovo.selphies;

public class ProfileFiller {
    private String image, desc, postId;

    public ProfileFiller(){

    }

    public ProfileFiller(String image, String description, String postId){
        this.image = image;
        this.desc = description;
        this.postId = postId;
    }

    public String getImage() {
        return image;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
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
