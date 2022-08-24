package com.example.postit;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import com.example.postit.entities.Post;
import com.example.postit.repositories.PostsRepository;
import com.example.postit.viewmodels.PostsViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class CreatePostActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1;
    private static final int GALLERY_REQUEST = 2;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int MY_STORAGE_READ_PERMISSION_CODE = 101;
    private static final int MY_STORAGE_WRITE_PERMISSION_CODE = 102;
    private Uri mImageUri;
    private String ImageFireBaseURL;
    private Bitmap photo;
    private Button postButton;
    private ImageView imgToUplaod;
    private PostsRepository mRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_STORAGE_READ_PERMISSION_CODE);
        }
        else if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_STORAGE_WRITE_PERMISSION_CODE);
        }
        else {

            ImageButton profile = findViewById(R.id.ProfileButton);
            profile.setVisibility(View.INVISIBLE);

            TextView profileText = findViewById(R.id.settingsText);
            profileText.setVisibility(View.VISIBLE);

            ImageButton BackButton = (ImageButton) findViewById(R.id.BackButton);
            BackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }

            });
            PostsViewModel viewModel = new ViewModelProvider(this).get(PostsViewModel.class);
            postButton = findViewById(R.id.btn_postit);
//            postButton.setEnabled(false);
            postButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(photo == null){
                        Toast.makeText(getApplicationContext(), R.string.select_photo, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
                        uploadImageToFirebase();
                        viewModel.add(new Post(UUID.randomUUID().toString(), FirebaseUtils.getCurrentUser().getDisplayName(), simpleDateFormat.format(Calendar.getInstance().getTime()), ImageFireBaseURL, ImageFireBaseURL, FirebaseUtils.getCurrentUserid(), 0));

                    }
                }
            });

            imgToUplaod = findViewById(R.id.buttonAddPhoto);
            imgToUplaod.setOnClickListener(view -> {
                selectImage();
            });
        }
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(CreatePostActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Take Photo")) {
                openCam();
            }
            else if (options[item].equals("Choose from Gallery")) {
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

    private File createTemporaryFile(String part, String ext) throws Exception {
        return File.createTempFile(part, ext, getCacheDir());
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
//            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
            photo = null;
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
                imgToUplaod.setImageBitmap(photo);
//                postButton.setEnabled(true);
            }
        }
    }

    private void uploadImageToFirebase(){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        ImageFireBaseURL = FirebaseUtils.getCurrentUserid() + "/" + System.currentTimeMillis();
        UploadTask uploadTask = FirebaseStorage.getInstance().getReference().child(ImageFireBaseURL).putBytes(stream.toByteArray());
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
