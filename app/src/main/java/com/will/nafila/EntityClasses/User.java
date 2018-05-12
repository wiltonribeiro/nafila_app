package com.will.nafila.EntityClasses;

import java.util.HashMap;

/**
 * Created by willrcneto on 28/03/18.
 */

public class User {
    private String name, email, userId, userKey, imageUrl, country;
    private int points;
    private String lastSeenNotification;
    private HashMap<String,Object> followers;
    private HashMap<String,Object> following;
    private HashMap<String,Object> followersAlreadySeen;

    public User() {

    }

    public String getLastSeenNotification() {
        return lastSeenNotification;
    }

    public void setLastSeenNotification(String lastSeenNotification) {
        this.lastSeenNotification = lastSeenNotification;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public HashMap<String, Object> getFollowers() {
        return followers;
    }

    public HashMap<String, Object> getFollowing() {
        return following;
    }

    public HashMap<String, Object> getFollowersAlreadySeen() {
        return followersAlreadySeen;
    }

    public void setFollowers(HashMap<String, Object> followers) {
        this.followers = followers;
    }

    public void setFollowing(HashMap<String, Object> following) {
        this.following = following;
    }

    public void setFollowersAlreadySeen(HashMap<String, Object> followersAlreadySeen) {
        this.followersAlreadySeen = followersAlreadySeen;
    }

}
