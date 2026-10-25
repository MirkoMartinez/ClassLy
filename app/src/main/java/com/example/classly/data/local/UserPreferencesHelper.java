package com.example.classly.data.local;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Utilidad de persistencia ligera basada en SharedPreferences.
 * Permite almacenar configuraciones de usuario, estados de sesión y preferencias del sistema
 * (cumpliendo con el requisito obligatorio de la entrega final).
 */
public class UserPreferencesHelper {
    private static final String PREF_NAME = "ClassLyUserPrefs";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_DARK_MODE = "dark_mode_enabled";
    private static final String KEY_SESSION_TOKEN = "session_token";

    private final SharedPreferences sharedPreferences;

    public UserPreferencesHelper(Context context) {
        this.sharedPreferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveUserName(String name) {
        sharedPreferences.edit().putString(KEY_USER_NAME, name).apply();
    }

    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, "Estudiante");
    }

    public void setDarkModeEnabled(boolean enabled) {
        sharedPreferences.edit().putBoolean(KEY_DARK_MODE, enabled).apply();
    }

    public boolean isDarkModeEnabled() {
        return sharedPreferences.getBoolean(KEY_DARK_MODE, false);
    }

    public void saveSessionToken(String token) {
        sharedPreferences.edit().putString(KEY_SESSION_TOKEN, token).apply();
    }

    public String getSessionToken() {
        return sharedPreferences.getString(KEY_SESSION_TOKEN, "");
    }

    public void clearSession() {
        sharedPreferences.edit().clear().apply();
    }
}
