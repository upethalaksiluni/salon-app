package com.example.sulochanasalon;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager
{
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_USER_ID = "USER_ID";
    private static final String KEY_ADMIN_ID = "ADMIN_ID";
    private static final String KEY_ADMIN_NAME = "ADMIN_NAME";
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void saveAdminSession(int adminId, String adminName) {
        editor.putInt(KEY_ADMIN_ID, adminId);
        editor.putString(KEY_ADMIN_NAME, adminName);
        editor.apply();
    }

    public void saveUserSession(int userId) {
        editor.putInt(KEY_USER_ID, userId);
        editor.apply();
    }

    public int getAdminId() {
        return prefs.getInt(KEY_ADMIN_ID, -1);
    }

    public String getAdminName() {
        return prefs.getString(KEY_ADMIN_NAME, "Admin");
    }

    public int getUserId() {
        return prefs.getInt(KEY_USER_ID, -1);
    }

    public void clearSession() {
        editor.clear().apply();
    }
}

