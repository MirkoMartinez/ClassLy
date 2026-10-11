package com.example.classly.ui.viewmodels;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.classly.data.models.ClassInfo;
import com.example.classly.data.repository.DataRepository;
import java.util.List;

/**
 * ViewModel para gestionar el estado y la lógica de negocio del horario de clases.
 * Hereda de BaseViewModel demostrando asincronismo con ExecutorService (hilos secundarios)
 * y máquina de estados (UiState) con manejo estricto de excepciones (try/catch) y Logcat.
 */
public class ScheduleViewModel extends BaseViewModel {
    private static final String TAG = "ClassLyDebug";

    private final MutableLiveData<List<ClassInfo>> classesLiveData = new MutableLiveData<>();
    private int currentDayIndex = 0;

    /**
     * Carga las clases correspondientes a un día específico en un hilo secundario.
     */
    public void loadClassesForDay(int dayIndex) {
        this.currentDayIndex = dayIndex;
        uiStateLiveData.setValue(UiState.LOADING);
        Log.d(TAG, "ScheduleViewModel: Cargando clases para el día index " + dayIndex + " en hilo secundario.");

        executorService.execute(() -> {
            try {
                Thread.sleep(150); // Simular carga asincrónica fluida
                List<ClassInfo> classes = DataRepository.getInstance().getClassesByDay(dayIndex);
                classesLiveData.postValue(classes);
                uiStateLiveData.postValue(UiState.SUCCESS);
                Log.i(TAG, "ScheduleViewModel: Clases cargadas exitosamente (" + classes.size() + " elementos).");
            } catch (Exception e) {
                Log.e(TAG, "ScheduleViewModel: Error cargando clases", e);
                uiStateLiveData.postValue(UiState.ERROR);
                errorMessageLiveData.postValue(e.getMessage());
            }
        });
    }

    /**
     * Retorna el LiveData observable con la lista de clases del día actual.
     */
    public LiveData<List<ClassInfo>> getClasses() {
        return classesLiveData;
    }

    /**
     * Agrega una nueva clase en segundo plano y refresca el LiveData.
     */
    public void addClass(ClassInfo classInfo) {
        uiStateLiveData.setValue(UiState.LOADING);
        Log.d(TAG, "ScheduleViewModel: Añadiendo clase " + classInfo.getSubject());

        executorService.execute(() -> {
            try {
                DataRepository.getInstance().addClass(classInfo);
                loadClassesForDay(currentDayIndex);
                Log.i(TAG, "ScheduleViewModel: Clase añadida con éxito.");
            } catch (Exception e) {
                Log.e(TAG, "ScheduleViewModel: Error al añadir clase", e);
                uiStateLiveData.postValue(UiState.ERROR);
                errorMessageLiveData.postValue(e.getMessage());
            }
        });
    }

    /**
     * Actualiza una clase existente en segundo plano y refresca el LiveData.
     */
    public void updateClass(ClassInfo classInfo) {
        uiStateLiveData.setValue(UiState.LOADING);
        Log.d(TAG, "ScheduleViewModel: Actualizando clase ID " + classInfo.getId());

        executorService.execute(() -> {
            try {
                DataRepository.getInstance().updateClass(classInfo);
                loadClassesForDay(currentDayIndex);
                Log.i(TAG, "ScheduleViewModel: Clase actualizada con éxito.");
            } catch (Exception e) {
                Log.e(TAG, "ScheduleViewModel: Error al actualizar clase", e);
                uiStateLiveData.postValue(UiState.ERROR);
                errorMessageLiveData.postValue(e.getMessage());
            }
        });
    }

    /**
     * Elimina una clase por ID en segundo plano y refresca el LiveData.
     */
    public void deleteClass(String id) {
        uiStateLiveData.setValue(UiState.LOADING);
        Log.d(TAG, "ScheduleViewModel: Eliminando clase ID " + id);

        executorService.execute(() -> {
            try {
                DataRepository.getInstance().deleteClass(id);
                loadClassesForDay(currentDayIndex);
                Log.i(TAG, "ScheduleViewModel: Clase eliminada con éxito.");
            } catch (Exception e) {
                Log.e(TAG, "ScheduleViewModel: Error al eliminar clase", e);
                uiStateLiveData.postValue(UiState.ERROR);
                errorMessageLiveData.postValue(e.getMessage());
            }
        });
    }
}
