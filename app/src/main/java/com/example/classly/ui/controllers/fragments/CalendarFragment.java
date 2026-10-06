package com.example.classly.ui.controllers.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.app.AlertDialog;
import com.example.classly.R;
import com.example.classly.data.models.AssignmentInfo;
import com.example.classly.data.models.ExamInfo;
import com.example.classly.data.repository.DataRepository;
import com.example.classly.databinding.FragmentCalendarBinding;
import com.example.classly.databinding.DialogAddAssignmentBinding;
import com.example.classly.databinding.DialogAddExamBinding;
import com.example.classly.ui.adapters.AssignmentsAdapter;
import com.example.classly.ui.adapters.ExamsAdapter;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Fragmento que muestra un calendario interactivo (MaterialCalendarView)
 * para visualizar exámenes y trabajos prácticos programados por fecha,
 * permitiendo filtrarlos, agregarlos, editarlos o eliminarlos.
 */
public class CalendarFragment extends Fragment implements ExamsAdapter.OnExamActionListener, AssignmentsAdapter.OnAssignmentActionListener {

    private FragmentCalendarBinding binding;
    private ExamsAdapter examsAdapter;
    private AssignmentsAdapter assignmentsAdapter;
    private String selectedDateStr;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Configurar RecyclerViews para exámenes y TPs
        binding.rvCalendarExams.setLayoutManager(new LinearLayoutManager(getContext()));
        examsAdapter = new ExamsAdapter(new ArrayList<>(), this);
        binding.rvCalendarExams.setAdapter(examsAdapter);

        binding.rvCalendarAssignments.setLayoutManager(new LinearLayoutManager(getContext()));
        assignmentsAdapter = new AssignmentsAdapter(new ArrayList<>(), this);
        binding.rvCalendarAssignments.setAdapter(assignmentsAdapter);

        // Seleccionar la fecha actual por defecto en el calendario
        binding.calendarView.setSelectedDate(CalendarDay.today());
        CalendarDay today = CalendarDay.today();
        updateSelectedDate(today.getYear(), today.getMonth() - 1, today.getDay());

        // Listener cuando el usuario cambia de fecha en el calendario
        binding.calendarView.setOnDateChangedListener((widget, date, selected) -> {
            updateSelectedDate(date.getYear(), date.getMonth() - 1, date.getDay());
        });

        // Botón flotante para agregar un nuevo trabajo práctico
        binding.fabAddAssignment.setOnClickListener(v -> showAddAssignmentDialog(null));

        // Resaltar días con exámenes y TPs en el calendario
        highlightEventDays();
    }

    private void updateSelectedDate(int year, int month, int dayOfMonth) {
        selectedDateStr = String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, dayOfMonth);
        refreshEvents();
    }

    private void highlightEventDays() {
        if (binding == null) return;
        binding.calendarView.removeDecorators();

        List<ExamInfo> allExams = DataRepository.getInstance().getExams();
        List<AssignmentInfo> allAssignments = DataRepository.getInstance().getAssignments();

        List<CalendarDay> examDays = new ArrayList<>();
        for (ExamInfo exam : allExams) {
            String[] parts = exam.getDate().split("-");
            if (parts.length == 3) {
                try {
                    examDays.add(CalendarDay.from(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2])));
                } catch (Exception ignored) {}
            }
        }

        List<CalendarDay> assignmentDays = new ArrayList<>();
        for (AssignmentInfo assignment : allAssignments) {
            String[] parts = assignment.getDate().split("-");
            if (parts.length == 3) {
                try {
                    assignmentDays.add(CalendarDay.from(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2])));
                } catch (Exception ignored) {}
            }
        }

        // Rojo para exámenes, Azul para TPs
        binding.calendarView.addDecorator(new EventDayDecorator(android.graphics.Color.RED, examDays));
        binding.calendarView.addDecorator(new EventDayDecorator(android.graphics.Color.BLUE, assignmentDays));
    }

    private void refreshEvents() {
        if (binding == null) return;

        List<ExamInfo> filteredExams = DataRepository.getInstance().getExams().stream()
                .filter(exam -> selectedDateStr.equals(exam.getDate()))
                .collect(Collectors.toList());

        List<AssignmentInfo> filteredAssignments = DataRepository.getInstance().getAssignments().stream()
                .filter(a -> selectedDateStr.equals(a.getDate()))
                .collect(Collectors.toList());

        examsAdapter.updateData(filteredExams);
        assignmentsAdapter.updateData(filteredAssignments);

        boolean hasExams = !filteredExams.isEmpty();
        boolean hasAssignments = !filteredAssignments.isEmpty();

        binding.tvExamsHeader.setVisibility(hasExams ? View.VISIBLE : View.GONE);
        binding.rvCalendarExams.setVisibility(hasExams ? View.VISIBLE : View.GONE);

        binding.tvAssignmentsHeader.setVisibility(hasAssignments ? View.VISIBLE : View.GONE);
        binding.rvCalendarAssignments.setVisibility(hasAssignments ? View.VISIBLE : View.GONE);

        if (!hasExams && !hasAssignments) {
            binding.tvNoEvents.setVisibility(View.VISIBLE);
        } else {
            binding.tvNoEvents.setVisibility(View.GONE);
        }
    }

    private void showAddAssignmentDialog(@Nullable AssignmentInfo assignmentToEdit) {
        DialogAddAssignmentBinding dialogBinding = DialogAddAssignmentBinding.inflate(getLayoutInflater());

        if (assignmentToEdit != null) {
            dialogBinding.etSubject.setText(assignmentToEdit.getSubject());
            dialogBinding.etTitle.setText(assignmentToEdit.getTitle());
            dialogBinding.etDate.setText(assignmentToEdit.getDate());
            dialogBinding.etDescription.setText(assignmentToEdit.getDescription());
            dialogBinding.cbSubmitted.setChecked(assignmentToEdit.isSubmitted());
            if (assignmentToEdit.getGrade() != null) {
                dialogBinding.etGrade.setText(String.valueOf(assignmentToEdit.getGrade()));
            }
        } else {
            dialogBinding.etDate.setText(selectedDateStr);
        }

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
                .setTitle(assignmentToEdit == null ? getString(R.string.dialog_add_assignment) : getString(R.string.dialog_edit_assignment))
                .setView(dialogBinding.getRoot())
                .setPositiveButton(assignmentToEdit == null ? getString(R.string.action_add) : getString(R.string.action_save), (dialog, which) -> {
                    String subject = dialogBinding.etSubject.getText().toString();
                    String title = dialogBinding.etTitle.getText().toString();
                    String date = dialogBinding.etDate.getText().toString();
                    String description = dialogBinding.etDescription.getText().toString();
                    boolean isSubmitted = dialogBinding.cbSubmitted.isChecked();
                    String gradeStr = dialogBinding.etGrade.getText().toString();
                    Double grade = gradeStr.isEmpty() ? null : Double.parseDouble(gradeStr);

                    if (!subject.isEmpty() && !title.isEmpty()) {
                        if (assignmentToEdit == null) {
                            AssignmentInfo newAssignment = new AssignmentInfo(subject, date, title, description);
                            newAssignment.setSubmitted(isSubmitted);
                            newAssignment.setGrade(grade);
                            DataRepository.getInstance().addAssignment(newAssignment);
                        } else {
                            assignmentToEdit.setSubject(subject);
                            assignmentToEdit.setTitle(title);
                            assignmentToEdit.setDate(date);
                            assignmentToEdit.setDescription(description);
                            assignmentToEdit.setSubmitted(isSubmitted);
                            assignmentToEdit.setGrade(grade);
                            DataRepository.getInstance().updateAssignment(assignmentToEdit);
                        }
                        highlightEventDays();
                        refreshEvents();
                    }
                })
                .setNegativeButton(getString(R.string.action_cancel), null)
                .show();
    }

    @Override
    public void onEdit(ExamInfo examInfo) {
        DialogAddExamBinding dialogBinding = DialogAddExamBinding.inflate(getLayoutInflater());
        dialogBinding.etSubject.setText(examInfo.getSubject());
        dialogBinding.etDate.setText(examInfo.getDate());
        dialogBinding.etType.setText(examInfo.getType());
        dialogBinding.etTopic.setText(examInfo.getTopic());
        if (examInfo.getGrade() != null) {
            dialogBinding.etGrade.setText(String.valueOf(examInfo.getGrade()));
        }

        dialogBinding.etDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new android.app.DatePickerDialog(requireContext(), (view1, y, m, d) -> {
                String formattedDate = String.format(Locale.US, "%04d-%02d-%02d", y, m + 1, d);
                dialogBinding.etDate.setText(formattedDate);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        new AlertDialog.Builder(requireContext())
                .setTitle("Editar Examen")
                .setView(dialogBinding.getRoot())
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String subject = dialogBinding.etSubject.getText().toString();
                    String date = dialogBinding.etDate.getText().toString();
                    String type = dialogBinding.etType.getText().toString();
                    String topic = dialogBinding.etTopic.getText().toString();
                    String gradeStr = dialogBinding.etGrade.getText().toString();
                    Double grade = gradeStr.isEmpty() ? null : Double.parseDouble(gradeStr);

                    if (!subject.isEmpty()) {
                        examInfo.setSubject(subject);
                        examInfo.setDate(date);
                        examInfo.setType(type);
                        examInfo.setTopic(topic);
                        examInfo.setGrade(grade);
                        DataRepository.getInstance().updateExam(examInfo);
                        highlightEventDays();
                        refreshEvents();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void onDelete(ExamInfo examInfo) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Eliminar Examen")
                .setMessage("¿Estás seguro de que quieres eliminar este examen?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    DataRepository.getInstance().deleteExam(examInfo.getId());
                    highlightEventDays();
                    refreshEvents();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void onEdit(AssignmentInfo assignmentInfo) {
        showAddAssignmentDialog(assignmentInfo);
    }

    @Override
    public void onDelete(AssignmentInfo assignmentInfo) {
        new AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.dialog_delete_assignment_title))
                .setMessage(getString(R.string.dialog_delete_assignment_msg))
                .setPositiveButton(getString(R.string.action_delete), (dialog, which) -> {
                    DataRepository.getInstance().deleteAssignment(assignmentInfo.getId());
                    highlightEventDays();
                    refreshEvents();
                })
                .setNegativeButton(getString(R.string.action_cancel), null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private static class EventDayDecorator implements DayViewDecorator {
        private final int color;
        private final HashSet<CalendarDay> dates;

        public EventDayDecorator(int color, Collection<CalendarDay> dates) {
            this.color = color;
            this.dates = new HashSet<>(dates);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(8, color));
        }
    }
}
