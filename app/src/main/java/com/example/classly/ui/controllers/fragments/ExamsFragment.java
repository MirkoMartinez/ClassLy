package com.example.classly.ui.controllers.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.app.AlertDialog;
import com.example.classly.R;
import com.example.classly.data.models.ExamInfo;
import com.example.classly.ui.adapters.ExamsAdapter;
import com.example.classly.databinding.FragmentExamsBinding;
import com.example.classly.databinding.DialogAddExamBinding;
import com.example.classly.ui.viewmodels.ExamsViewModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Fragmento encargado de listar todos los exámenes y evaluaciones programadas,
 * permitiendo al usuario agregar nuevos exámenes mediante un diálogo con selector de fecha,
 * así como editarlos o eliminarlos.
 * Utiliza ExamsViewModel para separar la lógica de negocio y gestionar el estado reactivo.
 */
public class ExamsFragment extends Fragment implements ExamsAdapter.OnExamActionListener {

    private FragmentExamsBinding binding;
    private ExamsAdapter adapter;
    private ExamsViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentExamsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar ViewModel
        viewModel = new ViewModelProvider(this).get(ExamsViewModel.class);

        // Configurar RecyclerView y adaptador de exámenes
        binding.rvExams.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ExamsAdapter(new ArrayList<>(), this);
        binding.rvExams.setAdapter(adapter);

        // Observar LiveData de exámenes de forma reactiva
        viewModel.getExams().observe(getViewLifecycleOwner(), exams -> {
            adapter.updateData(exams);
        });

        // Observar máquina de estados (UiState) para el ProgressBar
        viewModel.getUiState().observe(getViewLifecycleOwner(), state -> {
            if (binding != null) {
                binding.progressBar.setVisibility(state == com.example.classly.ui.viewmodels.UiState.LOADING ? View.VISIBLE : View.GONE);
            }
        });

        // Botón flotante para añadir un nuevo examen
        binding.fabAddExam.setOnClickListener(v -> showAddExamDialog(null));

        viewModel.loadExams();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewModel != null) {
            viewModel.loadExams();
        }
    }

    /**
     * Muestra el diálogo para añadir o editar un examen.
     *
     * @param examToEdit Objeto ExamInfo a editar, o null si se trata de un nuevo registro.
     */
    private void showAddExamDialog(@Nullable ExamInfo examToEdit) {
        DialogAddExamBinding dialogBinding = DialogAddExamBinding.inflate(getLayoutInflater());

        if (examToEdit != null) {
            dialogBinding.etSubject.setText(examToEdit.getSubject());
            dialogBinding.etDate.setText(examToEdit.getDate());
            dialogBinding.etType.setText(examToEdit.getType());
            dialogBinding.etTopic.setText(examToEdit.getTopic());
            if (examToEdit.getGrade() != null) {
                dialogBinding.etGrade.setText(String.valueOf(examToEdit.getGrade()));
            }
        }

        // Selector de fecha (DatePickerDialog) al hacer clic en el campo de fecha
        dialogBinding.etDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            new android.app.DatePickerDialog(requireContext(), (view1, y, m, d) -> {
                String formattedDate = String.format(Locale.US, "%04d-%02d-%02d", y, m + 1, d);
                dialogBinding.etDate.setText(formattedDate);
            }, year, month, day).show();
        });

        new AlertDialog.Builder(requireContext())
                .setTitle(examToEdit == null ? getString(R.string.dialog_add_exam) : getString(R.string.dialog_edit_exam))
                .setView(dialogBinding.getRoot())
                .setPositiveButton(examToEdit == null ? getString(R.string.action_add) : getString(R.string.action_save), (dialog, which) -> {
                    String subject = dialogBinding.etSubject.getText().toString();
                    String date = dialogBinding.etDate.getText().toString();
                    String type = dialogBinding.etType.getText().toString();
                    String topic = dialogBinding.etTopic.getText().toString();
                    String gradeStr = dialogBinding.etGrade.getText().toString();
                    Double grade = gradeStr.isEmpty() ? null : Double.parseDouble(gradeStr);

                    if (!subject.isEmpty()) {
                        if (examToEdit == null) {
                            ExamInfo newExam = new ExamInfo(subject, date, type, topic);
                            newExam.setGrade(grade);
                            viewModel.addExam(newExam);
                        } else {
                            examToEdit.setSubject(subject);
                            examToEdit.setDate(date);
                            examToEdit.setType(type);
                            examToEdit.setTopic(topic);
                            examToEdit.setGrade(grade);
                            viewModel.updateExam(examToEdit);
                        }
                    }
                })
                .setNegativeButton(getString(R.string.action_cancel), null)
                .show();
    }

    @Override
    public void onEdit(ExamInfo examInfo) {
        showAddExamDialog(examInfo);
    }

    @Override
    public void onDelete(ExamInfo examInfo) {
        new AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.dialog_delete_exam_title))
                .setMessage(getString(R.string.dialog_delete_exam_msg))
                .setPositiveButton(getString(R.string.action_delete), (dialog, which) -> {
                    viewModel.deleteExam(examInfo.getId());
                })
                .setNegativeButton(getString(R.string.action_cancel), null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
