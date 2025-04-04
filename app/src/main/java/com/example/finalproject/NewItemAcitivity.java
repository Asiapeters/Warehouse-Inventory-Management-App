package com.example.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.db.ItemDatabase;

import java.util.Objects;

public class NewItemAcitivity extends AppCompatActivity {
    private ItemDatabase itemDatabase;

    private EditText itemNameTextEdit;
    private EditText itemDescriptionTextEdit;
    private EditText itemQuantityTextEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_item);

        itemDatabase = new ItemDatabase(getApplicationContext());

        itemNameTextEdit = findViewById(R.id.editTextItemName);
        itemDescriptionTextEdit = findViewById(R.id.edittextItemDescription);
        itemQuantityTextEdit = findViewById(R.id.editTextNumberQuantity);

        // Enable the back button
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle the up button click (back action)
        if (item.getItemId() == android.R.id.home) {
            goToHome();
            return true;
        } else if (item.getItemId() == R.id.action_logout) {
            logout();
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        // Clear user session data
        SharedPreferences preferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        // Redirect to Login
        Intent intent = new Intent(NewItemAcitivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();  // Close the current activity
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_menu, menu);
        return true;
    }

    private boolean validateItemDetails(String name, String description, String quantity) {
        if (name.isEmpty() || description.isEmpty() || quantity.isEmpty()) {
            Toast.makeText(NewItemAcitivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Integer.parseInt(quantity) <= 0) {
            Toast.makeText(NewItemAcitivity.this, "Please make quantity greater than zero", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void createItem(View view) {
        String name = itemNameTextEdit.getText().toString();
        String description = itemDescriptionTextEdit.getText().toString();
        String quantity = itemQuantityTextEdit.getText().toString();
        boolean itemCreated;

        if (validateItemDetails(name, description, quantity)) {
            itemCreated = itemDatabase.createItem(name, description, Integer.parseInt(quantity));
            if (itemCreated) {
                Toast.makeText(NewItemAcitivity.this, "Item Successfully created", Toast.LENGTH_SHORT).show();
                goToHome();
            }
        }
    }

    private void goToHome() {
        Intent intent = new Intent(NewItemAcitivity.this, ItemsActivity.class);
        startActivity(intent);
        finish();
    }
}
