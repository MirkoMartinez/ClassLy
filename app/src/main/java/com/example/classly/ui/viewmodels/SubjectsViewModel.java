package com.example.classly.ui.viewmodels;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.classly.data.models.SubjectInfo;
import com.example.classly.data.repository.DataRepository;
import java.util.List;

/**
 * ViewModel para gestionar el estado y la lógica de negocio de las asignaturas.
 * Hereda de BaseViewModel utilizando hilos secundarios (ExecutorService) y máquina de estados (UiState).
 */
public class SubjectsViewModel extends BaseViewModel {
    private static final String TAG = "ClassLyDebug";

    private final MutableLiveData<List<SubjectInfo>> subjectsLiveData = new MutableLiveData<>();

    public void loadSubjects() {
        uiStateLiveData.setValue(UiState.LOADING);
        Log.d(TAG, "SubjectsViewModel: Cargando asignaturas en hilo secundario.");

        executorService.execute(() -> {
            try {
                Thread.sleep(150);
                List<SubjectInfo> subjects = DataRepository.getInstance().getSubjects();
                subjectsLiveData.postValue(subjects);
                uiStateLiveData.postValue(UiState.SUCCESS);
                Log.i(TAG, "SubjectsViewModel: Asignaturas cargadas exitosamente (" + subjects.size() + " elementos).");
            } catch (Exception e) {
                Log.e(TAG, "SubjectsViewModel: Error cargando asignaturas", e);
                uiStateLiveData.postValue(UiState.ERROR);
                errorMessageLiveData.postValue(e.getMessage());
            }
        });
    }

    public LiveData<List<SubjectInfo>> getSubjects() {
        return subjectsLiveData;
    }

    public void addSubject(SubjectInfo subjectInfo) {
        uiStateLiveData.setValue(UiState.LOADING);
        Log.d(TAG, "SubjectsViewModel: Añadiendo asignatura " + subjectInfo.getName());

        executorService.execute(() -> {
            try {
                DataRepository.getInstance().addSubject(subjectInfo);
                loadSubjects();
                Log.i(TAG, "SubjectsViewModel: Asignatura añadida con éxito.");
            } catch (Exception e) {
                Log.e(TAG, "SubjectsViewModel: Error al añadir asignatura", e);
                uiStateLiveData.postValue(UiState.ERROR);
                errorMessageLiveData.postValue(e.getMessage());
            }
        });
    }

    public void updateSubject(SubjectInfo subjectInfo) {
        uiStateLiveData.setValue(UiState.LOADING);
        Log.d(TAG, "SubjectsViewModel: Actualizando asignatura ID " + subjectInfo.getId());

        executorService.execute(() -> {
            try {
                DataRepository.getInstance().updateSubject(subjectInfo);
                loadSubjects();
                Log.i(TAG, "SubjectsViewModel: Asignatura actualizada con éxito.");
            } catch (Exception e) {
                Log.e(TAG, "SubjectsViewModel: Error al actualizar asignatura", e);
                uiStateLiveData.postValue(UiState.ERROR);
                errorMessageLiveData.postValue(e.getMessage());
            }
        });
    }

    public void deleteSubject(String id) {
        uiStateLiveData.setValue(UiState.LOADING);
        Log.d(TAG, "SubjectsViewModel: Eliminando asignatura ID " + id);

        executorService.execute(() -> {
            try {
                DataRepository.getInstance().deleteSubject(id);
                loadSubjects();
                Log.i(TAG, "SubjectsViewModel: Asignatura eliminada con éxito.");
            } catch (Exception e) {
                Log.e(TAG, "SubjectsViewModel: Error al eliminar asignatura", e);
                uiStateLiveData.postValue(UiState.ERROR);
                errorMessageLiveData.postValue(e.getMessage());
            }
        });
    }
}
