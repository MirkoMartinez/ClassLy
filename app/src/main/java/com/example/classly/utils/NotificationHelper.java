package com.example.classly.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import com.example.classly.R;

/**
 * Clase de utilidad para la configuración y creación del canal de notificaciones
 * requerido en Android 8.0 (API 26) y superiores.
 */
public class NotificationHelper {
    public static final String CHANNEL_ID = "EXAM_REMINDERS";

    /**
     * Crea el canal de notificaciones para recordatorios de exámenes si la versión de Android lo requiere.
     *
     * @param context Contexto de la aplicación.
     */
    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.notification_channel_name);
            String description = context.getString(R.string.notification_channel_desc);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}
