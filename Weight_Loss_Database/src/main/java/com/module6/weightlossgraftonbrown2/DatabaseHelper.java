package com.module6.weightlossgraftonbrown2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UserAccounts.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "UserAccounts";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    private static final String SALT = "CS_499_SALT";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the database table for user accounts if it doesn't exist
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                " (" + COLUMN_USERNAME + " TEXT PRIMARY KEY, " + COLUMN_PASSWORD + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades if needed (not implemented in this example)
    }

    private String hashWithSalt(String password) {
        try {
            // Use SHA-256 for hashing the password
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Get the bytes of the salt and password
            byte[] saltBytes = SALT.getBytes();
            byte[] passwordBytes = password.getBytes();

            // Combine salt and password bytes to create the input for hashing
            byte[] combined = new byte[saltBytes.length + passwordBytes.length];
            System.arraycopy(saltBytes, 0, combined, 0, saltBytes.length);
            System.arraycopy(passwordBytes, 0, combined, saltBytes.length, passwordBytes.length);

            // Perform the hashing
            byte[] hashedBytes = md.digest(combined);

            // Convert the hashed bytes to a readable string format
            StringBuilder hashBuilder = new StringBuilder();
            for (byte b : hashedBytes) {
                hashBuilder.append(String.format("%02x", b));
            }

            return hashBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean createAccount(String username, String password) {
        // Sanitize and validate input
        username = sanitizeInput(username);
        password = sanitizeInput(password);

        // Check if the input is valid
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            return false; // Input validation failed
        }

        // Hash and salt the password
        String hashedPassword = hashWithSalt(password);

        // Get a writable database instance
        SQLiteDatabase db = getWritableDatabase();

        // Prepare the values to be inserted into the database
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, hashedPassword);

        // Insert the new account into the database
        long result = db.insert(TABLE_NAME, null, values);
        db.close();

        // Check if the insertion was successful
        return result != -1;
    }

    public boolean isValidCredentials(String username, String password) {
        // Sanitize and validate input
        username = sanitizeInput(username);
        password = sanitizeInput(password);

        // Check if the input is valid
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            return false; // Input validation failed
        }

        // Hash and salt the provided password to match the stored hash in the database
        String hashedPassword = hashWithSalt(password);

        // Get a readable database instance
        SQLiteDatabase db = getReadableDatabase();

        // Query the database for the username and hashed password
        Cursor cursor = db.query(
                TABLE_NAME,
                new String[]{COLUMN_USERNAME, COLUMN_PASSWORD},
                COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?",
                new String[]{username, hashedPassword},
                null,
                null,
                null
        );

        // Check if the cursor returned any matching rows
        boolean isValid = cursor.getCount() > 0;

        // Close the cursor and the database
        cursor.close();
        db.close();

        // Return the result of validation
        return isValid;
    }

    private String sanitizeInput(String input) {
        // Trim leading and trailing whitespaces
        return input.trim();
    }
}
