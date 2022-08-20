package com.example.postit.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class Post {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String whenPosted;
    private String profileImage;
    private String imgUrl;
    private int likes;
    private String user_id;

    public Post(String name, String whenPosted, String profileImage, String imgUrl, String user_id) {
        this.name = name;
        this.whenPosted = whenPosted;
        this.profileImage = profileImage;
        this.imgUrl = imgUrl;
        this.user_id = user_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setWhenPosted(String whenPosted) {
        this.whenPosted = whenPosted;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getWhenPosted() {
        return whenPosted;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public int getLikes() {
        return likes;
    }

    public String getUser_id() {
        return user_id;
    }

    public void select() {
        name = name + " selected";
    }
}
