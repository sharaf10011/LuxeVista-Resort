package com.luxevista.resort;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    private MaterialToolbar toolbar;
    private TextView tvUserName, tvUserEmail, tvUserPhone;
    private RecyclerView recyclerViewBookings;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_profile);
            Log.d(TAG, "Profile layout set successfully");

            initViews();
            setupToolbar();
            setupRecyclerView();
            loadUserData();

        } catch (Exception e) {
            Log.e(TAG, "Error in ProfileActivity onCreate: " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(this, "Error loading profile", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initViews() {
        try {
            toolbar = findViewById(R.id.toolbar);
            tvUserName = findViewById(R.id.tvUserName);
            tvUserEmail = findViewById(R.id.tvUserEmail);
            tvUserPhone = findViewById(R.id.tvUserPhone);
            recyclerViewBookings = findViewById(R.id.recyclerViewBookings);

            databaseHelper = new DatabaseHelper(this);
            Log.d(TAG, "Views initialized");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing views: " + e.getMessage());
            throw e;
        }
    }

    private void setupToolbar() {
        try {
            if (toolbar != null) {
                setSupportActionBar(toolbar);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setTitle("My Profile");
                }
                toolbar.setNavigationOnClickListener(v -> onBackPressed());
            }
            Log.d(TAG, "Toolbar setup complete");
        } catch (Exception e) {
            Log.e(TAG, "Error setting up toolbar: " + e.getMessage());
        }
    }

    private void setupRecyclerView() {
        try {
            if (recyclerViewBookings != null) {
                recyclerViewBookings.setLayoutManager(new LinearLayoutManager(this));
            }
            Log.d(TAG, "RecyclerView setup complete");
        } catch (Exception e) {
            Log.e(TAG, "Error setting up RecyclerView: " + e.getMessage());
        }
    }

    private void loadUserData() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("LuxeVistaPrefs", MODE_PRIVATE);
            String userEmail = sharedPreferences.getString("userEmail", "");

            if (databaseHelper != null && !userEmail.isEmpty()) {
                User user = databaseHelper.getUserByEmail(userEmail);
                if (user != null) {
                    // Set user info
                    if (tvUserName != null) tvUserName.setText(user.getFullName());
                    if (tvUserEmail != null) tvUserEmail.setText(user.getEmail());
                    if (tvUserPhone != null) tvUserPhone.setText(user.getPhone());

                    // Load user bookings
                    List<Booking> bookings = databaseHelper.getUserBookings(user.getId());
                    BookingAdapter bookingAdapter = new BookingAdapter(bookings);
                    if (recyclerViewBookings != null) {
                        recyclerViewBookings.setAdapter(bookingAdapter);
                    }

                    Log.d(TAG, "User data loaded: " + user.getFullName() + ", Bookings: " + bookings.size());
                } else {
                    Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "User not found in database");
                }
            } else {
                Toast.makeText(this, "Please login again", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "No user email or database helper");
                finish();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading user data: " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(this, "Error loading user data", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}