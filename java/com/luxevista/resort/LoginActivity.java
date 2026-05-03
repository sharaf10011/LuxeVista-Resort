package com.luxevista.resort;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private TextInputEditText etEmail, etPassword;
    private Button btnLogin;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_login);
            Log.d(TAG, "Layout set successfully");

            initViews();
            setupClickListeners();

        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(this, "Error loading login screen", Toast.LENGTH_SHORT).show();
        }
    }

    private void initViews() {
        try {
            etEmail = findViewById(R.id.etEmail);
            etPassword = findViewById(R.id.etPassword);
            btnLogin = findViewById(R.id.btnLogin);

            databaseHelper = new DatabaseHelper(this);

            Log.d(TAG, "Views initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing views: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupClickListeners() {
        try {
            btnLogin.setOnClickListener(v -> {
                Log.d(TAG, "Login button clicked");
                loginUser();
            });

            // Handle register link
            findViewById(R.id.tvRegister).setOnClickListener(v -> {
                Log.d(TAG, "Register link clicked");
                try {
                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                } catch (Exception e) {
                    Log.e(TAG, "Error starting RegisterActivity: " + e.getMessage());
                    Toast.makeText(this, "Error opening registration", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "Error setting up click listeners: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loginUser() {
        try {
            String email = "";
            String password = "";

            if (etEmail != null && etEmail.getText() != null) {
                email = etEmail.getText().toString().trim();
            }

            if (etPassword != null && etPassword.getText() != null) {
                password = etPassword.getText().toString().trim();
            }

            Log.d(TAG, "Attempting login with email: " + email);

            // Validation
            if (TextUtils.isEmpty(email)) {
                showError("Email is required");
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showError("Enter a valid email");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                showError("Password is required");
                return;
            }

            if (password.length() < 6) {
                showError("Password must be at least 6 characters");
                return;
            }

            // Attempt login
            if (databaseHelper != null && databaseHelper.loginUser(email, password)) {
                Log.d(TAG, "Login successful");

                // Save login state
                SharedPreferences sharedPreferences = getSharedPreferences("LuxeVistaPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLoggedIn", true);
                editor.putString("userEmail", email);
                editor.apply();

                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();

                // Navigate to MainActivity
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            } else {
                Log.d(TAG, "Login failed");
                showError("Invalid email or password");
            }

        } catch (Exception e) {
            Log.e(TAG, "Error during login: " + e.getMessage());
            e.printStackTrace();
            showError("Login failed. Please try again.");
        }
    }

    private void showError(String message) {
        try {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Error shown: " + message);
        } catch (Exception e) {
            Log.e(TAG, "Error showing toast: " + e.getMessage());
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