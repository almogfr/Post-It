package com.example.postit;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.postit.entities.Post;
import com.example.postit.viewmodels.PostsViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private static final int CAMERA_REQUEST = 1;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private ArrayList<String> Images = new ArrayList<>();
    private ProgressBar progressBar;
    private List<Post> posts;
    private StorageReference storageReference;
    private DatabaseReference url;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private Uri mImageUri;
    private FeedAdapter feedAdapter;
    private PostsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        super.onCreate(savedInstanceState);
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences sharedPreferences = getSharedPreferences("myfile", MODE_PRIVATE);
        if (!sharedPreferences.getBoolean("OnboardingFinishFlag", false)) {
            Intent intent = new Intent(MainActivity.this, OnboardingActivity.class);
            startActivity(intent);
        }
        setContentView(R.layout.activity_main);
//        progressBar = findViewById(R.id.progress);
//        progressBar.setVisibility(View.VISIBLE);

        ImageButton image = findViewById(R.id.BackButton);
        image.setVisibility(View.GONE);

        ImageButton settings = findViewById(R.id.SettingsButton);
        settings.setVisibility(View.VISIBLE);

        TextView Appname = findViewById(R.id.appname);
        Appname.setVisibility(View.VISIBLE);
        url = database.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference().child("/" + FirebaseUtils.getCurrentUserid() + "/");


        viewModel = new ViewModelProvider(this).get(PostsViewModel.class);
//        viewModel.add(new Post(UUID.randomUUID().toString(), "almog", "11111", "https://firebasestorage.googleapis.com/v0/b/postit-cfab0.appspot.com/o/dkQj8uq7Bug061psB4fuWg9PksS2%2F1657404674448?alt=media&token=2ddd1cc5-abd1-4b69-bcab-7031a20443cc", "https://firebasestorage.googleapis.com/v0/b/postit-cfab0.appspot.com/o/dkQj8uq7Bug061psB4fuWg9PksS2%2F1657404674448?alt=media&token=2ddd1cc5-abd1-4b69-bcab-7031a20443cc", "321231231", 0));
        RecyclerView lstFeed = (RecyclerView) findViewById(R.id.lstFeed);

//        posts = generatePosts();
        feedAdapter = new FeedAdapter(this);
        lstFeed.setAdapter(feedAdapter);
        lstFeed.setLayoutManager(new LinearLayoutManager(this));


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floating_action_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        SwipeRefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(() -> {
            viewModel.reload();
        });

        viewModel.get().observe(this, posts -> {
            feedAdapter.setPosts(posts);
            refreshLayout.setRefreshing(false);
        });


        ImageButton prfilePageButton = (ImageButton) findViewById(R.id.ProfileButton);
        prfilePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfilePageActivity.class);
                startActivity(intent);
            }
        });
        ImageButton settingsPageButton = (ImageButton) findViewById(R.id.SettingsButton);
        settingsPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void selectImage() {
        final CharSequence[] options = {"Post It with image", "Post It with video", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Upload Post");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Post It with image")) {
//                openCam();
                Intent intent = new Intent(MainActivity.this, CreatePostActivity.class);
                startActivity(intent);
            }
            else if (options[item].equals("Post It with video")) {
                openGallery();
            }
            else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void openGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }
}