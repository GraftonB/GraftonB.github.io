package com.module6.weightlossgraftonbrown2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private LinearLayout containerLayout;
    private double goalWeight = 0.0;
    private static final String PREFS_NAME = "WeightLoggingPrefs";
    private static final String KEY_GOAL_WEIGHT = "GoalWeight";
    private static final String KEY_LOGGED_WEIGHTS = "LoggedWeights";
    private SharedPreferences sharedPreferences;

    /*
     * Called when the activity is created.
     * Initializes the UI components, sets click listeners,
     * retrieves any stored goal weight and logged weights.
     * If a goal weight is set, it is displayed in the UI.
     * If there are logged weights, they are displayed in separate rows.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Find UI components
        containerLayout = findViewById(R.id.containerLayout);
        Button logWeightButton = findViewById(R.id.logWeightButton);
        Button setGoalWeightButton = findViewById(R.id.setGoalWeightButton);
        ImageButton settingsButton = findViewById(R.id.settingsButton);

        // Set click listeners for buttons
        logWeightButton.setOnClickListener(v -> showWeightInputDialog());
        setGoalWeightButton.setOnClickListener(v -> showGoalWeightInputDialog());
        settingsButton.setOnClickListener(v -> navigateToSettings());

        // Check if the goal weight is already set in SharedPreferences
        if (sharedPreferences.contains(KEY_GOAL_WEIGHT)) {
            goalWeight = sharedPreferences.getFloat(KEY_GOAL_WEIGHT, 0.0f);
            updateGoalWeightTextView();
        }
        // Check if the logged weights are already set in SharedPreferences
        List<String> loggedWeights = getLoggedWeights();
        for (String weight : loggedWeights) {
            addRow(weight);
        }
    }


    private void navigateToSettings() {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    /*
     * Shows a dialog for inputting weight.
     * The user can enter a weight value and choose to log it or cancel.
     * If the weight is logged, it is added as a row in the UI.
     */
    private void showWeightInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Log Weight");

        // Inflate the custom dialog layout
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_weight_input, null);
        final EditText weightEditText = viewInflated.findViewById(R.id.weightEditText);

        builder.setView(viewInflated);

        // Set positive button click listener for logging weight
        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
            // Get the weight input from the EditText
            String weightInput = weightEditText.getText().toString();
            // Add the weight as a row in the UI
            addRow(weightInput);
            dialog.dismiss();
        });

        // Set negative button click listener for canceling weight input
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());

        // Show the weight input dialog
        builder.show();
    }


    /*
     * Shows a dialog for setting the goal weight.
     * The user can enter a weight value and choose to set it as the goal weight or cancel.
     * If the goal weight is set, it is saved in SharedPreferences and displayed in the UI.
     */
    @SuppressLint("SetTextI18n")
    private void showGoalWeightInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Goal Weight");

        // Inflate the custom dialog layout
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_weight_input, null);
        final EditText weightEditText = viewInflated.findViewById(R.id.weightEditText);

        builder.setView(viewInflated);

        // Set positive button click listener for setting goal weight
        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
            // Get the goal weight input from the EditText
            String weightInput = weightEditText.getText().toString();
            goalWeight = Double.parseDouble(weightInput);

            // Save the goal weight in SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat(KEY_GOAL_WEIGHT, (float) goalWeight);
            editor.apply();

            // Update the goal weight displayed in the UI
            updateGoalWeightTextView();

            dialog.dismiss();
        });

        // Set negative button click listener for canceling goal weight input
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());

        // Show the goal weight input dialog
        builder.show();
    }


    @SuppressLint("SetTextI18n")
    private void addRow(String weight) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View rowView = inflater.inflate(R.layout.row_item, containerLayout, false);

        TextView dateTextView = rowView.findViewById(R.id.dateTextView);
        TextView weightTextView = rowView.findViewById(R.id.weightTextView);
        ImageButton deleteButton = rowView.findViewById(R.id.deleteButton);
        ImageButton editButton = rowView.findViewById(R.id.editButton);

        // Get the current date and format it
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String currentDate = dateFormat.format(new Date());

        // Set the current date and weight in the respective TextViews
        dateTextView.setText("Date: " + currentDate);
        weightTextView.setText(weight + " lbs");

        // Set click listener for the delete button
        deleteButton.setOnClickListener(v -> {
            containerLayout.removeView(rowView);
            removeLoggedWeight(weight);
        });

        // Set click listener for the edit button
        editButton.setOnClickListener(v -> showWeightEditDialog(weightTextView, weight));

        // Check if logged weight meets goal weight criteria
        double loggedWeight = Double.parseDouble(weight);
        if (loggedWeight <= goalWeight) {
            Toast.makeText(MainActivity.this, "Goal weight reached!", Toast.LENGTH_SHORT).show();
        }

        // Save the logged weight in SharedPreferences
        saveLoggedWeight(weight);

        containerLayout.addView(rowView);
    }

    @SuppressLint("SetTextI18n")
    private void showWeightEditDialog(final TextView weightTextView, final String weight) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Weight");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_weight_input, null);
        final EditText weightEditText = viewInflated.findViewById(R.id.weightEditText);
        weightEditText.setText(weight);

        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
            String weightInput = weightEditText.getText().toString();
            weightTextView.setText(weightInput + " lbs");
            updateLoggedWeight(weight, weightInput);
            dialog.dismiss();
        });

        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());

        builder.show();
    }

    @SuppressLint("SetTextI18n")
    private void updateGoalWeightTextView() {
        TextView goalWeightTextView = findViewById(R.id.goalWeightTextView);
        goalWeightTextView.setText("Goal Weight: " + goalWeight);
    }

    // Method to save the logged weight
    private void saveLoggedWeight(String weight) {
        List<String> loggedWeights = getLoggedWeights();
        loggedWeights.add(weight);

        Set<String> weightSet = new HashSet<>(loggedWeights);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(KEY_LOGGED_WEIGHTS, weightSet);
        editor.apply();
    }

    // Method to remove the logged weight
    private void removeLoggedWeight(String weight) {
        List<String> loggedWeights = getLoggedWeights();
        loggedWeights.remove(weight);

        Set<String> weightSet = new HashSet<>(loggedWeights);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(KEY_LOGGED_WEIGHTS, weightSet);
        editor.apply();
    }

    // Method to update the logged weight
    private void updateLoggedWeight(String oldWeight, String newWeight) {
        List<String> loggedWeights = getLoggedWeights();
        loggedWeights.remove(oldWeight);
        loggedWeights.add(newWeight);

        Set<String> weightSet = new HashSet<>(loggedWeights);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(KEY_LOGGED_WEIGHTS, weightSet);
        editor.apply();
    }

    // Method to retrieve the logged weights from SharedPreferences
    private List<String> getLoggedWeights() {
        Set<String> weightSet = sharedPreferences.getStringSet(KEY_LOGGED_WEIGHTS, new HashSet<>());
        return new ArrayList<>(weightSet);
    }
}
