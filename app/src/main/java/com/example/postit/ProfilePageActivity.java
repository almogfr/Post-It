package com.example.postit;

import static com.example.postit.FirestoreUtils.getFirestore;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.postit.entities.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

public class ProfilePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        TextView firstnameTextView = findViewById(R.id.FirstnameProfilePage);
        TextView addressTextView = findViewById(R.id.AddressProfilePage);
        TextView mobileTextView = findViewById(R.id.mobileProfilePage);
        TextView dateofbirthTextView = findViewById(R.id.DOBProfilePage);
        TextView emailTextView = findViewById(R.id.emailProfilePage);
        emailTextView.setText(FirebaseUtils.getCurrentemail());

        ImageButton profile = findViewById(R.id.ProfileButton);
        profile.setVisibility(View.INVISIBLE);

        TextView profileText = findViewById(R.id.profileText);
        profileText.setVisibility(View.VISIBLE);

        ImageButton BackButton = (ImageButton) findViewById(R.id.BackButton);
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Button logoutBtn=findViewById(R.id.logout);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUtils.logout(ProfilePageActivity.this);
            }
        });

        getFirestore().collection("users").document(FirebaseUtils.getCurrentemail()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (!documentSnapshot.exists()) {
                            return;
                        } else {
                            Users user = documentSnapshot.toObject(Users.class);
                            dateofbirthTextView.setText(user.getDateOfBirth());
                            addressTextView.setText(user.getAddress());
                            mobileTextView.setText(user.getMobileNumber());
                            firstnameTextView.setText(user.getFirstName());
                    }
                }})
            .addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(getApplicationContext(), "Error getting data!!!", Toast.LENGTH_LONG).show();
           // Log.e(TAG, "onFailure: " + e);
        }
    });

    }
}