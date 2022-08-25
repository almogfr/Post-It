package com.example.postit.webservices;

import androidx.lifecycle.MutableLiveData;

import com.example.postit.MyApplication;
import com.example.postit.R;
import com.example.postit.data.PostDao;
import com.example.postit.entities.Post;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostAPI
{
    Retrofit retrofit;
    private MutableLiveData<List<Post>> postListData;
    private PostDao dao;
    RetrofitAPI retrofitAPI;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public PostAPI(MutableLiveData<List<Post>> postListData, PostDao dao) {
        this.postListData = postListData;
        this.dao = dao;

        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.PostsUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitAPI = retrofit.create(RetrofitAPI.class);
    }

    public void add(Post post)
    {
        new Thread(() -> {
            dao.insert(post);
            postListData.postValue(dao.get());

            new AddPostsTask(postListData, dao, post).execute();
        }).start();
    }

    public void delete(Post post)
    {
        dao.delete(post);
        postListData.postValue(dao.get());

        Call<Void> call = retrofitAPI.deletePost(post.getId());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {}

            @Override
            public void onFailure(Call<Void> call, Throwable t) {}
        });
    }

    public void get()
    {
        new GetPostsTask(postListData, dao).execute();
    }



}