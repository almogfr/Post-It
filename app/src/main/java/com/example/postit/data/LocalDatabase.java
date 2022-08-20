package com.example.postit.data;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.postit.MyApplication;
import com.example.postit.R;
import com.example.postit.entities.Post;

@Database(entities = {Post.class}, version = 1)
public abstract class LocalDatabase extends RoomDatabase {

    private static LocalDatabase instance;

    public abstract PostDao postDao();

    public static LocalDatabase getInstance() {
        if (instance == null) {
            instance = Room.databaseBuilder(MyApplication.context.getApplicationContext(),
                                            LocalDatabase.class,
                                            MyApplication.context.getString(R.string.DbName)).fallbackToDestructiveMigration().build();
        }
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }
}