package com.example.postit;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseUtils {
    public static void FirebaseRegister(String email, String password, ProgressBar progressbar, Activity context){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();


        // Validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(MyApplication.context.getApplicationContext(),
                    "Please enter email",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(MyApplication.context.getApplicationContext(),
                    "Please enter password",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }

        progressbar.setVisibility(View.VISIBLE);

        // create new user or register new user
        mAuth
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful()) {
                            Toast.makeText(MyApplication.context.getApplicationContext(),
                                    "Registration successful!",
                                    Toast.LENGTH_LONG)
                                    .show();

                            // hide the progress bar
                            progressbar.setVisibility(View.GONE);

                            // if the user created intent to login activity
                            Intent intent
                                    = new Intent(MyApplication.context,
                                    MainActivity.class);
                            MyApplication.context.startActivity(intent);
                        }
                        else {
                            FirebaseAuthException e = (FirebaseAuthException)task.getException();
                            // Registration failed
                            Toast.makeText(
                                    MyApplication.context.getApplicationContext(),
                                    "Registration failed"
                                            + " Please try again later"+"\n Failed Registration: "+e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();

                            // hide the progress bar
                            progressbar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    public static void FirebaseLogin(String email, String password, ProgressBar progressbar, Activity context){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(MyApplication.context.getApplicationContext(),
                    "Please enter email!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(MyApplication.context.getApplicationContext(),
                    "Please enter password",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }

        progressbar.setVisibility(View.VISIBLE);

        // signin existing user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(
                                    @NonNull Task<AuthResult> task)
                            {
                                if (task.isSuccessful()) {
                                    Toast.makeText(context.getApplicationContext(),
                                            "Login successful",
                                            Toast.LENGTH_LONG)
                                            .show();

                                    // hide the progress bar
                                    progressbar.setVisibility(View.GONE);

                                    // if sign-in is successful
                                    // intent to home activity
                                    Intent intent
                                            = new Intent(context,
                                            MainActivity.class);
                                    context.startActivity(intent);
                                    context.finish();
                                }

                                else {

                                    // sign-in failed
                                    Toast.makeText(context.getApplicationContext(),
                                            "Login failed",
                                            Toast.LENGTH_LONG)
                                            .show();

                                    // hide the progress bar
                                    progressbar.setVisibility(View.GONE);
                                }
                            }
                        });
    }

    public static boolean isSignedIn(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return (currentUser != null);
    }

    public static FirebaseUser getCurrentUser() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if(isSignedIn()){
            return mAuth.getCurrentUser();
        }
        return null;
    }

    public static String getCurrentUserid() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if(isSignedIn()){
            return mAuth.getCurrentUser().getUid();
        }
        return null;
    }

    public static StorageReference getStorageRef(){
        FirebaseStorage storage  = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        return storageRef;
    }

    public static String getCurrentemail() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if(isSignedIn()){
            return mAuth.getCurrentUser().getEmail();
        }
        return null;
    }

    public static void logout(Activity context) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        Intent intent
                = new Intent(context,
                LoginActivity.class);
        context.startActivity(intent);
        context.finish();
    }

}