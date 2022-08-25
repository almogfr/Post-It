package com.example.postit.webservices;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.postit.data.PostDao;
import com.example.postit.entities.Post;
import com.google.android.gms.tasks.OnFailureListener;

import java.util.List;

import static android.content.ContentValues.TAG;
import static com.example.postit.FirestoreUtils.getFirestore;

public class DeletePostsTask extends AsyncTask<Void, Void, Void>
{
    private MutableLiveData<List<Post>> postListData;
    private PostDao dao;
    private Post post;

    public DeletePostsTask(MutableLiveData<List<Post>> postListData, PostDao dao, Post post) {
        this.postListData = postListData;
        this.dao = dao;
        this.post = post;
    }

    @Override
    protected Void doInBackground(Void... urls)
    {
        getFirestore().collection("Post").document(post.getId()).delete()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(getApplicationContext(), "Error getting data!!!", Toast.LENGTH_LONG).show();
                        Log.e(TAG, "onFailure: " + e);
                    }
                });
        return null;
    }
}