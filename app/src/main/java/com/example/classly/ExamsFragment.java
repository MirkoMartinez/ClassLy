package com.example.classly;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.app.AlertDialog;
import com.example.classly.data.DataRepository;
import com.example.classly.data.ExamInfo;
import com.example.classly.databinding.FragmentExamsBinding;
import com.example.classly.databinding.DialogAddExamBinding;
import java.util.ArrayList;

public class ExamsFragment extends Fragment implements ExamsAdapter.OnExamActionListener {

    private FragmentExamsBinding binding;
    private ExamsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentExamsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.rvExams.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ExamsAdapter(new ArrayList<>(DataRepository.getInstance().getExams()), this);
        binding.rvExams.setAdapter(adapter);

        binding.fabAddExam.setOnClickListener(v -> showAddExamDialog(null));
    }

    private void showAddExamDialog(@Nullable ExamInfo examToEdit) {
        DialogAddExamBinding dialogBinding = DialogAddExamBinding.inflate(getLayoutInflater());

        if (examToEdit != null) {
            dialogBinding.etSubject.setText(examToEdit.getSubject());
            dialogBinding.etDate.setText(examToEdit.getDate());
            dialogBinding.etType.setText(examToEdit.getType());
            dialogBinding.etTopic.setText(examToEdit.getTopic());
            dialogBinding.etDaysRemaining.setText(String.valueOf(examToEdit.getDaysRemaining()));
        }

        new AlertDialog.Builder(requireContext())
                .setTitle(examToEdit == null ? "Añadir Examen" : "Editar Examen")
                .setView(dialogBinding.getRoot())
                .setPositiveButton(examToEdit == null ? "Añadir" : "Guardar", (dialog, which) -> {
                    String subject = dialogBinding.etSubject.getText().toString();
                    String date = dialogBinding.etDate.getText().toString();
                    String type = dialogBinding.etType.getText().toString();
                    String topic = dialogBinding.etTopic.getText().toString();
                    String daysStr = dialogBinding.etDaysRemaining.getText().toString();
                    int daysRemaining = daysStr.isEmpty() ? 0 : Integer.parseInt(daysStr);

                    if (!subject.isEmpty()) {
                        if (examToEdit == null) {
                            DataRepository.getInstance().addExam(new ExamInfo(subject, date, type, topic, daysRemaining));
                        } else {
                            examToEdit.setSubject(subject);
                            examToEdit.setDate(date);
                            examToEdit.setType(type);
                            examToEdit.setTopic(topic);
                            examToEdit.setDaysRemaining(daysRemaining);
                            DataRepository.getInstance().updateExam(examToEdit);
                        }
                        adapter.updateData(DataRepository.getInstance().getExams());
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void onEdit(ExamInfo examInfo) {
        showAddExamDialog(examInfo);
    }

    @Override
    public void onDelete(ExamInfo examInfo) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Eliminar Examen")
                .setMessage("¿Estás seguro de que quieres eliminar este examen?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    DataRepository.getInstance().deleteExam(examInfo.getId());
                    adapter.updateData(DataRepository.getInstance().getExams());
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
