package com.example.postit.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.postit.entities.Post;

import java.util.List;

@Dao
public interface PostDao {

    @Query("SELECT * FROM Post")
    List<Post> get();

    @Insert
    void insert(Post... posts);

    @Insert
    void insertList(List<Post> users);

    @Update
    void update(Post... posts);

    @Delete
    void delete(Post... posts);

    @Query("DELETE FROM Post")
    void clear();
}