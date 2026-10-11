package com.example.classly.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ViewModel base abstracto que provee un pool de hilos compartido (ExecutorService)
 * para la ejecución asincrónica de operaciones pesadas en hilos secundarios (Background Threads),
 * protegiendo el hilo principal (Main Thread), junto con la gestión de estados de UI (UiState).
 */
public abstract class BaseViewModel extends ViewModel {
    protected final ExecutorService executorService = Executors.newFixedThreadPool(4);
    protected final MutableLiveData<UiState> uiStateLiveData = new MutableLiveData<>(UiState.SUCCESS);
    protected final MutableLiveData<String> errorMessageLiveData = new MutableLiveData<>();

    public LiveData<UiState> getUiState() {
        return uiStateLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessageLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        // Liberar el pool de hilos cuando el ViewModel sea destruido para evitar fugas de memoria
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
