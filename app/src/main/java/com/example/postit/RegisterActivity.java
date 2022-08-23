package com.example.postit;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailTextView, passwordTextView, firstnameTextView, lastnameTextView, addressTextView, mobileTextView, dateofbirthTextView;
    private Button Btn;
    private ProgressBar progressbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        emailTextView = findViewById(R.id.username);
        passwordTextView = findViewById(R.id.password);
        firstnameTextView = findViewById(R.id.firstname);
        lastnameTextView = findViewById(R.id.lastname);
        addressTextView = findViewById(R.id.address);
        mobileTextView = findViewById(R.id.mobile);
        dateofbirthTextView = findViewById(R.id.dateofbirth);
        Btn = findViewById(R.id.register);
        progressbar = findViewById(R.id.loading);

        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                registerNewUser();
            }
        });
    }

    private void registerNewUser()
    {

        // show the visibility of progress bar to show loading
        progressbar.setVisibility(View.VISIBLE);

        // Take the value of two edit texts in Strings
        String email, password, firstname, lastname, address, mobile, dateofbirth;
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();
        firstname = firstnameTextView.getText().toString();
        lastname = lastnameTextView.getText().toString();
        address = addressTextView.getText().toString();
        mobile = mobileTextView.getText().toString();
        dateofbirth = dateofbirthTextView.getText().toString();

        // Validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter email!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter password!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (TextUtils.isEmpty(firstname)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter first name!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (TextUtils.isEmpty(lastname)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter last name!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // create new user or register new user
        mAuth
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),
                                    "Registration successful!",
                                    Toast.LENGTH_LONG)
                                    .show();

                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("FirstName", firstname);
                            hashMap.put("LastName", lastname);
                            hashMap.put("MobileNumber", mobile);
                            hashMap.put("Address", address);
                            hashMap.put("DateOfBirth", dateofbirth);

                            FirebaseFirestore.getInstance().collection("users")
                                    .document(FirebaseUtils.getCurrentemail())
                                    .set(hashMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(RegisterActivity.this, "Data Saved successful", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RegisterActivity.this, ""+ e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });


                            // hide the progress bar
                            progressbar.setVisibility(View.GONE);

                            // if the user created intent to login activity
                            Intent intent
                                    = new Intent(RegisterActivity.this,
                                    MainActivity.class);
                            startActivity(intent);

                        }
                        else {
                            FirebaseAuthException e = (FirebaseAuthException)task.getException();
                            // Registration failed
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Registration failed!!"
                                            + " Please try again later"+"\n Failed Registration: "+e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();

                            // hide the progress bar
                            progressbar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}