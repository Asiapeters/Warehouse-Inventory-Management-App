package com.example.finalproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.db.UserDatabase;

public class RegisterActivity extends AppCompatActivity {
    EditText usernameEditText;
    EditText passwordEditText;
    EditText confrimPasswordEditText;
    UserDatabase userDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        userDatabase = new UserDatabase(getApplicationContext());

        // Enable the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        usernameEditText = findViewById(R.id.editTextUsername);
        passwordEditText = findViewById(R.id.editTextPassword);
        confrimPasswordEditText = findViewById(R.id.editTextConfirmPassword);
    }

    private boolean validateRegistration(String username, String password, String confirmPassword) {
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void registerUser(View view) {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confrimPasswordEditText.getText().toString();
        boolean userCreated;

        if (validateRegistration(username, password, confirmPassword)) {
            userCreated = userDatabase.createUser(username, password);
            if(userCreated){
                goToLogin();
            }
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle the up button click (back action)
        if (item.getItemId() == android.R.id.home) {
            goToLogin();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToLogin() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
