package com.example.classly;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.classly.data.DataRepository;
import com.example.classly.data.ClassInfo;
import com.example.classly.data.ExamInfo;
import com.example.classly.databinding.FragmentHomeBinding;
import java.util.List;

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
        
        DataRepository repo = DataRepository.getInstance();
        
        List<ClassInfo> classes = repo.getClassesByDay(0); // For dummy, just pick Monday
        ClassInfo nextClass = classes.isEmpty() ? null : classes.get(0);
        
        List<ExamInfo> exams = repo.getExams();
        ExamInfo nearestExam = exams.isEmpty() ? null : exams.get(0);

        if (nextClass != null) {
            binding.tvNextClassSubject.setText(nextClass.getSubject());
            binding.tvNextClassTime.setText(nextClass.getTime());
            binding.tvNextClassRoom.setText(nextClass.getRoom());
        }

        if (nearestExam != null) {
            binding.tvNearestExam.setText(nearestExam.getSubject() + " (" + nearestExam.getType() + ")");
            binding.tvDaysRemaining.setText("Faltan " + nearestExam.getDaysRemaining() + " días");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
