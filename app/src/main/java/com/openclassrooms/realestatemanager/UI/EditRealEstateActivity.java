package com.openclassrooms.realestatemanager.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.placeholder.PlaceholderContent;
import com.openclassrooms.realestatemanager.roomdb.AppDatabase;
import com.openclassrooms.realestatemanager.roomdb.ResidenceDAO;

public class EditRealEstateActivity extends AppCompatActivity {

    AppDatabase db;
    ResidenceDAO residenceDAO;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    PlaceholderContent.PlaceholderItem residence;

    TextInputEditText editResidType;
    TextInputEditText editResidSurface;
    TextInputEditText editResidRooms;
    TextInputEditText editResidBathrooms;
    TextInputEditText editResidBedrooms;
    TextInputEditText editResidLocation;
    TextInputEditText editResidAddress;
    TextInputEditText editResidDescription;
    TextInputEditText editResidPrice;

    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_residence);

        db = AppDatabase.getInstance(this);
        residenceDAO = db.residenceDAO();

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPref.edit();

        Gson gson = new Gson();
        String json = sharedPref.getString("CurrentResidence", "");
        residence = gson.fromJson(json, PlaceholderContent.PlaceholderItem.class);


        this.configureLayout();
    }

    private void configureLayout() {
        editResidType = findViewById(R.id.editResidType);
        editResidType.setText(residence.residType);

        editResidSurface = findViewById(R.id.editResidSurface);
        editResidSurface.setText(residence.residSurface);

        editResidRooms = findViewById(R.id.editResidRooms);
        editResidRooms.setText(residence.residRooms);

        editResidBathrooms = findViewById(R.id.editResidBathrooms);
        editResidBathrooms.setText(residence.residBathrooms);

        editResidBedrooms = findViewById(R.id.editResidBedrooms);
        editResidBedrooms.setText(residence.residBedrooms);

        editResidLocation = findViewById(R.id.editResidLocation);
        editResidLocation.setText(residence.residLocation);

        editResidAddress = findViewById(R.id.editResidAddress);
        editResidAddress.setText(residence.residAddress);

        editResidDescription = findViewById(R.id.editResidDescription);
        editResidDescription.setText(residence.residDescription);

        editResidPrice = findViewById(R.id.editResidPrice);
        editResidPrice.setText(residence.residPrice);

        saveButton = findViewById(R.id.editResidSaveButton);

        saveButton.setEnabled(true);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editResidence();
                Intent i = new Intent(EditRealEstateActivity.this, ItemDetailHostActivity.class);
                startActivity(i);
            }
        });
    }

    private void editResidence() {
        PlaceholderContent.PlaceholderItem editedResidence = new PlaceholderContent.PlaceholderItem(
                residence.id,
                editResidType.getText().toString(),
                editResidLocation.getText().toString(),
                editResidAddress.getText().toString(),
                editResidPrice.getText().toString(),
                editResidDescription.getText().toString(),
                editResidSurface.getText().toString(),
                editResidRooms.getText().toString(),
                editResidBathrooms.getText().toString(),
                editResidBedrooms.getText().toString()
        );


        residenceDAO.update(editedResidence);
        PlaceholderContent.editItem(residence, editedResidence);
        onListUpdated();
    }

    public void onListUpdated() {
        finish();
        overridePendingTransition(0, 0);
        startActivity(new Intent(EditRealEstateActivity.this, ItemDetailHostActivity.class));
        overridePendingTransition(0, 0);
    }
}
