package com.example.postit.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.postit.data.LocalDatabase;
import com.example.postit.data.PostDao;
import com.example.postit.entities.Post;
import com.example.postit.webservices.GetPostsTask;
import com.example.postit.webservices.PostAPI;
import java.util.LinkedList;
import java.util.List;


public class PostsRepository {
    private PostDao dao;
    private PostListData postListData;
    private PostAPI api;

    public PostsRepository() {
        LocalDatabase db = LocalDatabase.getInstance();
        dao = db.postDao();
        postListData = new PostListData();
        api = new PostAPI(postListData, dao);
    }


    class PostListData extends MutableLiveData<List<Post>> {

        public PostListData() {
            super();
            setValue(new LinkedList<Post>());
        }

        @Override
        protected void onActive() {
            super.onActive();

            new Thread(() -> {
                postListData.postValue(dao.get());
                new GetPostsTask(postListData, dao).execute();
            }).start();
        }
    }

    public LiveData<List<Post>> getAll() {
        return postListData;
    }

    public void add(final Post post) {
        api.add(post);
    }

    public void delete(final Post post) {
        api.delete(post);
    }

    public void reload() {
        api.get();
    }

}