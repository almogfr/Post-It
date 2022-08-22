package com.example.postit.webservices;

import com.example.postit.R;
import com.example.postit.entities.Post;

import java.util.List;
import com.example.postit.R;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitAPI {
    @GET("Post?key=AIzaSyAt44mfkn_nYds5amtsOdKAIimq_iGUIX0")
    Call<List<Post>> getPosts();

    @Headers("key: " + R.string.api_key)
    @POST("Post")
    Call<Void> createPost(@Body Post post);

    @Headers("key: " + R.string.api_key)
    @DELETE("Post/{id}")
    Call<Void> deletePost(@Path("id") String id);
}