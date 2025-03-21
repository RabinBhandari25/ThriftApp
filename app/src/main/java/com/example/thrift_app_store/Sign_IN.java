package com.example.thrift_app_store;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class Sign_IN extends AppCompatActivity {

    EditText email, password;
    Button signIn;
    TextView signUp_back;
    Intent intent;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        signUp_back = findViewById(R.id.back_to_signup);
        signUp_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Sign_IN.this, Sign_up.class);
                startActivity(intent);
            }
        });

        email = findViewById(R.id.signIn_email);
        password = findViewById(R.id.signIn_password);
        signIn = findViewById(R.id.btn_signIn);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_txt = email.getText().toString().trim();
                String password_txt = password.getText().toString().trim();

                if (email_txt.isEmpty() || password_txt.isEmpty()) {
                    Toast.makeText(Sign_IN.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email_txt, password_txt)
                        .addOnCompleteListener(Sign_IN.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Login successful
                                    intent = new Intent(Sign_IN.this, HomePage.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // Login failed
                                    handleLoginError(task.getException());
                                }
                            }
                        });
            }
        });
    }

    private void handleLoginError(Exception exception) {
        if (exception == null) {
            Toast.makeText(Sign_IN.this, "Login failed: Unknown error", Toast.LENGTH_LONG).show();
            return;
        }

        String errorMessage;
        if (exception instanceof FirebaseAuthInvalidUserException) {
            // No user found with this email
            errorMessage = "No account found with this email. Please sign up.";
        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            // Invalid email or password
            errorMessage = "Invalid email or password. Please try again.";
        } else {
            // Other errors (e.g., network issues)
            errorMessage = "Login failed: " + exception.getMessage();
        }

        Toast.makeText(Sign_IN.this, errorMessage, Toast.LENGTH_LONG).show();
    }
}