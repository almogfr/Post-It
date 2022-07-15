package com.example.postit;

public class Post {
    String name;
    String whenPosted;
    int profileImage;
    int img;

    public Post(String name, String whenPosted, int profileImage, int img) {
        this.name = name;
        this.whenPosted = whenPosted;
        this.profileImage = profileImage;
        this.img = img;
    }

    public String getName() { return name; }

    public String getWhenPosted() { return whenPosted; }

    public int getProfileImage() { return profileImage; }

    public int getImg() { return img; }

    public void select() { name = name + " selected"; }
}
