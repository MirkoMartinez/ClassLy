package com.example.classly.ui.viewmodels;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.classly.data.models.AssignmentInfo;
import com.example.classly.data.models.ExamInfo;
import com.example.classly.data.repository.DataRepository;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ViewModel para el CalendarFragment. Hereda de BaseViewModel gestionando de forma asincrónica
 * los exámenes y trabajos prácticos filtrados por fecha.
 */
public class CalendarViewModel extends BaseViewModel {
    private static final String TAG = "ClassLyDebug";

    private final MutableLiveData<List<ExamInfo>> examsForDateLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<AssignmentInfo>> assignmentsForDateLiveData = new MutableLiveData<>();

    public void loadEventsForDate(String dateStr) {
        uiStateLiveData.setValue(UiState.LOADING);
        Log.d(TAG, "CalendarViewModel: Cargando eventos para la fecha " + dateStr + " en hilo secundario.");

        executorService.execute(() -> {
            try {
                Thread.sleep(150);
                List<ExamInfo> filteredExams = DataRepository.getInstance().getExams().stream()
                        .filter(exam -> dateStr.equals(exam.getDate()))
                        .collect(Collectors.toList());
                examsForDateLiveData.postValue(filteredExams);

                List<AssignmentInfo> filteredAssignments = DataRepository.getInstance().getAssignments().stream()
                        .filter(a -> dateStr.equals(a.getDate()))
                        .collect(Collectors.toList());
                assignmentsForDateLiveData.postValue(filteredAssignments);

                uiStateLiveData.postValue(UiState.SUCCESS);
                Log.i(TAG, "CalendarViewModel: Eventos cargados para la fecha " + dateStr);
            } catch (Exception e) {
                Log.e(TAG, "CalendarViewModel: Error cargando eventos de calendario", e);
                uiStateLiveData.postValue(UiState.ERROR);
                errorMessageLiveData.postValue(e.getMessage());
            }
        });
    }

    public LiveData<List<ExamInfo>> getExamsForDate() {
        return examsForDateLiveData;
    }

    public LiveData<List<AssignmentInfo>> getAssignmentsForDate() {
        return assignmentsForDateLiveData;
    }

    public void deleteExam(String id, String currentDateStr) {
        uiStateLiveData.setValue(UiState.LOADING);
        executorService.execute(() -> {
            try {
                DataRepository.getInstance().deleteExam(id);
                loadEventsForDate(currentDateStr);
            } catch (Exception e) {
                Log.e(TAG, "CalendarViewModel: Error eliminando examen", e);
                uiStateLiveData.postValue(UiState.ERROR);
                errorMessageLiveData.postValue(e.getMessage());
            }
        });
    }

    public void deleteAssignment(String id, String currentDateStr) {
        uiStateLiveData.setValue(UiState.LOADING);
        executorService.execute(() -> {
            try {
                DataRepository.getInstance().deleteAssignment(id);
                loadEventsForDate(currentDateStr);
            } catch (Exception e) {
                Log.e(TAG, "CalendarViewModel: Error eliminando TP", e);
                uiStateLiveData.postValue(UiState.ERROR);
                errorMessageLiveData.postValue(e.getMessage());
            }
        });
    }
}
