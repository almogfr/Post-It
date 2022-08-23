package com.example.postit.repositories;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.postit.MyApplication;
import com.example.postit.R;
import com.example.postit.data.LocalDatabase;
import com.example.postit.data.PostDao;
import com.example.postit.entities.Post;
import com.example.postit.webservices.GetPostsTask;
import com.example.postit.webservices.PostAPI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.example.postit.FirestoreUtils.getFirestore;

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