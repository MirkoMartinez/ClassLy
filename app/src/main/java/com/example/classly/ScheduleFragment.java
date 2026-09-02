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
import android.widget.ArrayAdapter;
import com.example.classly.data.DataRepository;
import com.example.classly.data.ClassInfo;
import com.example.classly.databinding.FragmentScheduleBinding;
import com.example.classly.databinding.DialogAddClassBinding;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScheduleFragment extends Fragment implements ScheduleAdapter.OnClassActionListener {

    private FragmentScheduleBinding binding;
    private ScheduleAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentScheduleBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.rvSchedule.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ScheduleAdapter(new ArrayList<>(), this);
        binding.rvSchedule.setAdapter(adapter);

        List<String> days = Arrays.asList("L", "M", "Mi", "J", "V");
        for (String day : days) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(day));
        }

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                updateSchedule(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        binding.fabAddClass.setOnClickListener(v -> showAddClassDialog(null));

        updateSchedule(0);
    }

    private void updateSchedule(int dayIndex) {
        List<ClassInfo> classes = DataRepository.getInstance().getClassesByDay(dayIndex);
        adapter.updateData(classes);
    }

    private void showAddClassDialog(@Nullable ClassInfo classToEdit) {
        DialogAddClassBinding dialogBinding = DialogAddClassBinding.inflate(getLayoutInflater());
        
        String[] days = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, days);
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
                .setTitle(classToEdit == null ? "Añadir Clase" : "Editar Clase")
                .setView(dialogBinding.getRoot())
                .setPositiveButton(classToEdit == null ? "Añadir" : "Guardar", (dialog, which) -> {
                    String subject = dialogBinding.etSubject.getText().toString();
                    String time = dialogBinding.etTime.getText().toString();
                    String room = dialogBinding.etRoom.getText().toString();
                    String professor = dialogBinding.etProfessor.getText().toString();
                    int dayIndex = dialogBinding.spinnerDay.getSelectedItemPosition();

                    if (!subject.isEmpty()) {
                        if (classToEdit == null) {
                            DataRepository.getInstance().addClass(new ClassInfo(subject, time, room, professor, dayIndex));
                        } else {
                            classToEdit.setSubject(subject);
                            classToEdit.setTime(time);
                            classToEdit.setRoom(room);
                            classToEdit.setProfessor(professor);
                            classToEdit.setDayIndex(dayIndex);
                            DataRepository.getInstance().updateClass(classToEdit);
                        }
                        updateSchedule(binding.tabLayout.getSelectedTabPosition());
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void onEdit(ClassInfo classInfo) {
        showAddClassDialog(classInfo);
    }

    @Override
    public void onDelete(ClassInfo classInfo) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Eliminar Clase")
                .setMessage("¿Estás seguro de que quieres eliminar esta clase?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    DataRepository.getInstance().deleteClass(classInfo.getId());
                    updateSchedule(binding.tabLayout.getSelectedTabPosition());
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
