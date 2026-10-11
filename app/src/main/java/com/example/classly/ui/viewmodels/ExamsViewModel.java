package com.example.classly.ui.viewmodels;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.classly.data.models.ExamInfo;
import com.example.classly.data.repository.DataRepository;
import java.util.List;

/**
 * ViewModel para gestionar el estado y la lógica de negocio de los exámenes y evaluaciones.
 * Hereda de BaseViewModel utilizando hilos secundarios (ExecutorService) y máquina de estados (UiState).
 */
public class ExamsViewModel extends BaseViewModel {
    private static final String TAG = "ClassLyDebug";

    private final MutableLiveData<List<ExamInfo>> examsLiveData = new MutableLiveData<>();

    public void loadExams() {
        uiStateLiveData.setValue(UiState.LOADING);
        Log.d(TAG, "ExamsViewModel: Cargando exámenes en hilo secundario.");

        executorService.execute(() -> {
            try {
                Thread.sleep(150);
                List<ExamInfo> exams = DataRepository.getInstance().getExams();
                examsLiveData.postValue(exams);
                uiStateLiveData.postValue(UiState.SUCCESS);
                Log.i(TAG, "ExamsViewModel: Exámenes cargados exitosamente (" + exams.size() + " elementos).");
            } catch (Exception e) {
                Log.e(TAG, "ExamsViewModel: Error cargando exámenes", e);
                uiStateLiveData.postValue(UiState.ERROR);
                errorMessageLiveData.postValue(e.getMessage());
            }
        });
    }

    public LiveData<List<ExamInfo>> getExams() {
        return examsLiveData;
    }

    public void addExam(ExamInfo examInfo) {
        uiStateLiveData.setValue(UiState.LOADING);
        Log.d(TAG, "ExamsViewModel: Añadiendo examen " + examInfo.getSubject());

        executorService.execute(() -> {
            try {
                DataRepository.getInstance().addExam(examInfo);
                loadExams();
                Log.i(TAG, "ExamsViewModel: Examen añadido con éxito.");
            } catch (Exception e) {
                Log.e(TAG, "ExamsViewModel: Error al añadir examen", e);
                uiStateLiveData.postValue(UiState.ERROR);
                errorMessageLiveData.postValue(e.getMessage());
            }
        });
    }

    public void updateExam(ExamInfo examInfo) {
        uiStateLiveData.setValue(UiState.LOADING);
        Log.d(TAG, "ExamsViewModel: Actualizando examen ID " + examInfo.getId());

        executorService.execute(() -> {
            try {
                DataRepository.getInstance().updateExam(examInfo);
                loadExams();
                Log.i(TAG, "ExamsViewModel: Examen actualizado con éxito.");
            } catch (Exception e) {
                Log.e(TAG, "ExamsViewModel: Error al actualizar examen", e);
                uiStateLiveData.postValue(UiState.ERROR);
                errorMessageLiveData.postValue(e.getMessage());
            }
        });
    }

    public void deleteExam(String id) {
        uiStateLiveData.setValue(UiState.LOADING);
        Log.d(TAG, "ExamsViewModel: Eliminando examen ID " + id);

        executorService.execute(() -> {
            try {
                DataRepository.getInstance().deleteExam(id);
                loadExams();
                Log.i(TAG, "ExamsViewModel: Examen eliminado con éxito.");
            } catch (Exception e) {
                Log.e(TAG, "ExamsViewModel: Error al eliminar examen", e);
                uiStateLiveData.postValue(UiState.ERROR);
                errorMessageLiveData.postValue(e.getMessage());
            }
        });
    }
}
