package com.module6.weightlossgraftonbrown2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SettingsActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_settings);

        Button requestPermissionButton = findViewById(R.id.requestPermissionButton);
        Button logoutButton = findViewById(R.id.logoutButton);
        Button logWeightButton = findViewById(R.id.logWeightButton);

        requestPermissionButton.setOnClickListener(v -> requestSMSPermission());

        logoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        logWeightButton.setOnClickListener(v -> {
            // Navigate to the gridActivity screen
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                showToast("SMS permission granted!");
                //sendSMSNotifications();
            } else {
                // Permission denied
                showToast("SMS permission denied.");
            }
        }
    }
    private void requestSMSPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission already granted
            showToast("SMS permission is already granted.");
        } else {
            // Permission not granted, show the permission request dialog
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    PERMISSION_REQUEST_CODE);
        }
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
