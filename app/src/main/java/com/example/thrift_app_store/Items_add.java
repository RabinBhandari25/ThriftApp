package com.example.thrift_app_store;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

import java.net.URL;
import java.util.ArrayList;

public class Items_add extends AppCompatActivity {

    private static final int PICK_IMAGES_REQUEST = 1;

     TextView uploadPictureButton;
     EditText title, description, price;
     RadioGroup conditionRadioGroup, productCategoryRadioGroup;
     Button submitButton;
       ArrayList<Uri> imageUris = new ArrayList<>();
       ArrayList<String> imageUrls = new ArrayList<>();
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
//        mAuth = FirebaseAuth.getInstance();

        /*
        *       we have all our views below
        * */
        uploadPictureButton = findViewById(R.id.uploadFileButton);
        title = findViewById(R.id.titleEditText);
        description = findViewById(R.id.descriptionEditText);
        price = findViewById(R.id.priceEditText);
        conditionRadioGroup = findViewById(R.id.conditionRadioGroup);
        productCategoryRadioGroup = findViewById(R.id.productCategoryRadioGroup);
        submitButton = findViewById(R.id.submitButton);



        uploadPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();


                for( int i = 0; i<imageUris.size(); i++){
                    imageUrls.add(String.valueOf(imageUris.get(i)));
                }

            }
        });


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int i = 0; i < imageUrls.size(); i++){
                    Log.w("test", imageUrls.get(i));
                }

//                saveItemToFirebase();
            }
        });



    }

    private void saveItemToFirebase() {
        String titleStr = title.getText().toString().trim();
        String descriptionStr = description.getText().toString().trim();
        String priceStr = price.getText().toString().trim();

        // Validate input
        if (titleStr.isEmpty() || descriptionStr.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double priceDouble;
        try {
            priceDouble = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid price", Toast.LENGTH_SHORT).show();
            return;
        }

        // Retrieve selected condition
        int selectedConditionId = conditionRadioGroup.getCheckedRadioButtonId();
        if (selectedConditionId == -1) {
            Toast.makeText(this, "Please select a condition", Toast.LENGTH_SHORT).show();
            return;
        }
        RadioButton selectedConditionButton = findViewById(selectedConditionId);
        String condition = selectedConditionButton.getText().toString();

        // Retrieve selected category
        int selectedCategoryId = productCategoryRadioGroup.getCheckedRadioButtonId();
        if (selectedCategoryId == -1) {
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
            return;
        }
        RadioButton selectedCategoryButton = findViewById(selectedCategoryId);
        String category = selectedCategoryButton.getText().toString();

        // Upload images to Firebase Storage and get their URLs
        if (imageUris.isEmpty()) {
            Toast.makeText(this, "Please select at least one image", Toast.LENGTH_SHORT).show();
            return;
        }


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
                    // Clear the list before adding new URIs
                    imageUris.clear();

                    if (data.getClipData() != null) {
                        // Multiple images selected
                        int count = data.getClipData().getItemCount();
                        for (int i = 0; i < count; i++) {
                            Uri imageUri = data.getClipData().getItemAt(i).getUri();
                            imageUris.add(imageUri); // Add the URI to the list
                        }
                    } else if (data.getData() != null) {
                        // Single image selected
                        Uri imageUri = data.getData();
                        imageUris.add(imageUri); // Add the URI to the list
                    }

                    // Log the number of selected images
                    Log.d("Selected Images", "Number of images: " + imageUris.size());

                    // Display a toast to confirm selection
                    Toast.makeText(this, "Selected " + imageUris.size() + " images", Toast.LENGTH_SHORT).show();


                }
            } else if (resultCode == RESULT_CANCELED) {
                // User canceled the selection
                Toast.makeText(this, "Image selection canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}