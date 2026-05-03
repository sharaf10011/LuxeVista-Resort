package com.luxevista.resort;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RoomBookingActivity extends AppCompatActivity implements RoomAdapter.OnRoomClickListener {
    private static final String TAG = "RoomBookingActivity";
    private MaterialToolbar toolbar;
    private RecyclerView recyclerViewRooms;
    private RoomAdapter roomAdapter;
    private List<Room> roomList;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_room_booking);
            Log.d(TAG, "RoomBooking layout set successfully");

            initViews();
            setupToolbar();
            setupRecyclerView();
            loadRooms();

            databaseHelper = new DatabaseHelper(this);
            Log.d(TAG, "RoomBookingActivity initialized successfully");

        } catch (Exception e) {
            Log.e(TAG, "Error in RoomBookingActivity onCreate: " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(this, "Error loading rooms", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initViews() {
        try {
            toolbar = findViewById(R.id.toolbar);
            recyclerViewRooms = findViewById(R.id.recyclerViewRooms);
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
                    getSupportActionBar().setTitle("Book Rooms");
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
            roomList = new ArrayList<>();
            roomAdapter = new RoomAdapter(roomList, this);

            if (recyclerViewRooms != null) {
                recyclerViewRooms.setLayoutManager(new LinearLayoutManager(this));
                recyclerViewRooms.setAdapter(roomAdapter);
            }
            Log.d(TAG, "RecyclerView setup complete");
        } catch (Exception e) {
            Log.e(TAG, "Error setting up RecyclerView: " + e.getMessage());
            throw e;
        }
    }

    private void loadRooms() {
        try {
            roomList.clear();

            // Using simple drawable resource IDs that exist by default
            roomList.add(new Room("Ocean View Suite", "Luxurious suite with panoramic ocean views, private balcony, and premium amenities.", 350.0, R.drawable.ocean_suite, "Suite"));
            roomList.add(new Room("Deluxe Room", "Spacious room with modern furnishing, garden view, and exclusive services.", 250.0, R.drawable.deluxe_room, "Standard"));
            roomList.add(new Room("Presidential Suite", "Ultimate luxury experience with private pool, butler service, and exclusive amenities.", 850.0, R.drawable.presidential_suite, "Suite"));
            roomList.add(new Room("Garden Villa", "Private villa surrounded by tropical gardens with outdoor dining area.", 450.0, R.drawable.garden_villa, "Villa"));
            roomList.add(new Room("Beachfront Bungalow", "Traditional bungalow directly on the beach with private access to the ocean.", 550.0, R.drawable.beach_bungalow, "Bungalow"));

            if (roomAdapter != null) {
                roomAdapter.notifyDataSetChanged();
            }
            Log.d(TAG, "Rooms loaded: " + roomList.size());
        } catch (Exception e) {
            Log.e(TAG, "Error loading rooms: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onRoomClick(Room room) {
        try {
            Log.d(TAG, "Room clicked: " + room.getName());
            showDatePicker(room);
        } catch (Exception e) {
            Log.e(TAG, "Error handling room click: " + e.getMessage());
            Toast.makeText(this, "Error booking room", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDatePicker(Room room) {
        try {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        try {
                            Calendar selectedDate = Calendar.getInstance();
                            selectedDate.set(year, month, dayOfMonth);

                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            String bookingDate = dateFormat.format(selectedDate.getTime());

                            bookRoom(room, bookingDate);
                        } catch (Exception e) {
                            Log.e(TAG, "Error processing date selection: " + e.getMessage());
                            Toast.makeText(this, "Error processing date", Toast.LENGTH_SHORT).show();
                        }
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );

            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();

        } catch (Exception e) {
            Log.e(TAG, "Error showing date picker: " + e.getMessage());
            Toast.makeText(this, "Error showing date picker", Toast.LENGTH_SHORT).show();
        }
    }

    private void bookRoom(Room room, String date) {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("LuxeVistaPrefs", MODE_PRIVATE);
            String userEmail = sharedPreferences.getString("userEmail", "");

            if (databaseHelper != null && !userEmail.isEmpty()) {
                User user = databaseHelper.getUserByEmail(userEmail);
                if (user != null) {
                    boolean success = databaseHelper.addBooking(user.getId(), "Room", room.getName(), date, room.getPrice());
                    if (success) {
                        Toast.makeText(this, "Room booked successfully for " + date, Toast.LENGTH_LONG).show();
                        Log.d(TAG, "Room booking successful");
                    } else {
                        Toast.makeText(this, "Failed to book room", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Failed to save booking to database");
                    }
                } else {
                    Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "User not found in database");
                }
            } else {
                Toast.makeText(this, "Please login again", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "No user email or database helper");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error booking room: " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(this, "Error processing booking", Toast.LENGTH_SHORT).show();
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