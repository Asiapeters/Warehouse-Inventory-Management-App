package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.Models.User;
import com.example.finalproject.db.ItemDatabase;
import com.example.finalproject.db.UserDatabase;

public class MainActivity extends AppCompatActivity {
    EditText usernameEditText;
    EditText passwordEditText;
    UserDatabase userDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        userDatabase = new UserDatabase(getApplicationContext());

        usernameEditText = findViewById(R.id.editTextUsername);
        passwordEditText = findViewById(R.id.editTextPassword);
    }

    public void validateLogin(View view) {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(MainActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
        else {
            User user = userDatabase.getUserByUsername(username);
            if(user != null && user.getUsername().equals(username) && user.getPassword().equals(password)) {
                Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, ItemsActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void goToRegistristation(View view) {
        Intent intent = new Intent(MainActivity.this, RegisterActivity   .class);
        startActivity(intent);
        finish();
    }
}