package com.example.classly.ui.viewmodels;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.classly.data.models.AssignmentInfo;
import com.example.classly.data.repository.DataRepository;
import java.util.List;

/**
 * ViewModel para gestionar el estado y la lógica de negocio de los trabajos prácticos (TPs).
 * Hereda de BaseViewModel utilizando hilos secundarios (ExecutorService) y máquina de estados (UiState).
 */
public class AssignmentsViewModel extends BaseViewModel {
    private static final String TAG = "ClassLyDebug";

    private final MutableLiveData<List<AssignmentInfo>> assignmentsLiveData = new MutableLiveData<>();

    public void loadAssignments() {
        uiStateLiveData.setValue(UiState.LOADING);
        Log.d(TAG, "AssignmentsViewModel: Cargando TPs en hilo secundario.");

        executorService.execute(() -> {
            try {
                Thread.sleep(150);
                List<AssignmentInfo> assignments = DataRepository.getInstance().getAssignments();
                assignmentsLiveData.postValue(assignments);
                uiStateLiveData.postValue(UiState.SUCCESS);
                Log.i(TAG, "AssignmentsViewModel: TPs cargados exitosamente (" + assignments.size() + " elementos).");
            } catch (Exception e) {
                Log.e(TAG, "AssignmentsViewModel: Error cargando TPs", e);
                uiStateLiveData.postValue(UiState.ERROR);
                errorMessageLiveData.postValue(e.getMessage());
            }
        });
    }

    public LiveData<List<AssignmentInfo>> getAssignments() {
        return assignmentsLiveData;
    }

    public void addAssignment(AssignmentInfo assignmentInfo) {
        uiStateLiveData.setValue(UiState.LOADING);
        Log.d(TAG, "AssignmentsViewModel: Añadiendo TP " + assignmentInfo.getTitle());

        executorService.execute(() -> {
            try {
                DataRepository.getInstance().addAssignment(assignmentInfo);
                loadAssignments();
                Log.i(TAG, "AssignmentsViewModel: TP añadido con éxito.");
            } catch (Exception e) {
                Log.e(TAG, "AssignmentsViewModel: Error al añadir TP", e);
                uiStateLiveData.postValue(UiState.ERROR);
                errorMessageLiveData.postValue(e.getMessage());
            }
        });
    }

    public void updateAssignment(AssignmentInfo assignmentInfo) {
        uiStateLiveData.setValue(UiState.LOADING);
        Log.d(TAG, "AssignmentsViewModel: Actualizando TP ID " + assignmentInfo.getId());

        executorService.execute(() -> {
            try {
                DataRepository.getInstance().updateAssignment(assignmentInfo);
                loadAssignments();
                Log.i(TAG, "AssignmentsViewModel: TP actualizado con éxito.");
            } catch (Exception e) {
                Log.e(TAG, "AssignmentsViewModel: Error al actualizar TP", e);
                uiStateLiveData.postValue(UiState.ERROR);
                errorMessageLiveData.postValue(e.getMessage());
            }
        });
    }

    public void deleteAssignment(String id) {
        uiStateLiveData.setValue(UiState.LOADING);
        Log.d(TAG, "AssignmentsViewModel: Eliminando TP ID " + id);

        executorService.execute(() -> {
            try {
                DataRepository.getInstance().deleteAssignment(id);
                loadAssignments();
                Log.i(TAG, "AssignmentsViewModel: TP eliminado con éxito.");
            } catch (Exception e) {
                Log.e(TAG, "AssignmentsViewModel: Error al eliminar TP", e);
                uiStateLiveData.postValue(UiState.ERROR);
                errorMessageLiveData.postValue(e.getMessage());
            }
        });
    }
}
