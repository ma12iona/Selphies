package com.example.lenovo.selphies;

public class HomeFiller {
    private String username, desc, image, postId, userId;
    private Long endorse;

    public HomeFiller(){

    }

    public HomeFiller(String username, String desc, String image, String postId, String userId, Long endorse){
        this.username = username;
        this.desc = desc;
        this.image = image;
        this.postId = postId;
        this.userId = userId;
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

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
