package com.example.postit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.postit.entities.Post;

import java.io.Serializable;

public class ImageClickedActivity extends AppCompatActivity implements Serializable {

    ImageView selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_clicked);

        Intent mIntent = getIntent();
        Post postItem = (Post) mIntent.getSerializableExtra("UniqueKey");

        TextView postName = findViewById(R.id.feed_post_name);
        TextView postWhen = findViewById(R.id.feed_post_when);
        ImageView feedPostProfileImg = findViewById(R.id.feed_post_profile_img);
        ImageView feedPostImg = findViewById(R.id.feed_post_img);
        TextView postLikes = findViewById(R.id.post_likes);
        TextView text = findViewById(R.id.comment_text);

        postName.setText(postItem.getName());
        postWhen.setText(postItem.getWhenPosted());
        text.setText(postItem.getText());
        Glide.with(getApplicationContext())
                .load(FirebaseUtils.getStorageRef().child(postItem.getProfileImage()))
                .into(feedPostProfileImg);
        Glide.with(getApplicationContext())
                .load(FirebaseUtils.getStorageRef().child(postItem.getImgUrl()))
                .into(feedPostImg);
        postLikes.setText(postItem.getLikes() + " Likes");

        ImageButton profilePageButton = findViewById(R.id.ProfileButton);
        profilePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ImageClickedActivity.this, ProfilePageActivity.class);
                startActivity(intent);
            }
        });
        ImageButton BackButton = findViewById(R.id.BackButton);
        BackButton.setOnClickListener(view -> finish());
    }
}