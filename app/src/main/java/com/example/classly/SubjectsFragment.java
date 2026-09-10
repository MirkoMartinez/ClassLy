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
import com.example.classly.data.SubjectInfo;
import com.example.classly.databinding.FragmentSubjectsBinding;
import com.example.classly.databinding.DialogAddSubjectBinding;
import java.util.ArrayList;

public class SubjectsFragment extends Fragment implements SubjectsAdapter.OnSubjectActionListener {

    private FragmentSubjectsBinding binding;
    private SubjectsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSubjectsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.rvSubjects.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SubjectsAdapter(new ArrayList<>(DataRepository.getInstance().getSubjects()), this);
        binding.rvSubjects.setAdapter(adapter);

        binding.fabAddSubject.setOnClickListener(v -> showAddSubjectDialog(null));
    }

    // Este es el método más complejo: muestra el formulario para añadir o editar una materia
    private void showAddSubjectDialog(@Nullable SubjectInfo subjectToEdit) {
        DialogAddSubjectBinding dialogBinding = DialogAddSubjectBinding.inflate(getLayoutInflater());

        // Si estamos editando, rellenamos los campos con la info que ya teníamos
        if (subjectToEdit != null) {
            dialogBinding.etName.setText(subjectToEdit.getName());
            dialogBinding.etCredits.setText(String.valueOf(subjectToEdit.getCredits()));
            dialogBinding.etHours.setText(String.valueOf(subjectToEdit.getHours()));
            dialogBinding.etGrade.setText(String.valueOf(subjectToEdit.getAverageGrade()));
            dialogBinding.switchPromotional.setChecked(subjectToEdit.isPromotional());
            if (subjectToEdit.isPromotional()) {
                dialogBinding.layoutPromotionGrade.setVisibility(View.VISIBLE);
                dialogBinding.etPromotionGrade.setText(String.valueOf(subjectToEdit.getPromotionGrade()));
            }
        }

        // Hacemos que el campo de "nota necesaria" aparezca o desaparezca según el switch
        dialogBinding.switchPromotional.setOnCheckedChangeListener((buttonView, isChecked) -> {
            dialogBinding.layoutPromotionGrade.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        // Construimos el cuadro de diálogo de Material Design
        new AlertDialog.Builder(requireContext())
                .setTitle(subjectToEdit == null ? "Añadir Asignatura" : "Editar Asignatura")
                .setView(dialogBinding.getRoot())
                .setPositiveButton(subjectToEdit == null ? "Añadir" : "Guardar", (dialog, which) -> {
                    // Recolectamos todo lo que el usuario escribió
                    String name = dialogBinding.etName.getText().toString();
                    String creditsStr = dialogBinding.etCredits.getText().toString();
                    String hoursStr = dialogBinding.etHours.getText().toString();
                    String gradeStr = dialogBinding.etGrade.getText().toString();
                    boolean isPromotional = dialogBinding.switchPromotional.isChecked();
                    String promotionGradeStr = dialogBinding.etPromotionGrade.getText().toString();

                    int credits = creditsStr.isEmpty() ? 0 : Integer.parseInt(creditsStr);
                    int hours = hoursStr.isEmpty() ? 0 : Integer.parseInt(hoursStr);
                    double grade = gradeStr.isEmpty() ? 0.0 : Double.parseDouble(gradeStr);
                    double promotionGrade = (isPromotional && !promotionGradeStr.isEmpty()) ? Double.parseDouble(promotionGradeStr) : 0.0;

                    if (!name.isEmpty()) {
                        if (subjectToEdit == null) {
                            // Si es nueva, la mandamos al repositorio
                            DataRepository.getInstance().addSubject(new SubjectInfo(name, credits, hours, grade, isPromotional, promotionGrade));
                        } else {
                            // Si ya existía, actualizamos sus datos actuales
                            subjectToEdit.setName(name);
                            subjectToEdit.setCredits(credits);
                            subjectToEdit.setHours(hours);
                            subjectToEdit.setAverageGrade(grade);
                            subjectToEdit.setPromotional(isPromotional);
                            subjectToEdit.setPromotionGrade(promotionGrade);
                            DataRepository.getInstance().updateSubject(subjectToEdit);
                        }
                        // Refrescamos la lista para que se vean los cambios al instante
                        adapter.updateData(DataRepository.getInstance().getSubjects());
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void onEdit(SubjectInfo subjectInfo) {
        showAddSubjectDialog(subjectInfo);
    }

    @Override
    public void onDelete(SubjectInfo subjectInfo) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Eliminar Asignatura")
                .setMessage("¿Estás seguro de que quieres eliminar esta asignatura?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    DataRepository.getInstance().deleteSubject(subjectInfo.getId());
                    adapter.updateData(DataRepository.getInstance().getSubjects());
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
