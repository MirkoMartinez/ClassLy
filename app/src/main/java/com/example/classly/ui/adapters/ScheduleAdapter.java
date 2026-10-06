package com.example.classly.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.classly.R;
import com.example.classly.data.models.ClassInfo;
import com.example.classly.databinding.ItemClassBinding;
import java.util.List;

/**
 * Adaptador para RecyclerView que muestra la lista de clases o turnos en el horario académico.
 * Permite visualizar la materia, horario, aula y profesor, así como gestionar acciones de edición y eliminación.
 */
public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    private List<ClassInfo> classes;
    private OnClassActionListener listener;

    /**
     * Interfaz de callbacks para manejar acciones sobre los elementos de clase.
     */
    public interface OnClassActionListener {
        void onEdit(ClassInfo classInfo);
        void onDelete(ClassInfo classInfo);
    }

    /**
     * Constructor del adaptador de horarios.
     *
     * @param classes  Lista inicial de clases.
     * @param listener Listener de eventos de acción.
     */
    public ScheduleAdapter(List<ClassInfo> classes, OnClassActionListener listener) {
        this.classes = classes;
        this.listener = listener;
    }

    /**
     * Actualiza la lista de clases y refresca la vista del RecyclerView.
     *
     * @param newClasses Nueva lista de clases.
     */
    public void updateData(List<ClassInfo> newClasses) {
        this.classes = newClasses;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemClassBinding binding = ItemClassBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        var context = holder.itemView.getContext();
        ClassInfo item = classes.get(position);

        // Vinculación de datos con las vistas del ítem
        holder.binding.tvSubject.setText(item.getSubject());
        holder.binding.tvTimeRoom.setText(item.getTime() + " - " + item.getRoom());
        holder.binding.tvProfessor.setText(context.getString(R.string.adapter_professor, item.getProfessor()));

        // Configuración de clics para editar y eliminar
        holder.binding.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(item);
        });
        holder.binding.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(item);
        });
    }

    @Override
    public int getItemCount() {
        return classes.size();
    }

    /**
     * ViewHolder para los elementos de clase.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemClassBinding binding;

        public ViewHolder(ItemClassBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
