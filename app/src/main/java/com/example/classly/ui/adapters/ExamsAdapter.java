package com.example.classly.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.classly.R;
import com.example.classly.data.models.ExamInfo;
import com.example.classly.databinding.ItemExamBinding;
import java.util.List;

/**
 * Adaptador para RecyclerView encargado de mostrar la lista de exámenes y evaluaciones.
 * Permite visualizar detalles como la materia, tipo de examen, fecha, tema y días restantes,
 * además de proveer callbacks para editar o eliminar cada elemento.
 */
public class ExamsAdapter extends RecyclerView.Adapter<ExamsAdapter.ViewHolder> {

    private List<ExamInfo> exams;
    private OnExamActionListener listener;

    /**
     * Interfaz de callbacks para manejar acciones de edición y eliminación sobre un examen.
     */
    public interface OnExamActionListener {
        void onEdit(ExamInfo examInfo);
        void onDelete(ExamInfo examInfo);
    }

    /**
     * Constructor del adaptador de exámenes.
     *
     * @param exams    Lista inicial de exámenes.
     * @param listener Listener para eventos de acción (editar/eliminar).
     */
    public ExamsAdapter(List<ExamInfo> exams, OnExamActionListener listener) {
        this.exams = exams;
        this.listener = listener;
    }

    /**
     * Actualiza los datos mostrados en el adaptador y notifica al RecyclerView.
     *
     * @param newExams Nueva lista de exámenes.
     */
    public void updateData(List<ExamInfo> newExams) {
        this.exams = newExams;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemExamBinding binding = ItemExamBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        var context = holder.itemView.getContext();
        ExamInfo item = exams.get(position);

        // Vinculación de datos con las vistas mediante ViewBinding
        holder.binding.tvSubject.setText(item.getSubject());
        holder.binding.tvBadge.setText(item.getType());
        holder.binding.tvDate.setText(context.getString(R.string.adapter_date, item.getDate()));
        holder.binding.tvTopic.setText(context.getString(R.string.adapter_topic, item.getTopic()));
        holder.binding.tvDaysRemaining.setText(context.getString(R.string.adapter_days_remaining, item.getDaysRemaining()));
        
        if (item.getGrade() != null) {
            holder.binding.tvGrade.setText(context.getString(R.string.adapter_exam_grade, String.valueOf(item.getGrade())));
        } else {
            holder.binding.tvGrade.setText(context.getString(R.string.adapter_exam_pending));
        }
        
        // Destacar en rojo si quedan menos de 7 días para el examen
        if (item.getDaysRemaining() < 7) {
            holder.binding.tvDaysRemaining.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
        } else {
            holder.binding.tvDaysRemaining.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray));
        }

        // Listeners para botones de edición y eliminación
        holder.binding.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(item);
        });
        holder.binding.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(item);
        });
    }

    @Override
    public int getItemCount() {
        return exams.size();
    }

    /**
     * ViewHolder que mantiene referencias a las vistas del elemento de examen.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemExamBinding binding;

        public ViewHolder(ItemExamBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
