package com.example.classly.utils;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.example.classly.R;

/**
 * Worker de WorkManager encargado de ejecutar en segundo plano la tarea de mostrar
 * una notificación recordatoria de exámenes próximos en el dispositivo.
 */
public class ExamReminderWorker extends Worker {

    public ExamReminderWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String examName = getInputData().getString("exam_name");
        if (examName == null) examName = getApplicationContext().getString(R.string.default_exam_name);

        showNotification(examName);
        return Result.success();
    }

    /**
     * Construye y emite la notificación local con la información del examen.
     */
    private void showNotification(String examName) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), NotificationHelper.CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(getApplicationContext().getString(R.string.notification_title))
                .setContentText(getApplicationContext().getString(R.string.notification_content, examName))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        try {
            notificationManager.notify((int) System.currentTimeMillis(), builder.build());
        } catch (SecurityException e) {
            // Manejar excepciones por falta de permisos de notificación en Android 13+
        }
    }
}
