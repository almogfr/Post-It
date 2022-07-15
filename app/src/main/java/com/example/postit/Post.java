package com.example.postit;

public class Post {
    String name;
    String whenPosted;
    String profileImage;
    String imgUrl;

    public Post(String name, String whenPosted, String profileImage, String imgUrl) {
        this.name = name;
        this.whenPosted = whenPosted;
        this.profileImage = profileImage;
        this.imgUrl = imgUrl;
    }

    public String getName() { return name; }

    public String getWhenPosted() { return whenPosted; }

    public String getProfileImage() { return profileImage; }

    public String getImgUrl() { return imgUrl; }

    public void select() { name = name + " selected"; }
}
