package com.example.sulochanasalon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Salon.db";
    private static final int DATABASE_VERSION = 11;

    // Users Table
    private static final String TABLE_USERS = "Users";
    public static final String COLUMN_USERID = "UserID";
    public static final String COLUMN_FULLNAME = "FullName";
    public static final String COLUMN_EMAIL = "Email";
    public static final String COLUMN_PASSWORD = "Password";
    public static final String COLUMN_ROLE = "Role";
    public static final String COLUMN_PHONE = "Phone";
    public static final String COLUMN_GENDER = "Gender";
    public static final String COLUMN_DOB = "DateOfBirth";
    public static final String COLUMN_ADDRESS = "Address";
    public static final String COLUMN_PROFILE_PICTURE = "ProfilePicture";

    // Appointments Table
    private static final String TABLE_APPOINTMENTS = "appointments";
    public static final String COLUMN_APPOINTMENT_ID = "AppointmentID";
    public static final String COLUMN_DATE = "Date";
    public static final String COLUMN_TIME = "Time";
    public static final String COLUMN_SERVICE = "Service";
    public static final String COLUMN_STYLIST = "Stylist";
    public static final String COLUMN_STATUS = "Status";
    public static final String COLUMN_CLIENT_ID = "ClientID";

    // Services Table
    private static final String TABLE_SERVICES = "Services";
    public static final String COLUMN_SERVICE_ID = "ServiceID";
    public static final String COLUMN_SERVICE_NAME = "ServiceName";
    public static final String COLUMN_PARENT_SERVICE_ID = "ParentServiceID";
    public static final String COLUMN_DESCRIPTION = "Description";
    public static final String COLUMN_PRICE = "Price";
    public static final String COLUMN_IMAGE_URL = "ImageUrl";

    // Payment Table
    private static final String TABLE_PAYMENTS = "Payments";
    public static final String COLUMN_PAYMENT_ID = "PaymentID";
    public static final String COLUMN_PAYMENT_AMOUNT = "Amount";
    public static final String COLUMN_PAYMENT_STATUS = "Status";
    public static final String COLUMN_FEEDBACK = "Feedback";

    // Notifications columns
    public static final String TABLE_NOTIFICATIONS = "Notifications";
    public static final String COLUMN_NOTIFICATION_ID = "NotificationID";
    public static final String COLUMN_MESSAGE = "Message";
    public static final String COLUMN_NOTIFICATION_DATE = "NotificationDate";
    public static final String COLUMN_USER_ID = "UserID";

    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users Table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " (" +
                COLUMN_USERID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FULLNAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_ROLE + " TEXT, " +
                COLUMN_PHONE + " TEXT, " +
                COLUMN_GENDER + " TEXT, " +
                COLUMN_DOB + " TEXT, " +
                COLUMN_ADDRESS + " TEXT, " +
                COLUMN_PROFILE_PICTURE + " TEXT);");

        // Pre-fill Admin users (if needed for testing or first-time app use)
        db.execSQL("INSERT INTO " + TABLE_USERS + " (FullName, Email, Password, Role, Phone) VALUES " +
                "('Admin1', 'admin1@example.com', 'Admin123', 'Admin', '1234567890'), " +
                "('Admin2', 'admin2@example.com', 'Admin1234', 'Admin', '0987654321');");

        // Create Appointments Table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_APPOINTMENTS + " (" +
                COLUMN_APPOINTMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATE + " TEXT NOT NULL, " +
                COLUMN_TIME + " TEXT NOT NULL, " +
                COLUMN_SERVICE + " TEXT NOT NULL, " +
                COLUMN_STYLIST + " TEXT NOT NULL, " +
                COLUMN_STATUS + " TEXT DEFAULT 'Pending', " +
                COLUMN_CLIENT_ID + " INTEGER NOT NULL, " +
                "FOREIGN KEY (" + COLUMN_CLIENT_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USERID + ") ON DELETE CASCADE);");

        // Create Services Table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_SERVICES + " (" +
                COLUMN_SERVICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SERVICE_NAME + " TEXT NOT NULL, " +
                COLUMN_PARENT_SERVICE_ID + " INTEGER, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_PRICE + " REAL, " +
                COLUMN_IMAGE_URL + " TEXT, " +
                "FOREIGN KEY (" + COLUMN_PARENT_SERVICE_ID + ") REFERENCES " + TABLE_SERVICES + "(" + COLUMN_SERVICE_ID + ") ON DELETE CASCADE);");

        db.execSQL("CREATE TABLE IF NOT EXISTS Payments (" +
                "PaymentID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "AppointmentID INTEGER NOT NULL, " +
                "Amount REAL NOT NULL, " +
                "Status TEXT DEFAULT 'Pending', " +
                "Feedback TEXT, " +  // Make sure this column exists
                "FOREIGN KEY (AppointmentID) REFERENCES appointments(AppointmentID) ON DELETE CASCADE);");

        db.execSQL("CREATE TABLE IF NOT EXISTS Notifications (" +
                "NotificationID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Message TEXT NOT NULL, " +
                "NotificationDate TEXT NOT NULL, " +
                "UserID INTEGER NOT NULL, " +
                "FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPOINTMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICES);
        db.execSQL("DROP TABLE IF EXISTS Payments");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATIONS);

        onCreate(db);
    }


    public User loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?", new String[]{email, password});

        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USERID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FULLNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENDER)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DOB)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROFILE_PICTURE))
            );
            cursor.close();
            db.close();
            return user;
        }

        if (cursor != null) cursor.close();
        db.close();
        return null; // Return null if user is not found
    }

    public boolean verifyEmailAndPhone(String email, String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        boolean isMatch = false;

        try {
            // Trim any leading or trailing spaces from email and phone
            email = email.trim();
            phone = phone.trim();

            // Logging for debugging
            Log.d("verifyEmailAndPhone", "Verifying email: " + email + " with phone: " + phone);

            // Query to check if email and phone match in the database
            cursor = db.rawQuery(
                    "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ? AND " + COLUMN_PHONE + " = ?",
                    new String[]{email, phone}
            );

            if (cursor != null && cursor.moveToFirst()) {
                isMatch = true;  // If a match is found
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return isMatch;
    }

    public boolean registerUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Use a single connection for both checking and inserting
        Cursor cursor = null;
        try {
            // Check if email already exists
            cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ?", new String[]{user.getEmail()});
            if (cursor.moveToFirst()) {
                return false; // Email already exists
            }

            // Insert the new user
            ContentValues values = new ContentValues();
            values.put(COLUMN_FULLNAME, user.getFullName());
            values.put(COLUMN_EMAIL, user.getEmail());
            values.put(COLUMN_PASSWORD, user.getPassword());
            values.put(COLUMN_ROLE, user.getRole());
            values.put(COLUMN_PHONE, user.getPhone());
            values.put(COLUMN_GENDER, user.getGender());
            values.put(COLUMN_DOB, user.getDateOfBirth());
            values.put(COLUMN_ADDRESS, user.getAddress());
            values.put(COLUMN_PROFILE_PICTURE, user.getProfilePicture());

            long result = db.insert(TABLE_USERS, null, values);
            if (result == -1) {
                Log.e("DatabaseHelper", "Error inserting user. Possible constraint violation.");
                return false;
            }
            return true;
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error in registerUser: " + e.getMessage());
            return false;
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close(); // Close the cursor
            db.close(); // Close the database after completing the operation
        }
    }

    // Get User by Email
    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Logging for debugging
            Log.d("getUserByEmail", "Fetching user with email: " + email);

            cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ?", new String[]{email});

            if (cursor != null && cursor.moveToFirst()) {
                User user = new User(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USERID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FULLNAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENDER)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DOB)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROFILE_PICTURE))
                );
                cursor.close();
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return null;  // Return null if no user found
    }

    public User getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Query the Users table with the given userId
            cursor = db.query(TABLE_USERS, null, COLUMN_USERID + " = ?",
                    new String[]{String.valueOf(userId)}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                // If user exists, create a User object
                return createUserFromCursor(cursor);
            } else {
                Log.e("DatabaseHelper", "User not found with userId: " + userId);
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error retrieving user by ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return null; // Return null if no user found
    }

    // Delete User
    public boolean deleteUserAccount(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_USERS, COLUMN_EMAIL + " = ?", new String[]{email});
        db.close();
        return rowsAffected > 0;
    }

    // Reset Password
    public boolean resetPassword(String email, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, newPassword);
        int rowsAffected = db.update(TABLE_USERS, values, COLUMN_EMAIL + " = ?", new String[]{email});
        db.close();
        return rowsAffected > 0;
    }

    // Fetch All Users
    public ArrayList<User> fetchUsers() {
        ArrayList<User> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS, null);

        while (cursor.moveToNext()) {
            users.add(createUserFromCursor(cursor));
        }
        cursor.close();
        db.close();
        return users;
    }

    // Helper Method to Create User Object from Cursor
    private User createUserFromCursor(Cursor cursor) {
        return new User(
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USERID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FULLNAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENDER)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DOB)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROFILE_PICTURE))
        );
    }

    // Add Appointment
    public boolean addAppointment(String date, String time, String service, String stylist, String status, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_CLIENT_ID, userId);
        contentValues.put(COLUMN_SERVICE, service);
        contentValues.put(COLUMN_STYLIST, stylist);
        contentValues.put(COLUMN_DATE, date);
        contentValues.put(COLUMN_TIME, time);
        contentValues.put(COLUMN_STATUS, status);

        long result = db.insert(TABLE_APPOINTMENTS, null, contentValues);
        return result != -1;
    }

    // Fetch Unavailable Slots
    public ArrayList<String> fetchUnavailableSlots() {
        ArrayList<String> unavailableSlots = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_DATE + " || ' ' || " + COLUMN_TIME + " AS Slot FROM " + TABLE_APPOINTMENTS + " WHERE " + COLUMN_STATUS + " = 'Confirmed'";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                unavailableSlots.add(cursor.getString(cursor.getColumnIndexOrThrow("Slot")));
            }
            cursor.close();
        }

        return unavailableSlots;
    }

    // Fetch Appointments by Client ID and Date Type
    public ArrayList<String> fetchAppointmentsByDate(int userId, String type) {
        ArrayList<String> appointments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_APPOINTMENTS + " WHERE " + COLUMN_CLIENT_ID + " = ? AND " + COLUMN_DATE +
                (type.equals("past") ? " < " : " >= ") + "DATE('now') ORDER BY " + COLUMN_DATE + " ASC";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String appointment = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_APPOINTMENT_ID)) + " | " +
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)) + " | " +
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME)) + " | " +
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE)) + " | " +
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STYLIST)) + " | " +
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS));
                appointments.add(appointment);
            }
            cursor.close();
        }

        return appointments;
    }

    // Fetch All Appointments
    public ArrayList<String> fetchAppointments() {
        ArrayList<String> appointments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_APPOINTMENTS + " ORDER BY " + COLUMN_DATE + " ASC";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String appointment = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_APPOINTMENT_ID)) + " | " +
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)) + " | " +
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME)) + " | " +
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE)) + " | " +
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STYLIST)) + " | " +
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS));
                appointments.add(appointment);
            }
            cursor.close();
        }

        return appointments;
    }

    // Update Appointment Status
    public void updateAppointmentStatus(int appointmentID, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, status);
        db.update(TABLE_APPOINTMENTS, values, COLUMN_APPOINTMENT_ID + " = ?", new String[]{String.valueOf(appointmentID)});
    }

    public Cursor getAllServices() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_SERVICES, null);
    }

    public List<Service> getAllServicesAsList() {
        List<Service> services = new ArrayList<>();
        Cursor cursor = getAllServices(); // Call your existing method to get the Cursor

        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_ID));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_NAME));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
                    double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE));
                    int parentId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PARENT_SERVICE_ID));

                    // Create a Service object and add it to the list
                    services.add(new Service(id, name, price, description, parentId));
                }
            } finally {
                cursor.close(); // Always close the cursor after use
            }
        }
        return services;
    }

    public boolean addService(String name, String description, double price, String imageUrl, Integer parentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SERVICE_NAME, name);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_IMAGE_URL, imageUrl);
        values.put(COLUMN_PARENT_SERVICE_ID, parentId);

        long result = db.insert(TABLE_SERVICES, null, values);
        return result != -1;
    }

    public boolean updateService(int id, String name, String description, double price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SERVICE_NAME, name);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_PRICE, price);

        return db.update(TABLE_SERVICES, values, COLUMN_SERVICE_ID + " = ?", new String[]{String.valueOf(id)}) > 0;
    }

    // Fetch all services
    public List<Service> fetchAllServices() {
        List<Service> services = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SERVICES, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_NAME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE));
                int parentId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PARENT_SERVICE_ID));

                services.add(new Service(id, name, price, description, parentId));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return services;
    }

    // Delete a service by ID
    public boolean deleteService(int serviceId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_SERVICES, COLUMN_SERVICE_ID + " = ?", new String[]{String.valueOf(serviceId)});
        return rowsDeleted > 0;
    }

    // Add Payment
    public boolean addPayment(int appointmentId, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_APPOINTMENT_ID, appointmentId);
        values.put(COLUMN_PAYMENT_AMOUNT, amount);

        long result = db.insert(TABLE_PAYMENTS, null, values);
        return result != -1;
    }

    // Fetch Payments by User
    public ArrayList<String> fetchPaymentsByUser(int clientId) {
        ArrayList<String> payments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT p." + COLUMN_PAYMENT_ID + ", p." + COLUMN_PAYMENT_AMOUNT + ", p." + COLUMN_PAYMENT_STATUS + ", " +
                "a." + COLUMN_SERVICE + ", a." + COLUMN_DATE + ", a." + COLUMN_TIME + ", p." + COLUMN_FEEDBACK +
                " FROM " + TABLE_PAYMENTS + " p INNER JOIN " + TABLE_APPOINTMENTS + " a " +
                "ON p." + COLUMN_APPOINTMENT_ID + " = a." + COLUMN_APPOINTMENT_ID +
                " WHERE a." + COLUMN_CLIENT_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(clientId)});

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String payment = "Payment ID: " + cursor.getInt(0) +
                        "\nAmount: $" + cursor.getDouble(1) +
                        "\nStatus: " + cursor.getString(2) +
                        "\nService: " + cursor.getString(3) +
                        "\nDate: " + cursor.getString(4) +
                        "\nTime: " + cursor.getString(5) +
                        "\nFeedback: " + (cursor.getString(6) != null ? cursor.getString(6) : "No feedback provided");
                payments.add(payment);
            }
            cursor.close();
        }

        return payments;
    }

    // Update Payment Status
    public boolean updatePaymentStatus(int paymentId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PAYMENT_STATUS, status);

        return db.update(TABLE_PAYMENTS, values, COLUMN_PAYMENT_ID + " = ?", new String[]{String.valueOf(paymentId)}) > 0;
    }

    public boolean addFeedback(int paymentId, String feedback) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FEEDBACK, feedback);

        // Update the feedback for the specific PaymentID
        return db.update(TABLE_PAYMENTS, values, COLUMN_PAYMENT_ID + " = ?", new String[]{String.valueOf(paymentId)}) > 0;
    }

    // Fetch all payments for admin
    public ArrayList<String> fetchAllPayments() {
        ArrayList<String> payments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT p." + COLUMN_PAYMENT_ID + ", p." + COLUMN_PAYMENT_AMOUNT + ", p." + COLUMN_PAYMENT_STATUS + ", " +
                "a." + COLUMN_SERVICE + ", a." + COLUMN_DATE + ", a." + COLUMN_TIME + ", p." + COLUMN_FEEDBACK +
                " FROM " + TABLE_PAYMENTS + " p INNER JOIN " + TABLE_APPOINTMENTS + " a " +
                "ON p." + COLUMN_APPOINTMENT_ID + " = a." + COLUMN_APPOINTMENT_ID;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String payment = "Payment ID: " + cursor.getInt(0) +
                        "\nAmount: $" + cursor.getDouble(1) +
                        "\nStatus: " + cursor.getString(2) +
                        "\nService: " + cursor.getString(3) +
                        "\nDate: " + cursor.getString(4) +
                        "\nTime: " + cursor.getString(5) +
                        "\nFeedback: " + (cursor.getString(6) != null ? cursor.getString(6) : "No feedback provided");
                payments.add(payment);
            }
            cursor.close();
        }

        return payments;
    }

    // Method to get the last inserted appointment ID
    public int getLastInsertedAppointmentId() {
        SQLiteDatabase db = this.getReadableDatabase();
        int lastId = -1;

        // Query to fetch the last inserted AppointmentID
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_APPOINTMENT_ID + " FROM " + TABLE_APPOINTMENTS + " ORDER BY " + COLUMN_APPOINTMENT_ID + " DESC LIMIT 1", null);
        if (cursor != null && cursor.moveToFirst()) {
            lastId = cursor.getInt(0); // Get the first column value, which is the AppointmentID
            cursor.close();
        }
        return lastId; // Return -1 if no appointments exist
    }

    // Insert a notification into the database
    public long insertNotification(String message, String notificationDate, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MESSAGE, message);
        values.put(COLUMN_NOTIFICATION_DATE, notificationDate);
        values.put(COLUMN_USER_ID, userId);  // Foreign key reference to User

        long notificationId = db.insert(TABLE_NOTIFICATIONS, null, values);
        db.close();
        return notificationId;
    }

    public ArrayList<UserNotification> fetchAllNotifications() {
        ArrayList<UserNotification> notifications = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Query to fetch all notifications, ordered by NotificationDate (desc)
            String query = "SELECT * FROM Notifications ORDER BY NotificationDate DESC";
            cursor = db.rawQuery(query, null);

            // Loop through the result set and create UserNotification objects
            while (cursor != null && cursor.moveToNext()) {
                int notificationId = cursor.getInt(cursor.getColumnIndexOrThrow("NotificationID"));
                String message = cursor.getString(cursor.getColumnIndexOrThrow("Message"));
                String notificationDate = cursor.getString(cursor.getColumnIndexOrThrow("NotificationDate"));
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow("UserID"));

                // Create a UserNotification object and add it to the list
                UserNotification notification = new UserNotification(notificationId, message, notificationDate, userId);
                notifications.add(notification);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return notifications;
    }


    public ArrayList<UserNotification> fetchNotificationsForUser(int userId) {
        ArrayList<UserNotification> notifications = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to fetch notifications for the specific user, ordered by date
        String query = "SELECT * FROM " + TABLE_NOTIFICATIONS + " WHERE " + COLUMN_USER_ID + " = ? ORDER BY " + COLUMN_NOTIFICATION_DATE + " DESC";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int notificationId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NOTIFICATION_ID));
                String message = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE));
                String notificationDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTIFICATION_DATE));
                // Pass all 4 parameters to the UserNotification constructor
                int userIdFromDB = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));  // Get userId from DB
                notifications.add(new UserNotification(notificationId, message, notificationDate, userIdFromDB));
            }
            cursor.close();
        }
        db.close();
        return notifications;
    }
}
