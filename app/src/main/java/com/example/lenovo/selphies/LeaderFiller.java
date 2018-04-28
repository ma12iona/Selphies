package com.example.lenovo.selphies;

public class LeaderFiller {
    private String username, description, profile, userId;
    private Long endorse;

    public LeaderFiller(){

    }

    public LeaderFiller(String username, String description, String profile, String userId, Long endorse){
        this.username = username;
        this.description = description;
        this.profile = profile;
        this.userId = userId;
        this.endorse = endorse;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDesc(String desc) {
        this.description = description;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getEndorse() {
        return endorse;
    }

    public void setEndorse(Long endorse) {
        this.endorse = endorse;
    }
}
