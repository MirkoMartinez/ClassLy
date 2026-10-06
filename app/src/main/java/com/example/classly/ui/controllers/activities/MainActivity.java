package com.example.classly.ui.controllers.activities;

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
import com.example.classly.R;
import com.example.classly.databinding.ActivityMainBinding;
import com.example.classly.ui.controllers.fragments.ExamsFragment;
import com.example.classly.ui.controllers.fragments.CalendarFragment;
import com.example.classly.ui.controllers.fragments.HomeFragment;
import com.example.classly.ui.controllers.fragments.LoginFragment;
import com.example.classly.ui.controllers.fragments.ScheduleFragment;
import com.example.classly.ui.controllers.fragments.SubjectsFragment;
import com.example.classly.utils.NotificationHelper;
import com.example.classly.utils.ExamReminderWorker;
import java.util.concurrent.TimeUnit;

/**
 * Actividad principal (Hosting Activity) de la aplicación ClassLy.
 * Administra la navegación mediante Fragments, la barra de navegación inferior (BottomNavigationView),
 * la solicitud de permisos de notificaciones y la programación de recordatorios con WorkManager.
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final int REQUEST_CODE_POST_NOTIFICATIONS = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inicializar canal de notificaciones y verificar permisos en Android 13+
        NotificationHelper.createNotificationChannel(this);
        checkNotificationPermission();

        // Mostrar inicialmente el fragmento de inicio de sesión (Login)
        showFragment(new LoginFragment(), false);

        // Configurar la navegación de la barra inferior (Bottom Navigation)
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
            } else if (id == R.id.nav_calendar) {
                showFragment(new CalendarFragment(), false);
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null; // Prevenir fugas de memoria con ViewBinding
    }

    /**
     * Reemplaza el fragmento actual en el contenedor principal.
     *
     * @param fragment        Nuevo fragmento a mostrar.
     * @param addToBackStack  Indica si se debe añadir a la pila de retroceso (backstack).
     */
    public void showFragment(Fragment fragment, boolean addToBackStack) {
        var transaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    /**
     * Muestra u oculta la barra de navegación inferior.
     *
     * @param visible True para mostrar, false para ocultar (ej. en pantallas de Login/Registro).
     */
    public void setBottomNavVisibility(boolean visible) {
        binding.bottomNavigation.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
    
    /**
     * Realiza la transición al flujo principal de la app tras iniciar sesión o registrarse con éxito.
     */
    public void navigateToMain() {
        setBottomNavVisibility(true);
        binding.bottomNavigation.setSelectedItemId(R.id.nav_home);
        showFragment(new HomeFragment(), false);
        scheduleExamNotification("Matemáticas II", 5);
    }

    /**
     * Verifica y solicita el permiso POST_NOTIFICATIONS requerido en Android 13 (Tiramisu) y superiores.
     */
    private void checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE_POST_NOTIFICATIONS);
            }
        }
    }

    /**
     * Programa una notificación diferida para recordar un examen usando WorkManager.
     *
     * @param examName     Nombre del examen.
     * @param delaySeconds Retraso en segundos antes de disparar la notificación.
     */
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
