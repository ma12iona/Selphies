package com.example.lenovo.selphies;

/**
 * This class is use as a base for recycle view in profile page
 */
public class ProfileFiller {
    private String image, desc, postId;
    private Long endorse;

    public ProfileFiller(){

    }

    public ProfileFiller(String image, String description, String postId, Long endorse){
        this.image = image;
        this.desc = description;
        this.postId = postId;
        this.endorse = endorse;

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

    public Long getEndorse() {
        return endorse;
    }

    public void setEndorse(Long endorse) {
        this.endorse = endorse;
    }
}
