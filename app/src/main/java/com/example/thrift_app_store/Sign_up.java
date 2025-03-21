package com.example.thrift_app_store;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

public class Sign_up extends AppCompatActivity {


    EditText email, user, password_first, password_second;
    TextView moveToLogin;
    Button signUp;
    Intent intent;
    String emailAddress, new_password, confirm_password;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        email = findViewById(R.id.signUp_email);
        user = findViewById(R.id.signUp_username);
        password_first = findViewById(R.id.signUp_password);
        password_second = findViewById(R.id.signUp_confirm_password);

        moveToLogin = findViewById(R.id.text_already_have_id);
        signUp = findViewById(R.id.btn_signup);

        auth = FirebaseAuth.getInstance();
        moveToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Sign_up.this, Sign_IN.class);
                startActivity(intent);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isready = true;
                emailAddress = email.getText().toString().trim();
                new_password = password_first.getText().toString().trim();
                confirm_password = password_second.getText().toString().trim();

                if (emailAddress.isEmpty()) {
                    email.setError("Please enter an email");
                    isready =false;
                }

                if (new_password.isEmpty()) {
                    password_first.setError("Please enter a password");
                    isready =false;

                }

                if (confirm_password.isEmpty()) {
                    password_second.setError("Please confirm your password");
                    isready =false;

                }

                if (!new_password.equals(confirm_password)) {
                    password_second.setError("Passwords do not match");
                    isready =false;

                }

                if(isready){
                    registerUser(emailAddress,confirm_password);
                }


            }
        });

    }

    public void registerUser(String email, String password){

        Log.w("test",email);
        Log.w("test",password);


        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            String user_name = user.getText().toString();
                            updateUsername(user_name);

                            Toast.makeText(Sign_up.this, "Sign-up complete", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(Sign_up.this, "Sign-up unsuccessful", Toast.LENGTH_LONG).show();

                        }
                    }
                });

    }

    public void updateUsername(String newUsername) {
        // Validate input
        if (newUsername == null || newUsername.trim().isEmpty()) {
            Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newUsername.length() < 3 || newUsername.length() > 20) {
            Toast.makeText(this, "Username must be 3-20 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "No user signed in", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update Auth profile
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(newUsername)
                .build();

        user.updateProfile(profileUpdates)
                .addOnSuccessListener(unused -> {
                    FirebaseFirestore.getInstance()
                            .collection("users")
                            .document(user.getUid())
                            .update("username", newUsername)
                            .addOnSuccessListener(unused1 -> {
                                Toast.makeText(this, "Username updated", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}