package com.luxevista.resort;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

public class ServiceReservationActivity extends AppCompatActivity implements ServiceAdapter.OnServiceClickListener {
    private static final String TAG = "ServiceReservationActivity";
    private MaterialToolbar toolbar;
    private RecyclerView recyclerViewServices;
    private ServiceAdapter serviceAdapter;
    private List<Service> serviceList;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_service_reservation);
            Log.d(TAG, "ServiceReservation layout set successfully");

            initViews();
            setupToolbar();
            setupRecyclerView();
            loadServices();

            databaseHelper = new DatabaseHelper(this);
            Log.d(TAG, "ServiceReservationActivity initialized successfully");

        } catch (Exception e) {
            Log.e(TAG, "Error in ServiceReservationActivity onCreate: " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(this, "Error loading services", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initViews() {
        try {
            toolbar = findViewById(R.id.toolbar);
            recyclerViewServices = findViewById(R.id.recyclerViewServices);
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
                    getSupportActionBar().setTitle("Reserve Services");
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
            serviceList = new ArrayList<>();
            serviceAdapter = new ServiceAdapter(serviceList, this);

            if (recyclerViewServices != null) {
                recyclerViewServices.setLayoutManager(new LinearLayoutManager(this));
                recyclerViewServices.setAdapter(serviceAdapter);
            }
            Log.d(TAG, "RecyclerView setup complete");
        } catch (Exception e) {
            Log.e(TAG, "Error setting up RecyclerView: " + e.getMessage());
            throw e;
        }
    }

    private void loadServices() {
        try {
            serviceList.clear();

            // Using simple drawable resource IDs that exist by default
            serviceList.add(new Service("Spa Treatment", "Relaxing massage and wellness treatments with ocean view.", 120.0, R.drawable.spa_treatment, "Wellness"));
            serviceList.add(new Service("Fine Dining", "Exclusive dining experience with world-class cuisine and wine pairing.", 89.0, R.drawable.fine_dining, "Dining"));
            serviceList.add(new Service("Poolside Cabana", "Private cabana rental with premium service and refreshments.", 75.0, R.drawable.poolside_cabana, "Recreation"));
            serviceList.add(new Service("Guided Beach Tour", "Professional guided tour of local beaches and marine life.", 65.0, R.drawable.beach_tour, "Activity"));
            serviceList.add(new Service("Yoga Session", "Private yoga session on the beach with certified instructor.", 45.0, R.drawable.yoga_session, "Wellness"));
            serviceList.add(new Service("Sunset Cruise", "Romantic sunset cruise with champagne and gourmet snacks.", 150.0, R.drawable.sunset_cruise, "Activity"));

            if (serviceAdapter != null) {
                serviceAdapter.notifyDataSetChanged();
            }
            Log.d(TAG, "Services loaded: " + serviceList.size());
        } catch (Exception e) {
            Log.e(TAG, "Error loading services: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceClick(Service service) {
        try {
            Log.d(TAG, "Service clicked: " + service.getName());
            showDateTimePicker(service);
        } catch (Exception e) {
            Log.e(TAG, "Error handling service click: " + e.getMessage());
            Toast.makeText(this, "Error reserving service", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDateTimePicker(Service service) {
        try {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        try {
                            TimePickerDialog timePickerDialog = new TimePickerDialog(
                                    this,
                                    (timeView, hourOfDay, minute) -> {
                                        try {
                                            Calendar selectedDateTime = Calendar.getInstance();
                                            selectedDateTime.set(year, month, dayOfMonth, hourOfDay, minute);

                                            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                                            String reservationDateTime = dateTimeFormat.format(selectedDateTime.getTime());

                                            reserveService(service, reservationDateTime);
                                        } catch (Exception e) {
                                            Log.e(TAG, "Error processing time selection: " + e.getMessage());
                                            Toast.makeText(this, "Error processing time", Toast.LENGTH_SHORT).show();
                                        }
                                    },
                                    calendar.get(Calendar.HOUR_OF_DAY),
                                    calendar.get(Calendar.MINUTE),
                                    true
                            );
                            timePickerDialog.show();
                        } catch (Exception e) {
                            Log.e(TAG, "Error showing time picker: " + e.getMessage());
                            Toast.makeText(this, "Error showing time picker", Toast.LENGTH_SHORT).show();
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

    private void reserveService(Service service, String dateTime) {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("LuxeVistaPrefs", MODE_PRIVATE);
            String userEmail = sharedPreferences.getString("userEmail", "");

            if (databaseHelper != null && !userEmail.isEmpty()) {
                User user = databaseHelper.getUserByEmail(userEmail);
                if (user != null) {
                    boolean success = databaseHelper.addBooking(user.getId(), "Service", service.getName(), dateTime, service.getPrice());
                    if (success) {
                        Toast.makeText(this, "Service reserved successfully for " + dateTime, Toast.LENGTH_LONG).show();
                        Log.d(TAG, "Service reservation successful");
                    } else {
                        Toast.makeText(this, "Failed to reserve service", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Failed to save reservation to database");
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
            Log.e(TAG, "Error reserving service: " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(this, "Error processing reservation", Toast.LENGTH_SHORT).show();
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