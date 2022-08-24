package com.example.postit.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.UUID;


@Entity
public class Post implements Serializable {
    @PrimaryKey
    @NonNull
    private String id = UUID.randomUUID().toString();
    private String name;
    private String whenPosted;
    private String profileImage;
    private String imgUrl;
    private int likes;
    private String user_id;
    private String text;

    public Post(){}
    public Post(String id, String name, String text, String whenPosted, String profileImage, String imgUrl, String user_id, int likes) {
        this.id = id;
        this.name = name;
        this.text = text;
        this.whenPosted = whenPosted;
        this.profileImage = profileImage;
        this.imgUrl = imgUrl;
        this.user_id = user_id;
        this.likes = likes;
    }

    public void setId(String id) { this.id = id; }

    public void setText(String text) {
        this.text = text;
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

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getText(){
        return text;
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
