package com.luxevista.resort;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "luxevista.db";
    private static final int DATABASE_VERSION = 1;

    // Users table
    private static final String TABLE_USERS = "users";
    private static final String COL_USER_ID = "user_id";
    private static final String COL_EMAIL = "email";
    private static final String COL_PASSWORD = "password";
    private static final String COL_FULL_NAME = "full_name";
    private static final String COL_PHONE = "phone";
    private static final String COL_CREATED_AT = "created_at";

    // Bookings table
    private static final String TABLE_BOOKINGS = "bookings";
    private static final String COL_BOOKING_ID = "booking_id";
    private static final String COL_BOOKING_USER_ID = "user_id";
    private static final String COL_BOOKING_TYPE = "booking_type";
    private static final String COL_BOOKING_TITLE = "title";
    private static final String COL_BOOKING_DATE = "booking_date";
    private static final String COL_BOOKING_STATUS = "status";
    private static final String COL_BOOKING_PRICE = "price";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create users table
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_EMAIL + " TEXT UNIQUE, " +
                COL_PASSWORD + " TEXT, " +
                COL_FULL_NAME + " TEXT, " +
                COL_PHONE + " TEXT, " +
                COL_CREATED_AT + " TEXT)";
        db.execSQL(createUsersTable);

        // Create bookings table
        String createBookingsTable = "CREATE TABLE " + TABLE_BOOKINGS + " (" +
                COL_BOOKING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_BOOKING_USER_ID + " INTEGER, " +
                COL_BOOKING_TYPE + " TEXT, " +
                COL_BOOKING_TITLE + " TEXT, " +
                COL_BOOKING_DATE + " TEXT, " +
                COL_BOOKING_STATUS + " TEXT, " +
                COL_BOOKING_PRICE + " REAL, " +
                "FOREIGN KEY(" + COL_BOOKING_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + "))";
        db.execSQL(createBookingsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // User methods
    public boolean registerUser(String email, String password, String fullName, String phone) {
        if (isEmailExists(email)) {
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_EMAIL, email);
        values.put(COL_PASSWORD, password);
        values.put(COL_FULL_NAME, fullName);
        values.put(COL_PHONE, phone);
        values.put(COL_CREATED_AT, getCurrentDateTime());

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    public boolean loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COL_USER_ID},
                COL_EMAIL + "=? AND " + COL_PASSWORD + "=?",
                new String[]{email, password},
                null, null, null);

        boolean loginSuccess = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return loginSuccess;
    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COL_USER_ID},
                COL_EMAIL + "=?",
                new String[]{email},
                null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                null,
                COL_EMAIL + "=?",
                new String[]{email},
                null, null, null);

        User user = null;
        if (cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID)));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL)));
            user.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(COL_FULL_NAME)));
            user.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE)));
        }
        cursor.close();
        db.close();
        return user;
    }

    // Booking methods
    public boolean addBooking(int userId, String type, String title, String date, double price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_BOOKING_USER_ID, userId);
        values.put(COL_BOOKING_TYPE, type);
        values.put(COL_BOOKING_TITLE, title);
        values.put(COL_BOOKING_DATE, date);
        values.put(COL_BOOKING_STATUS, "Confirmed");
        values.put(COL_BOOKING_PRICE, price);

        long result = db.insert(TABLE_BOOKINGS, null, values);
        db.close();
        return result != -1;
    }

    public List<Booking> getUserBookings(int userId) {
        List<Booking> bookings = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BOOKINGS,
                null,
                COL_BOOKING_USER_ID + "=?",
                new String[]{String.valueOf(userId)},
                null, null, COL_BOOKING_DATE + " DESC");

        while (cursor.moveToNext()) {
            Booking booking = new Booking();
            booking.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_BOOKING_ID)));
            booking.setType(cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOKING_TYPE)));
            booking.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOKING_TITLE)));
            booking.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOKING_DATE)));
            booking.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOKING_STATUS)));
            booking.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_BOOKING_PRICE)));
            bookings.add(booking);
        }
        cursor.close();
        db.close();
        return bookings;
    }

    private String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(new Date());
    }
}
