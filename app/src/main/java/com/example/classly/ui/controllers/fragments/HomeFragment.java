package com.example.classly.ui.controllers.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.classly.R;
import com.example.classly.data.repository.DataRepository;
import com.example.classly.data.models.ClassInfo;
import com.example.classly.data.models.ExamInfo;
import com.example.classly.databinding.FragmentHomeBinding;
import java.util.List;

/**
 * Fragmento de la pantalla principal (Dashboard/Home).
 * Muestra resúmenes rápidos del próximo turno de clase del día y del examen más próximo,
 * actualizándose automáticamente cada vez que se reanuda el fragmento.
 */
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshData();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    /**
     * Consulta al repositorio los datos actuales para la próxima clase y el examen más cercano,
     * y los refleja en las tarjetas de la interfaz de usuario.
     */
    private void refreshData() {
        if (binding == null) return;
        
        DataRepository repo = DataRepository.getInstance();
        
        // Obtener clases del día (ej. Lunes -> índice 0)
        List<ClassInfo> classes = repo.getClassesByDay(0);
        ClassInfo nextClass = classes.isEmpty() ? null : classes.get(0);
        
        // Obtener exámenes próximos
        List<ExamInfo> exams = repo.getExams();
        ExamInfo nearestExam = exams.isEmpty() ? null : exams.get(0);

        if (nextClass != null) {
            binding.tvNextClassSubject.setText(nextClass.getSubject());
            binding.tvNextClassTime.setText(nextClass.getTime());
            binding.tvNextClassRoom.setText(nextClass.getRoom());
        }

        if (nearestExam != null) {
            binding.tvNearestExam.setText(nearestExam.getSubject() + " (" + nearestExam.getType() + ")");
            binding.tvDaysRemaining.setText(getString(R.string.adapter_days_remaining, nearestExam.getDaysRemaining()));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
