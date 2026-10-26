package com.example.classly.ui.viewmodels;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.classly.data.models.ClassInfo;
import com.example.classly.data.models.ExamInfo;
import com.example.classly.data.repository.DataRepository;
import java.util.List;

/**
 * ViewModel para el HomeFragment. Hereda de BaseViewModel aplicando asincronismo con ExecutorService
 * y máquina de estados (UiState), eliminando accesos síncronos directos al repositorio desde la vista.
 */
public class HomeViewModel extends BaseViewModel {
    private static final String TAG = "ClassLyDebug";

    private final MutableLiveData<ClassInfo> nextClassLiveData = new MutableLiveData<>();
    private final MutableLiveData<ExamInfo> nearestExamLiveData = new MutableLiveData<>();

    public void loadHomeData() {
        uiStateLiveData.setValue(UiState.LOADING);
        Log.d(TAG, "HomeViewModel: Cargando datos de inicio en hilo secundario.");

        executorService.execute(() -> {
            try {
                Thread.sleep(150);
                List<ClassInfo> todayClasses = DataRepository.getInstance().getClassesByDay(0);
                ClassInfo nextClass = todayClasses.isEmpty() ? null : todayClasses.get(0);
                nextClassLiveData.postValue(nextClass);

                List<ExamInfo> exams = DataRepository.getInstance().getExams();
                ExamInfo nearest = null;
                int minDays = Integer.MAX_VALUE;
                for (ExamInfo exam : exams) {
                    int days = exam.getDaysRemaining();
                    if (days >= 0 && days < minDays) {
                        minDays = days;
                        nearest = exam;
                    }
                }
                nearestExamLiveData.postValue(nearest);

                uiStateLiveData.postValue(UiState.SUCCESS);
                Log.i(TAG, "HomeViewModel: Datos de inicio cargados con éxito.");
            } catch (Exception e) {
                Log.e(TAG, "HomeViewModel: Error cargando datos de inicio", e);
                uiStateLiveData.postValue(UiState.ERROR);
                errorMessageLiveData.postValue(e.getMessage());
            }
        });
    }

    public LiveData<ClassInfo> getNextClass() {
        return nextClassLiveData;
    }

    public LiveData<ExamInfo> getNearestExam() {
        return nearestExamLiveData;
    }
}
