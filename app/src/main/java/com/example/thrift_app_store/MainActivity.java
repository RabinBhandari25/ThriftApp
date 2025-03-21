package com.example.thrift_app_store;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {



//    private FirebaseAuth mAuth;
//    Intent intent;
//    CredentialManager credentialManager;
//    private final String TAG = "GOOGLE_AUTH";




    Button signIn,signUp;
    Intent intent;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        mAuth = FirebaseAuth.getInstance();
//        credentialManager = CredentialManager.create(getBaseContext());



        mAuth = FirebaseAuth.getInstance();

        signIn = findViewById(R.id.login_btn);

        signUp = findViewById(R.id.signup_btn);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, Sign_IN.class);
                startActivity(intent);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this,Sign_up.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            intent = new Intent(MainActivity.this,HomePage.class);
            startActivity(intent);
        }
    }







    /*
    *   Problem with google sign-in
    *   Unable to add new google account
    *   And only able to access account saved in device
    *
    * */
//    private void authenticateUser(){
//
//        // Instantiate a Google sign-in request
//        GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
//                .setFilterByAuthorizedAccounts(true)
//                .setServerClientId(getString(R.string.default_web_client_id))
//                .build();
//
//
//        // Create the Credential Manager request
//        GetCredentialRequest request = new GetCredentialRequest.Builder()
//                .addCredentialOption(googleIdOption)
//                .build();
//
//
//
//        Log.w(TAG, "2");
//
//        credentialManager.getCredentialAsync(
//                MainActivity.this, // Context
//                request, // GetCredentialRequest
//                new CancellationSignal(), // CancellationSignal (can be null if not needed)
//                Executors.newSingleThreadExecutor(), // Executor
//                new CredentialManagerCallback<>() {
//                    @Override
//                    public void onResult(GetCredentialResponse result) {
//                        Log.w(TAG, "3");
//
//                        // Handle the successful credential response
//                        handleSignIn(result.getCredential());
//                    }
//
//                    @Override
//                    public void onError(@NonNull GetCredentialException e) {
//                        Log.e(TAG, "Couldn't retrieve user's credentials: " + e.getLocalizedMessage());
//
//                    }
//
//                }
//        );
//
//
//    }
//
//
//    private void handleSignIn(Credential credential) {
//        if (credential instanceof CustomCredential) {
//            CustomCredential customCredential = (CustomCredential) credential;
//
//            if (TYPE_GOOGLE_ID_TOKEN_CREDENTIAL.equals(customCredential.getType())) {
//                try {
//                    Bundle credentialData = customCredential.getData();
//                    GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credentialData);
//                    String idToken = googleIdTokenCredential.getIdToken();
//                    firebaseAuthWithGoogle(idToken);
//                } catch (Exception e) {
//                    Log.e("GOOGLE_AUTH", "Error parsing Google ID Token", e);
//                    Toast.makeText(this, "Error parsing Google ID Token", Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                Log.w("GOOGLE_AUTH", "Unsupported credential type: " + customCredential.getType());
//                Toast.makeText(this, "Unsupported credential type", Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            Log.w("GOOGLE_AUTH", "Unexpected credential format");
//            Toast.makeText(this, "Unexpected credential format", Toast.LENGTH_SHORT).show();
//        }
//    }
//    private void firebaseAuthWithGoogle(String idToken) {
//        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, task -> {
//                    if (task.isSuccessful()) {
//                        FirebaseUser user = mAuth.getCurrentUser();
//                        if (user != null) {
//                            updateUI(user);
//                        }
//                    } else {
//                        Exception exception = task.getException();
//                        if (exception != null) {
//                            Log.e("GOOGLE_AUTH", "Firebase Authentication failed", exception);
//                            Toast.makeText(MainActivity.this, "Authentication failed: " + exception.getMessage(), Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
//    }
//    private void updateUI(FirebaseUser user) {
//        if (user != null) {
//            intent = new Intent(MainActivity.this, HomePage.class);
//            startActivity(intent);
//            finish(); // Close the current activity
//        }
//    }
//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is already signed in
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
//    }







}