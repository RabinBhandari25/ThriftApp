package com.example.thrift_app_store;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home_page extends AppCompatActivity {

    Button logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        /****
         *
         * ALot of the things are themporary
         *
         * */

        // Move logout button setup outside of the insets listener
        logout = findViewById(R.id.button_to_back);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String name = user.getDisplayName();
                    String email = user.getEmail();
                    String photoUrl = user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : null;




                    Log.d("USERINFO", "Name: " + name);
                    Log.d("USERINFO", "Email: " + email);
                    Log.d("USERINFO", "Profile Pic URL: " + photoUrl);

                }

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Home_page.this, MainActivity.class));


                finish(); // Close Home_page after logout
            }
        });
    }
}
