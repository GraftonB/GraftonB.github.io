package com.module6.weightlossgraftonbrown2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

// MainActivity class declaration
public class LoginActivity extends AppCompatActivity {

    // Declare variables
    private EditText editTextUsername;
    private EditText editTextPassword;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        // Find views by their IDs
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonLogin = findViewById(R.id.buttonLogin);
        Button buttonCreateAccount = findViewById(R.id.buttonCreateAccount);

        // Create or open the database
        database = openOrCreateDatabase("UserAccounts.db", MODE_PRIVATE, null);

        // Create the user accounts table if doesnt exist
        database.execSQL("CREATE TABLE IF NOT EXISTS UserAccounts (username TEXT PRIMARY KEY, password TEXT)");

        // Set OnClickListener for login button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();

                if (isValidCredentials(username, password)) {
                    // Successful login output
                    showToast("Login successful!");

                    // Navigate to the notification_screen.xml
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    // Invalid login output
                    showToast("Invalid username or password");
                }
            }
        });

        // Set OnClickListener for create account button
        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();

                if (createAccount(username, password)) {
                    showToast("Account created successfully!");
                } else {
                    showToast("Failed to create an account.");
                }
            }
        });
    }

    // Method to validate user credentials
    private boolean isValidCredentials(String username, String password) {
        Cursor cursor = database.query(
                "UserAccounts",
                new String[]{"username", "password"},
                "username = ? AND password = ?",
                new String[]{username, password},
                null,
                null,
                null
        );

        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        return isValid;
    }

    // Method to create a new user account
    private boolean createAccount(String username, String password) {
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);

        long result = database.insert("UserAccounts", null, values);
        return result != -1;
    }

    // Method to show a Toast message
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the database connection
        if (database != null) {
            database.close();
        }
    }
}
