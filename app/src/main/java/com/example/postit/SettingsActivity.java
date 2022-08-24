package com.example.postit;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ImageButton profile = findViewById(R.id.ProfileButton);
        profile.setVisibility(View.VISIBLE);

        TextView profileText = findViewById(R.id.settingsText);
        profileText.setVisibility(View.VISIBLE);

        ImageButton BackButton = (ImageButton) findViewById(R.id.BackButton);
        BackButton.setOnClickListener(view -> finish());

        EditText passwordEt = findViewById(R.id.et_current_password);
        EditText passwordNew = findViewById(R.id.et_new_password);

        Button updatePasswordBtn = findViewById(R.id.btn_change_password);

        updatePasswordBtn.setOnClickListener(v -> {
            if (TextUtils.isEmpty(passwordEt.getText().toString())) {
                Toast.makeText(SettingsActivity.this, "Enter your current password", Toast.LENGTH_SHORT).show();
                return;
            }
            if (passwordNew.getText().toString().length() < 6){
                Toast.makeText(SettingsActivity.this, "Password length must be at least 6 chars", Toast.LENGTH_SHORT).show();
                return;
            }
                updatePassword(passwordEt.getText().toString(), passwordNew.getText().toString());
        });
    }

    public void updatePassword(String oldPassword, String newPassword){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);
        user.reauthenticate(authCredential)
                .addOnSuccessListener(unused -> user.updatePassword(newPassword)
                        .addOnSuccessListener(unused1 -> {
                            Toast.makeText(SettingsActivity.this, "Password updated", Toast.LENGTH_SHORT).show();
                            this.finish();
                        })
                        .addOnFailureListener(e -> Toast.makeText(SettingsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show()))
                .addOnFailureListener(e -> Toast.makeText(SettingsActivity.this, ""+ e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
