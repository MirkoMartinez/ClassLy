package com.example.classly.ui.controllers.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.app.AlertDialog;
import android.widget.ArrayAdapter;
import com.example.classly.R;
import com.example.classly.data.repository.DataRepository;
import com.example.classly.data.models.ClassInfo;
import com.example.classly.ui.adapters.ScheduleAdapter;
import com.example.classly.databinding.FragmentScheduleBinding;
import com.example.classly.databinding.DialogAddClassBinding;
import com.example.classly.utils.PdfScheduleParser;
import com.example.classly.utils.ScheduleParserHelper;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Fragmento que muestra el horario académico organizado por días de la semana mediante un TabLayout.
 * Permite agregar clases manualmente, editar o eliminar clases existentes,
 * e importar horarios automáticamente desde un archivo PDF mediante OCR (ML Kit) y parsing heurístico.
 */
public class ScheduleFragment extends Fragment implements ScheduleAdapter.OnClassActionListener {

    private FragmentScheduleBinding binding;
    private ScheduleAdapter adapter;
    private ActivityResultLauncher<String[]> pdfPickerLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Lanzador de selector de archivos PDF
        pdfPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.OpenDocument(),
                uri -> {
                    if (uri != null) {
                        showLoadingAndProcessPdf(uri);
                    }
                }
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentScheduleBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Configurar RecyclerView para listar las clases del día seleccionado
        binding.rvSchedule.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ScheduleAdapter(new ArrayList<>(), this);
        binding.rvSchedule.setAdapter(adapter);

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
                updateSchedule(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Botones flotantes para añadir clase manual e importar PDF
        binding.fabAddClass.setOnClickListener(v -> showAddClassDialog(null));
        binding.fabImportPdf.setOnClickListener(v -> pdfPickerLauncher.launch(new String[]{"application/pdf"}));

        updateSchedule(binding.tabLayout.getSelectedTabPosition());
    }

    @Override
    public void onResume() {
        super.onResume();
        updateSchedule(binding.tabLayout.getSelectedTabPosition());
    }

    /**
     * Muestra un diálogo de progreso e inicia el procesamiento OCR del PDF seleccionado.
     *
     * @param uri URI del archivo PDF seleccionado por el usuario.
     */
    private void showLoadingAndProcessPdf(android.net.Uri uri) {
        AlertDialog progressDialog = new AlertDialog.Builder(requireContext())
                .setTitle("Procesando horario")
                .setMessage("Extrayendo texto del PDF escaneado con OCR...")
                .setCancelable(false)
                .create();
        progressDialog.show();

        PdfScheduleParser.extractTextFromPdfUri(requireContext(), uri, new PdfScheduleParser.OnTextExtractedListener() {
            @Override
            public void onSuccess(String extractedText) {
                requireActivity().runOnUiThread(() -> {
                    progressDialog.dismiss();
                    showPreviewDialog(extractedText);
                });
            }

            @Override
            public void onError(Exception e) {
                requireActivity().runOnUiThread(() -> {
                    progressDialog.dismiss();
                    new AlertDialog.Builder(requireContext())
                            .setTitle("Error")
                            .setMessage("No se pudo procesar el PDF: " + e.getMessage())
                            .setPositiveButton("Aceptar", null)
                            .show();
                });
            }
        });
    }

    /**
     * Muestra una ventana de previsualización con las clases detectadas mediante parsing heurístico del texto extraído.
     *
     * @param extractedText Texto completo extraído por OCR de las páginas del PDF.
     */
    private void showPreviewDialog(String extractedText) {
        List<ClassInfo> parsedClasses = ScheduleParserHelper.parseScheduleText(extractedText);

        StringBuilder previewText = new StringBuilder();
        previewText.append("Se detectaron ").append(parsedClasses.size()).append(" clases:\n\n");
        for (ClassInfo c : parsedClasses) {
            String dayName = getDayName(c.getDayIndex());
            previewText.append("• ").append(c.getSubject())
                    .append(" (").append(dayName).append(" ")
                    .append(c.getTime()).append(")\n");
        }
        if (parsedClasses.isEmpty()) {
            previewText.append("No se pudieron detectar clases automáticamente. Intenta con otro PDF.");
        }

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_import_preview, null);
        android.widget.TextView tvPreview = dialogView.findViewById(R.id.tvPreviewContent);
        tvPreview.setText(previewText.toString());

        new AlertDialog.Builder(requireContext())
                .setTitle("Previsualización de Horario")
                .setView(dialogView)
                .setPositiveButton("Importar", (dialog, which) -> {
                    for (ClassInfo c : parsedClasses) {
                        DataRepository.getInstance().addClass(c);
                    }
                    updateSchedule(binding.tabLayout.getSelectedTabPosition());
                    android.widget.Toast.makeText(requireContext(), parsedClasses.size() + " clases importadas con éxito", android.widget.Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    /**
     * Retorna el nombre legible del día según su índice.
     */
    private String getDayName(int dayIndex) {
        switch (dayIndex) {
            case 0: return getString(R.string.day_monday);
            case 1: return getString(R.string.day_tuesday);
            case 2: return getString(R.string.day_wednesday);
            case 3: return getString(R.string.day_thursday);
            case 4: return getString(R.string.day_friday);
            default: return "Día " + dayIndex;
        }
    }

    /**
     * Actualiza el adaptador de clases según el día seleccionado en las pestañas.
     */
    private void updateSchedule(int dayIndex) {
        if (dayIndex < 0) dayIndex = 0;
        List<ClassInfo> classes = DataRepository.getInstance().getClassesByDay(dayIndex);
        adapter.updateData(classes);
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
                    DataRepository.getInstance().deleteClass(classInfo.getId());
                    updateSchedule(binding.tabLayout.getSelectedTabPosition());
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
