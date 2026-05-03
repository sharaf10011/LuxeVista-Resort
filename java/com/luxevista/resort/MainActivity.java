package com.luxevista.resort;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.appbar.MaterialToolbar;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private MaterialToolbar toolbar;
    private CardView cardRooms, cardServices, cardProfile, cardAttractions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_main);
            Log.d(TAG, "MainActivity layout set successfully");

            initViews();
            setupToolbar();
            setupCardViews();

        } catch (Exception e) {
            Log.e(TAG, "Error in MainActivity onCreate: " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(this, "Error loading main screen", Toast.LENGTH_SHORT).show();
        }
    }

    private void initViews() {
        try {
            toolbar = findViewById(R.id.toolbar);
            cardRooms = findViewById(R.id.cardRooms);
            cardServices = findViewById(R.id.cardServices);
            cardProfile = findViewById(R.id.cardProfile);
            cardAttractions = findViewById(R.id.cardAttractions);

            Log.d(TAG, "MainActivity views initialized");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing MainActivity views: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupToolbar() {
        try {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("LuxeVista Resort");
            }
            Log.d(TAG, "Toolbar setup complete");
        } catch (Exception e) {
            Log.e(TAG, "Error setting up toolbar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            getMenuInflater().inflate(R.menu.main_menu, menu);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error creating options menu: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            if (item.getItemId() == R.id.menu_logout) {
                showLogoutDialog();
                return true;
            }
            return super.onOptionsItemSelected(item);
        } catch (Exception e) {
            Log.e(TAG, "Error handling menu item: " + e.getMessage());
            return false;
        }
    }

    private void setupCardViews() {
        try {
            cardRooms.setOnClickListener(v -> {
                Log.d(TAG, "Rooms card clicked");
                try {
                    startActivity(new Intent(this, RoomBookingActivity.class));
                } catch (Exception e) {
                    Log.e(TAG, "Error opening RoomBookingActivity: " + e.getMessage());
                    Toast.makeText(this, "Error opening rooms", Toast.LENGTH_SHORT).show();
                }
            });

            cardServices.setOnClickListener(v -> {
                Log.d(TAG, "Services card clicked");
                try {
                    startActivity(new Intent(this, ServiceReservationActivity.class));
                } catch (Exception e) {
                    Log.e(TAG, "Error opening ServiceReservationActivity: " + e.getMessage());
                    Toast.makeText(this, "Error opening services", Toast.LENGTH_SHORT).show();
                }
            });

            cardProfile.setOnClickListener(v -> {
                Log.d(TAG, "Profile card clicked");
                try {
                    startActivity(new Intent(this, ProfileActivity.class));
                } catch (Exception e) {
                    Log.e(TAG, "Error opening ProfileActivity: " + e.getMessage());
                    Toast.makeText(this, "Error opening profile", Toast.LENGTH_SHORT).show();
                }
            });

            cardAttractions.setOnClickListener(v -> {
                Log.d(TAG, "Attractions card clicked");
                Toast.makeText(this, "Attractions feature coming soon!", Toast.LENGTH_SHORT).show();
            });

            Log.d(TAG, "Card views setup complete");
        } catch (Exception e) {
            Log.e(TAG, "Error setting up card views: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showLogoutDialog() {
        try {
            new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", (dialog, which) -> logout())
                    .setNegativeButton("No", null)
                    .show();
        } catch (Exception e) {
            Log.e(TAG, "Error showing logout dialog: " + e.getMessage());
            logout(); // Direct logout if dialog fails
        }
    }

    private void logout() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("LuxeVistaPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

            Log.d(TAG, "Logout successful");
        } catch (Exception e) {
            Log.e(TAG, "Error during logout: " + e.getMessage());
            e.printStackTrace();
        }
    }
}