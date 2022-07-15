package com.example.postit;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ImageClickedActivity extends AppCompatActivity {

    ImageView selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_clicked);
        selectedImage = (ImageView) findViewById(R.id.selectedImage);
        Bundle extras = getIntent().getExtras();
        String temp = extras.getString("image_item", "");
        new ImageDownloader(temp, selectedImage).execute();
//        selectedImage.setImageResource(temp);
    }
}