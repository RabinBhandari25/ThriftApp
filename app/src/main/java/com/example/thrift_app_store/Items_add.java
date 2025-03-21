package com.example.thrift_app_store;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class Items_add extends AppCompatActivity {

    private static final int PICK_IMAGES_REQUEST = 1;

     ImageView uploadPictureButton;
     EditText title, description, price;
     RadioGroup conditionRadioGroup, productCategoryRadioGroup;
     Button  prevImg, nextImg,submitButton;
     private   ArrayList<Uri> imageUris = new ArrayList<>();
    private String[] uri_string;

    int currentImg = 0;
     FirebaseAuth mAuth;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_items_add);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();

        /*
        *       we have all our views below
        * */
         mAuth = FirebaseAuth.getInstance();

        uploadPictureButton = findViewById(R.id.uploadFileButton);
        title = findViewById(R.id.titleEditText);
        description = findViewById(R.id.descriptionEditText);
        price = findViewById(R.id.priceEditText);
        conditionRadioGroup = findViewById(R.id.conditionRadioGroup);
        productCategoryRadioGroup = findViewById(R.id.productCategoryRadioGroup);
        submitButton = findViewById(R.id.submitButton);

        prevImg = findViewById(R.id.pic_move_left);
        nextImg = findViewById(R.id.pic_move_right);



        uploadPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();


            }
        });


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



               saveItemToFirebase();
            }
        });

        prevImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUris == null || imageUris.isEmpty() || currentImg <= 0) {
                    return;
                }
                currentImg--;
                updateImageView();
            }
        });

        nextImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUris == null || imageUris.isEmpty() || currentImg >= imageUris.size() - 1) {
                    return;
                }
                currentImg++;
                updateImageView();
            }
        });






    }

    private void updateImageView(){
        uploadPictureButton.setImageURI(imageUris.get(currentImg));
    }



    private void saveItemToFirebase() {
        String titleStr = title.getText().toString().trim();
        String descriptionStr = description.getText().toString().trim();
        String priceStr = price.getText().toString().trim();

        if(titleStr.isEmpty()){
            title.setError("Please fill in all fields");
            return;
        }

        if (descriptionStr.isEmpty()){
            description.setError("Please fill in all fields");
            return;
        }

        if (priceStr.isEmpty()){
            price.setError("Please fill in all fields");
            return;
        }

        double priceDouble;
        try {
            priceDouble = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid price", Toast.LENGTH_SHORT).show();
              return;
        }

        int selectedConditionId = conditionRadioGroup.getCheckedRadioButtonId();
        if (selectedConditionId == -1) {
            Toast.makeText(this, "Please select a condition", Toast.LENGTH_SHORT).show();
            return;
        }
        RadioButton selectedConditionButton = findViewById(selectedConditionId);
        String condition = selectedConditionButton.getText().toString();

//        Log.w("this", condition);
        // Retrieve selected category
        int selectedCategoryId = productCategoryRadioGroup.getCheckedRadioButtonId();
        if (selectedCategoryId == -1) {
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedCategoryButton = findViewById(selectedCategoryId);
        String category = selectedCategoryButton.getText().toString();


//        Log.w("this", category);


        // Upload images to Firebase Storage and get their URLs
        if (imageUris.isEmpty()) {
            Toast.makeText(this, "Please select at least one image", Toast.LENGTH_SHORT).show();
            return;
        }


        Item newItem = new Item(titleStr,descriptionStr,priceDouble,condition,category,uri_string);



        FirebaseUser user = mAuth.getCurrentUser();


        /**
         *  trying to save data to firebase
         *
         *  each item saved by the user will be saved under their email address
         *  and each items will be saved under a different key
         */

        HashMap<String, Object> itemMap = new HashMap<>();

// Add basic fields
        itemMap.put("title", titleStr);
        itemMap.put("description", descriptionStr);
        itemMap.put("price", priceDouble);
        itemMap.put("condition", condition);
        itemMap.put("category", category);

// Add timestamp
        itemMap.put("timestamp", ServerValue.TIMESTAMP);

// Convert image URIs to strings (Firebase can't store Uri objects directly)
        ArrayList<String> imageUriStrings = new ArrayList<>();
        for (Uri uri : imageUris) {
            imageUriStrings.add(uri.toString());
        }
        itemMap.put("imageUris", imageUriStrings);

// Add seller information
        if (user != null) {
            itemMap.put("sellerId", user.getUid());
            itemMap.put("sellerName", user.getDisplayName() != null ? user.getDisplayName() : "Anonymous");
        }

// Example of how to save this to Firebase
        FirebaseDatabase.getInstance().getReference("items")
                .push()  // Generates a unique ID
                .setValue(itemMap)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Item saved successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to save item", Toast.LENGTH_SHORT).show();
                    }
                });


    }











    /*
    *
    *   Every thing below this is for taking image from the gallery
    * */
    @SuppressLint("IntentReset")
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGES_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGES_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    imageUris.clear();

                    if (data.getClipData() != null) {

                        int count = data.getClipData().getItemCount();
                        for (int i = 0; i < count; i++) {
                            Uri imageUri = data.getClipData().getItemAt(i).getUri();
                            imageUris.add(imageUri);
                        }
                    } else if (data.getData() != null) {

                        Uri imageUri = data.getData();
                        imageUris.add(imageUri);
                    }


                }

                uploadPictureButton.setImageURI(imageUris.get(currentImg));
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Image selection canceled", Toast.LENGTH_SHORT).show();
            }
        }

    }
}