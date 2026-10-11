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
import android.widget.ArrayAdapter;
import com.example.classly.R;
import com.example.classly.data.models.ClassInfo;
import com.example.classly.ui.adapters.ScheduleAdapter;
import com.example.classly.databinding.FragmentScheduleBinding;
import com.example.classly.databinding.DialogAddClassBinding;
import com.example.classly.ui.viewmodels.ScheduleViewModel;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Fragmento que muestra el horario académico organizado por días de la semana mediante un TabLayout.
 * Permite agregar clases manualmente, editar o eliminar clases existentes.
 * Utiliza ScheduleViewModel para separar la lógica de negocio y gestionar el estado reactivo.
 */
public class ScheduleFragment extends Fragment implements ScheduleAdapter.OnClassActionListener {

    private FragmentScheduleBinding binding;
    private ScheduleAdapter adapter;
    private ScheduleViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentScheduleBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar ViewModel
        viewModel = new ViewModelProvider(this).get(ScheduleViewModel.class);

        // Configurar RecyclerView para listar las clases del día seleccionado
        binding.rvSchedule.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ScheduleAdapter(new ArrayList<>(), this);
        binding.rvSchedule.setAdapter(adapter);

        // Observar los cambios en el LiveData del ViewModel de forma reactiva
        viewModel.getClasses().observe(getViewLifecycleOwner(), classes -> {
            adapter.updateData(classes);
        });

        // Configurar pestañas (TabLayout) para los días de la semana (Lunes a Viernes)
        List<String> days = Arrays.asList(
                getString(R.string.day_short_mon),
                getString(R.string.day_short_tue),
                getString(R.string.day_short_wed),
                getString(R.string.day_short_thu),
                getString(R.string.day_short_fri)
        );
        if (binding.tabLayout.getTabCount() == 0) {
            for (String day : days) {
                binding.tabLayout.addTab(binding.tabLayout.newTab().setText(day));
            }
        }

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewModel.loadClassesForDay(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Botón flotante para añadir clase manual
        binding.fabAddClass.setOnClickListener(v -> showAddClassDialog(null));

        viewModel.loadClassesForDay(binding.tabLayout.getSelectedTabPosition());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewModel != null) {
            viewModel.loadClassesForDay(binding.tabLayout.getSelectedTabPosition());
        }
    }

    /**
     * Muestra el diálogo para añadir o editar una clase manualmente.
     */
    private void showAddClassDialog(@Nullable ClassInfo classToEdit) {
        DialogAddClassBinding dialogBinding = DialogAddClassBinding.inflate(getLayoutInflater());
        
        String[] daysArray = {
                getString(R.string.day_monday),
                getString(R.string.day_tuesday),
                getString(R.string.day_wednesday),
                getString(R.string.day_thursday),
                getString(R.string.day_friday)
        };
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, daysArray);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dialogBinding.spinnerDay.setAdapter(spinnerAdapter);

        if (classToEdit != null) {
            dialogBinding.etSubject.setText(classToEdit.getSubject());
            dialogBinding.etTime.setText(classToEdit.getTime());
            dialogBinding.etRoom.setText(classToEdit.getRoom());
            dialogBinding.etProfessor.setText(classToEdit.getProfessor());
            dialogBinding.spinnerDay.setSelection(classToEdit.getDayIndex());
        } else {
            dialogBinding.spinnerDay.setSelection(binding.tabLayout.getSelectedTabPosition());
        }

        new AlertDialog.Builder(requireContext())
                .setTitle(classToEdit == null ? getString(R.string.dialog_add_class) : getString(R.string.dialog_edit_class))
                .setView(dialogBinding.getRoot())
                .setPositiveButton(classToEdit == null ? getString(R.string.action_add) : getString(R.string.action_save), (dialog, which) -> {
                    String subject = dialogBinding.etSubject.getText().toString();
                    String time = dialogBinding.etTime.getText().toString();
                    String room = dialogBinding.etRoom.getText().toString();
                    String professor = dialogBinding.etProfessor.getText().toString();
                    int dayIdx = dialogBinding.spinnerDay.getSelectedItemPosition();

                    if (!subject.isEmpty()) {
                        if (classToEdit == null) {
                            viewModel.addClass(new ClassInfo(subject, time, room, professor, dayIdx));
                        } else {
                            classToEdit.setSubject(subject);
                            classToEdit.setTime(time);
                            classToEdit.setRoom(room);
                            classToEdit.setProfessor(professor);
                            classToEdit.setDayIndex(dayIdx);
                            viewModel.updateClass(classToEdit);
                        }
                    }
                })
                .setNegativeButton(getString(R.string.action_cancel), null)
                .show();
    }

    @Override
    public void onEdit(ClassInfo classInfo) {
        showAddClassDialog(classInfo);
    }

    @Override
    public void onDelete(ClassInfo classInfo) {
        new AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.dialog_delete_class_title))
                .setMessage(getString(R.string.dialog_delete_class_msg))
                .setPositiveButton(getString(R.string.action_delete), (dialog, which) -> {
                    viewModel.deleteClass(classInfo.getId());
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
