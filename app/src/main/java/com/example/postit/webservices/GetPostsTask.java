package com.example.postit.webservices;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.postit.data.PostDao;
import com.example.postit.entities.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import static android.content.ContentValues.TAG;
import static com.example.postit.FirestoreUtils.getFirestore;

public class GetPostsTask extends AsyncTask<Void, Void, Void>
{
    private MutableLiveData<List<Post>> postListData;
    private PostDao dao;

    public GetPostsTask(MutableLiveData<List<Post>> postListData, PostDao dao) {
        this.postListData = postListData;
        this.dao = dao;
    }

    @Override
    protected Void doInBackground(Void... urls)
    {
        getFirestore().collection("Post").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if (documentSnapshots.isEmpty()) {
                            Log.d(TAG, "onSuccess: LIST EMPTY");
                            return;
                        } else {
                            // Convert the whole Query Snapshot to a list
                            // of objects directly! No need to fetch each
                            // document.
                            List<Post> posts = documentSnapshots.toObjects(Post.class);

                            // Add all to your list
                            new Thread(() -> {
                                dao.clear();
                                dao.insertList(posts);
                                postListData.postValue(dao.get());
                            }).start();
                            Log.d(TAG, "onSuccess: " + posts);
                        }
                    }})
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