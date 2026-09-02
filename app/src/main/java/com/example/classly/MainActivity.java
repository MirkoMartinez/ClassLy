package com.example.classly;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import com.example.classly.databinding.ActivityMainBinding;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final int REQUEST_CODE_POST_NOTIFICATIONS = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize notification channel
        NotificationHelper.createNotificationChannel(this);
        
        // Request permission for Android 13+
        checkNotificationPermission();

        // Initial state: Login
        showFragment(new LoginFragment(), false);

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                showFragment(new HomeFragment(), false);
                return true;
            } else if (id == R.id.nav_schedule) {
                showFragment(new ScheduleFragment(), false);
                return true;
            } else if (id == R.id.nav_exams) {
                showFragment(new ExamsFragment(), false);
                return true;
            } else if (id == R.id.nav_subjects) {
                showFragment(new SubjectsFragment(), false);
                return true;
            }
            return false;
        });
    }

    public void showFragment(Fragment fragment, boolean addToBackStack) {
        var transaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    public void setBottomNavVisibility(boolean visible) {
        binding.bottomNavigation.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
    
    public void navigateToMain() {
        setBottomNavVisibility(true);
        binding.bottomNavigation.setSelectedItemId(R.id.nav_home);
        showFragment(new HomeFragment(), false);
        
        // Ejemplo: Programar una notificación de prueba para 5 segundos después de entrar
        scheduleExamNotification("Matemáticas II", 5);
    }

    private void checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE_POST_NOTIFICATIONS);
            }
        }
    }

    public void scheduleExamNotification(String examName, long delaySeconds) {
        Data inputData = new Data.Builder()
                .putString("exam_name", examName)
                .build();

        OneTimeWorkRequest notificationRequest = new OneTimeWorkRequest.Builder(ExamReminderWorker.class)
                .setInitialDelay(delaySeconds, TimeUnit.SECONDS)
                .setInputData(inputData)
                .build();

        WorkManager.getInstance(this).enqueue(notificationRequest);
    }
}
