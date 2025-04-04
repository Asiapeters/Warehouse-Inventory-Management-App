package com.example.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.Models.Item;
import com.example.finalproject.db.ItemDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.telephony.SmsManager;

import java.util.ArrayList;
import java.util.List;

public class ItemsActivity extends AppCompatActivity {
    private ListView listView;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_items);

        ItemDatabase database = new ItemDatabase(getApplicationContext());

        listView = findViewById(R.id.listView);

        List<Item> itemList = database.getAllItems();

        // Set up the adapter for ListView
        ArrayAdapter<Item> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                itemList
        );
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item clickedItem = itemList.get(position);
                Intent intent = new Intent(ItemsActivity.this, EditItemActivity.class);
                intent.putExtra("key_item", clickedItem.getId());
                startActivity(intent);
                finish();
            }
        });

        // Initialize Floating Action Button
        fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the next activity
                Intent intent = new Intent(ItemsActivity.this, NewItemAcitivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            logout();
            return true;
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
        Intent intent = new Intent(ItemsActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();  // Close the current activity
    }
}
