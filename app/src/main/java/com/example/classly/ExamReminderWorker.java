package com.example.classly;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class ExamReminderWorker extends Worker {

    public ExamReminderWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String examName = getInputData().getString("exam_name");
        if (examName == null) examName = "Examen";

        showNotification(examName);
        return Result.success();
    }

    private void showNotification(String examName) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), NotificationHelper.CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("¡Prepárate para tu examen!")
                .setContentText("Tu examen de " + examName + " es pronto. ¡No olvides estudiar!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        try {
            // Un ID único para cada notificación
            notificationManager.notify((int) System.currentTimeMillis(), builder.build());
        } catch (SecurityException e) {
            // Manejar falta de permisos en Android 13+
        }
    }
}
