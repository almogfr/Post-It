package com.example.postit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.postit.entities.Post;

import java.io.Serializable;

public class ImageClickedActivity extends AppCompatActivity implements Serializable {

    ImageView selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_clicked);

        Intent mIntent = getIntent();
        Post postItems = (Post) mIntent.getSerializableExtra("UniqueKey");

        TextView postName = findViewById(R.id.feed_post_name);
        TextView postWhen = findViewById(R.id.feed_post_when);
        ImageView feedPostProfileImg = findViewById(R.id.feed_post_profile_img);
        ImageView feedPostImg = findViewById(R.id.feed_post_img);
        TextView postLikes = findViewById(R.id.post_likes);

        postName.setText(postItems.getName());
        postWhen.setText(postItems.getWhenPosted());
        new ImageDownloader(postItems.getProfileImage(), feedPostProfileImg).execute();
        new ImageDownloader(postItems.getImgUrl(), feedPostImg).execute();
//        postLikes.setText(postItems.getLikes());

        ImageButton prfilePageButton = (ImageButton) findViewById(R.id.ProfileButton);
        prfilePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ImageClickedActivity.this, ProfilePageActivity.class);
                startActivity(intent);
            }
        });
        ImageButton BackButton = (ImageButton) findViewById(R.id.BackButton);
        BackButton.setOnClickListener(view -> finish());
    }
}