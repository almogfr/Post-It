package com.example.postit;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

        ImageButton mapsPageButton = (ImageButton) findViewById(R.id.mapsButton);
        mapsPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
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

    private void openCam(){
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        } else {
            File photo = null;
            try {
                // place where to store camera taken picture
                photo = createTemporaryFile("picture", ".png");
                photo.delete();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Please check SD card! Image shot is impossible!", Toast.LENGTH_SHORT).show();
            }
            mImageUri = FileProvider.getUriForFile(getBaseContext(), getBaseContext().getApplicationContext().getPackageName() + ".provider", photo);
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
            try {
                startActivityForResult(takePictureIntent, 1);
            } catch (ActivityNotFoundException e) {
                // display error state to the user
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
//            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Bitmap photo = null;
            try {
                photo = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageUri);
            } catch (IOException e) {
                Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            File file = new File(directory, System.currentTimeMillis() + ".PNG");
            if (!file.exists()) {
                Log.d("path", file.toString());
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file);
                    photo.compress(Bitmap.CompressFormat.JPEG, 50, fos);
                    fos.flush();
                    fos.close();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                UploadTask uploadTask = FirebaseStorage.getInstance().getReference().child(userUid + "/" + System.currentTimeMillis()).putBytes(stream.toByteArray());
                uploadTask
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(getApplicationContext(), "Image uploaded", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Fail to upload", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }

    private File createTemporaryFile(String part, String ext) throws Exception {
        return File.createTempFile(part, ext, getCacheDir());
    }
}