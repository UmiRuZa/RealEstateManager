package com.openclassrooms.realestatemanager.UI;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.placeholder.PlaceholderContent;
import com.openclassrooms.realestatemanager.roomdb.AppDatabase;
import com.openclassrooms.realestatemanager.roomdb.ResidenceDAO;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@SuppressWarnings("deprecation")
public class AddRealEstateActivity extends AppCompatActivity {

    AppDatabase db;
    ResidenceDAO residenceDAO;

    TextInputLayout residType;
    TextInputLayout residSurface;
    TextInputLayout residRooms;
    TextInputLayout residBathrooms;
    TextInputLayout residBedrooms;
    TextInputLayout residLocation;
    TextInputLayout residAddress;
    TextInputLayout residDescription;
    TextInputLayout residPrice;

    Button saveButton;

    private static final int GALLERY_REQUEST = 100;
    private static final int CAMERA_REQUEST = 200;
    Uri photoURI;
    ArrayList<Uri> mArrayUri;
    int position = 0;

    String currentPhotoPath;

    ImageView residPicturesIV;
    Button openGalleryButton;
    Button openCameraButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_residence);

        db = AppDatabase.getInstance(this);
        residenceDAO = db.residenceDAO();

        mArrayUri = new ArrayList<>();

        residPicturesIV = findViewById(R.id.residPicturesIV);

        //Request for camera runtime permission
        if (ContextCompat.checkSelfPermission(AddRealEstateActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddRealEstateActivity.this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }

        openGalleryButton = findViewById(R.id.residPicturesButtonGallery);
        openGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        openCameraButton = findViewById(R.id.residPicturesButtonCamera);
        openCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });

        this.configureLayout();
    }

    private void openGallery() {
        Intent i = new Intent();
        i.setType("image/*");
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        i.setAction(Intent.ACTION_GET_CONTENT);

        if (i.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                i.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(Intent.createChooser(i, "Select picture"), GALLERY_REQUEST);
            }
        }
    }

    private void openCamera() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (i.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                i.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(i, CAMERA_REQUEST);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            // Get the Image from data
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    // adding imageUri in array
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    mArrayUri.add(imageUri);
                }
            } else {
                Uri imageUri = data.getData();
                mArrayUri.add(imageUri);
            }

            // setting 1st selected image into imageView
            residPicturesIV.setImageURI(mArrayUri.get(0));
            position = 0;
        }
        else {
            // show this if no image is selected
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            residPicturesIV.setImageURI(photoURI);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void configureLayout() {
        residType = findViewById(R.id.residTypeLyt);
        residSurface = findViewById(R.id.residSurfaceLyt);
        residRooms = findViewById(R.id.residRoomsLyt);
        residBathrooms = findViewById(R.id.residBathroomsLyt);
        residBedrooms = findViewById(R.id.residBedroomsLyt);
        residLocation = findViewById(R.id.residLocationLyt);
        residAddress = findViewById(R.id.residAddressLyt);
        residDescription = findViewById(R.id.residDescriptionLyt);
        residPrice = findViewById(R.id.residPriceLyt);
        saveButton = findViewById(R.id.residSaveButton);

        saveButton.setEnabled(true);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createResidence();
                Intent i = new Intent(AddRealEstateActivity.this, ItemDetailHostActivity.class);
                startActivity(i);
            }
        });
    }

    private void createResidence() {
        PlaceholderContent.PlaceholderItem placeholderItem = new PlaceholderContent.PlaceholderItem(
                String.valueOf(System.currentTimeMillis()),
                residType.getEditText().getText().toString(),
                residLocation.getEditText().getText().toString(),
                residAddress.getEditText().getText().toString(),
                residPrice.getEditText().getText().toString(),
                residDescription.getEditText().getText().toString(),
                residSurface.getEditText().getText().toString(),
                residRooms.getEditText().getText().toString(),
                residBathrooms.getEditText().getText().toString(),
                residBedrooms.getEditText().getText().toString());
        PlaceholderContent.addItem(placeholderItem);

        residenceDAO.insertAll(placeholderItem);
    }


}
