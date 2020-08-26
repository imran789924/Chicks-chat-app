package com.example.whatsappfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText eUserEditText, ePassEditText, eMailEditText;
    Button eButtonRegister;

    FirebaseAuth auth;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        eUserEditText = findViewById(R.id.userEditText);
        ePassEditText = findViewById(R.id.passEditText);
        eMailEditText = findViewById(R.id.mailEditText);
        eButtonRegister = findViewById(R.id.buttonRegister);

        auth = FirebaseAuth.getInstance();


        //event listener for button
        eButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = eUserEditText.getText().toString();
                String email = eMailEditText.getText().toString();
                String password = ePassEditText.getText().toString();

                if(TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    Toast.makeText(RegisterActivity.this, "All fields were not filled", Toast.LENGTH_SHORT).show();
                } else {
                    Register(username, email, password);
                }
            }
        });

    }



    private void Register(final String userName, String email, String password){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userId = firebaseUser.getUid();

                            ref = FirebaseDatabase.getInstance()
                                    .getReference("MyUsers")
                                    .child(userId);

                            //HashMaps here
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userId);
                            hashMap.put("username", userName);
                            hashMap.put("imageURL", "default");

                            // got to the main activity after succefully registered

                            ref.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });

                        }else{
                            Toast.makeText(RegisterActivity.this, "Invalid Email or Password", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }



}