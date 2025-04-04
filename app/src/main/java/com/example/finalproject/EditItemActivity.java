package com.example.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.Models.Item;
import com.example.finalproject.db.ItemDatabase;

import java.util.Objects;


public class EditItemActivity extends AppCompatActivity {
    private TextView itemTextview;
    private EditText ItemNameTextEdit;
    private EditText itemDescriptionTextEdit;
    private EditText itemQuantityTextEdit;
    private ItemDatabase database;
    private int itemID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_item);

        itemTextview = findViewById(R.id.textViewItem);
        itemDescriptionTextEdit = findViewById(R.id.edittextItemDescription);
        itemQuantityTextEdit = findViewById(R.id.editTextNumberQuantity);
        ItemNameTextEdit = findViewById(R.id.editTextitemName);

        itemID = getIntent().getIntExtra("key_item", -1);
        database = new ItemDatabase(getApplicationContext());
        Item item = database.getItemByID(itemID);

        if(item == null) {
            Toast.makeText(EditItemActivity.this, "Item not found", Toast.LENGTH_SHORT).show();
            goToHome();
        }

        itemTextview.setText(item.getId() + " - " + item.getName());
        itemDescriptionTextEdit.setText(item.getDescription());
        itemQuantityTextEdit.setText(String.valueOf(item.getQuantity()));
        ItemNameTextEdit.setText(item.getName());

        // Enable the back button
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            goToHome();
            return true;
        } else if (item.getItemId() == R.id.action_logout) {
            logout();
        }

        return super.onOptionsItemSelected(item);
    }

    // Method to handle logout action
    private void logout() {
        // Clear user session data
        SharedPreferences preferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        // Redirect to Login
        Intent intent = new Intent(EditItemActivity.this, MainActivity.class);
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
            Toast.makeText(EditItemActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Integer.valueOf(quantity) <= 0) {
            Toast.makeText(EditItemActivity.this, "Please make quantity greater than zero", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void updateItem(View view) {
        String name = ItemNameTextEdit.getText().toString();
        String description = itemDescriptionTextEdit.getText().toString();
        String quantity = itemQuantityTextEdit.getText().toString();
        boolean itemUpdated;

        if (validateItemDetails(name, description, quantity)) {
            itemUpdated = database.updateItem(itemID, name, description, Integer.valueOf(quantity));
            if (itemUpdated) {
                Toast.makeText(EditItemActivity.this, "Item Successfully Updated", Toast.LENGTH_SHORT).show();
                goToHome();
            }
        }
    }

    public void deleteItem(View view) {
        String name = ItemNameTextEdit.getText().toString();
        String description = itemDescriptionTextEdit.getText().toString();
        String quantity = itemQuantityTextEdit.getText().toString();
        boolean itemDeleted;
        itemDeleted = database.deleteItem(itemID);
        if (itemDeleted) {
            Toast.makeText(EditItemActivity.this, "Item Successfully Deleted", Toast.LENGTH_SHORT).show();
            goToHome();
        }

    }


    private void goToHome() {
        Intent intent = new Intent(EditItemActivity.this, ItemsActivity.class);
        startActivity(intent);
        finish();
    }
}
