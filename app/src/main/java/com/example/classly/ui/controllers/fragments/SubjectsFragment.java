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
import com.example.classly.data.models.SubjectInfo;
import com.example.classly.ui.adapters.SubjectsAdapter;
import com.example.classly.databinding.FragmentSubjectsBinding;
import com.example.classly.databinding.DialogAddSubjectBinding;
import com.example.classly.ui.viewmodels.SubjectsViewModel;
import java.util.ArrayList;

/**
 * Fragmento que gestiona la lista de asignaturas y materias del estudiante.
 * Permite agregar nuevas materias especificando carga horaria, promedio y condiciones de promoción,
 * así como editarlas o eliminarlas.
 * Utiliza SubjectsViewModel para separar la lógica de negocio y gestionar el estado reactivo.
 */
public class SubjectsFragment extends Fragment implements SubjectsAdapter.OnSubjectActionListener {

    private FragmentSubjectsBinding binding;
    private SubjectsAdapter adapter;
    private SubjectsViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSubjectsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar ViewModel
        viewModel = new ViewModelProvider(this).get(SubjectsViewModel.class);

        // Configurar RecyclerView y adaptador de materias
        binding.rvSubjects.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SubjectsAdapter(new ArrayList<>(), this);
        binding.rvSubjects.setAdapter(adapter);

        // Observar LiveData de asignaturas de forma reactiva
        viewModel.getSubjects().observe(getViewLifecycleOwner(), subjects -> {
            adapter.updateData(subjects);
        });

        // Observar máquina de estados (UiState) para el ProgressBar
        viewModel.getUiState().observe(getViewLifecycleOwner(), state -> {
            if (binding != null) {
                binding.progressBar.setVisibility(state == com.example.classly.ui.viewmodels.UiState.LOADING ? View.VISIBLE : View.GONE);
            }
        });

        // Botón flotante para añadir materia
        binding.fabAddSubject.setOnClickListener(v -> showAddSubjectDialog(null));

        viewModel.loadSubjects();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewModel != null) {
            viewModel.loadSubjects();
        }
    }

    /**
     * Muestra el diálogo para agregar o editar una asignatura, gestionando la visibilidad
     * del campo de nota de promoción según el switch correspondiente.
     *
     * @param subjectToEdit Objeto SubjectInfo a editar, o null si es una nueva materia.
     */
    private void showAddSubjectDialog(@Nullable SubjectInfo subjectToEdit) {
        DialogAddSubjectBinding dialogBinding = DialogAddSubjectBinding.inflate(getLayoutInflater());

        if (subjectToEdit != null) {
            dialogBinding.etName.setText(subjectToEdit.getName());
            dialogBinding.etHours.setText(String.valueOf(subjectToEdit.getHours()));
            dialogBinding.etGrade.setText(String.valueOf(subjectToEdit.getAverageGrade()));
            dialogBinding.etPartialsCount.setText(String.valueOf(subjectToEdit.getPartialsCount()));
            dialogBinding.switchPromotional.setChecked(subjectToEdit.isPromotional());
            if (subjectToEdit.isPromotional()) {
                dialogBinding.layoutPromotionGrade.setVisibility(View.VISIBLE);
                dialogBinding.etPromotionGrade.setText(String.valueOf(subjectToEdit.getPromotionGrade()));
            }
        }

        // Mostrar u ocultar el campo de nota de promoción al cambiar el estado del switch
        dialogBinding.switchPromotional.setOnCheckedChangeListener((buttonView, isChecked) -> {
            dialogBinding.layoutPromotionGrade.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        new AlertDialog.Builder(requireContext())
                .setTitle(subjectToEdit == null ? getString(R.string.dialog_add_subject) : getString(R.string.dialog_edit_subject))
                .setView(dialogBinding.getRoot())
                .setPositiveButton(subjectToEdit == null ? getString(R.string.action_add) : getString(R.string.action_save), (dialog, which) -> {
                    String name = dialogBinding.etName.getText().toString();
                    String hoursStr = dialogBinding.etHours.getText().toString();
                    String gradeStr = dialogBinding.etGrade.getText().toString();
                    String partialsCountStr = dialogBinding.etPartialsCount.getText().toString();
                    boolean isPromotional = dialogBinding.switchPromotional.isChecked();
                    String promotionGradeStr = dialogBinding.etPromotionGrade.getText().toString();

                    int hours = hoursStr.isEmpty() ? 0 : Integer.parseInt(hoursStr);
                    double grade = gradeStr.isEmpty() ? 0.0 : Double.parseDouble(gradeStr);
                    int partialsCount = partialsCountStr.isEmpty() ? 2 : Integer.parseInt(partialsCountStr);
                    double promotionGrade = (isPromotional && !promotionGradeStr.isEmpty()) ? Double.parseDouble(promotionGradeStr) : 0.0;

                    if (!name.isEmpty()) {
                        if (subjectToEdit == null) {
                            viewModel.addSubject(new SubjectInfo(name, hours, grade, isPromotional, promotionGrade, partialsCount));
                        } else {
                            subjectToEdit.setName(name);
                            subjectToEdit.setHours(hours);
                            subjectToEdit.setAverageGrade(grade);
                            subjectToEdit.setPartialsCount(partialsCount);
                            subjectToEdit.setPromotional(isPromotional);
                            subjectToEdit.setPromotionGrade(promotionGrade);
                            viewModel.updateSubject(subjectToEdit);
                        }
                    }
                })
                .setNegativeButton(getString(R.string.action_cancel), null)
                .show();
    }

    @Override
    public void onEdit(SubjectInfo subjectInfo) {
        showAddSubjectDialog(subjectInfo);
    }

    @Override
    public void onDelete(SubjectInfo subjectInfo) {
        new AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.dialog_delete_subject_title))
                .setMessage(getString(R.string.dialog_delete_subject_msg))
                .setPositiveButton(getString(R.string.action_delete), (dialog, which) -> {
                    viewModel.deleteSubject(subjectInfo.getId());
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
