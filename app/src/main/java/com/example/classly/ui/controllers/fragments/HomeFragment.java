package com.example.classly.ui.controllers.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.classly.R;
import com.example.classly.databinding.FragmentHomeBinding;
import com.example.classly.ui.viewmodels.HomeViewModel;
import com.example.classly.ui.viewmodels.UiState;

/**
 * Fragmento de la pantalla principal (Dashboard/Home).
 * Utiliza HomeViewModel para cargar de forma asincrónica (hilos secundarios)
 * la próxima clase y el examen más próximo, aplicando arquitectura MVVM estricta.
 */
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // Observar datos de forma reactiva
        viewModel.getNextClass().observe(getViewLifecycleOwner(), nextClass -> {
            if (binding != null && nextClass != null) {
                binding.tvNextClassSubject.setText(nextClass.getSubject());
                binding.tvNextClassTime.setText(nextClass.getTime());
                binding.tvNextClassRoom.setText(nextClass.getRoom());
            } else if (binding != null) {
                binding.tvNextClassSubject.setText("Sin clases programadas");
                binding.tvNextClassTime.setText("-");
                binding.tvNextClassRoom.setText("-");
            }
        });

        viewModel.getNearestExam().observe(getViewLifecycleOwner(), nearestExam -> {
            if (binding != null && nearestExam != null) {
                binding.tvNearestExam.setText(nearestExam.getSubject() + " (" + nearestExam.getType() + ")");
                binding.tvDaysRemaining.setText(getString(R.string.adapter_days_remaining, nearestExam.getDaysRemaining()));
            } else if (binding != null) {
                binding.tvNearestExam.setText("No hay exámenes próximos");
                binding.tvDaysRemaining.setText("");
            }
        });

        // Observar errores asíncronos
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMsg -> {
            if (errorMsg != null && getContext() != null) {
                Toast.makeText(getContext(), "Error: " + errorMsg, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.loadHomeData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewModel != null) {
            viewModel.loadHomeData();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
