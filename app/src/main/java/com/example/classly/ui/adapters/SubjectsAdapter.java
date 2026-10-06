package com.example.classly.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.classly.R;
import com.example.classly.data.models.SubjectInfo;
import com.example.classly.databinding.ItemSubjectBinding;
import java.util.List;

/**
 * Adaptador para RecyclerView que muestra la lista de asignaturas o materias cursadas.
 * Muestra información sobre la carga horaria, promedio de calificaciones y el estado de promoción
 * (promocionado o va a final), junto con botones para editar y eliminar.
 */
public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.ViewHolder> {

    private List<SubjectInfo> subjects;
    private OnSubjectActionListener listener;

    /**
     * Interfaz de callbacks para las acciones de edición y eliminación de materias.
     */
    public interface OnSubjectActionListener {
        void onEdit(SubjectInfo subjectInfo);
        void onDelete(SubjectInfo subjectInfo);
    }

    /**
     * Constructor del adaptador de materias.
     *
     * @param subjects Lista inicial de materias.
     * @param listener Listener para acciones.
     */
    public SubjectsAdapter(List<SubjectInfo> subjects, OnSubjectActionListener listener) {
        this.subjects = subjects;
        this.listener = listener;
    }

    /**
     * Actualiza los datos de las materias y refresca el RecyclerView.
     *
     * @param newSubjects Nueva lista de materias.
     */
    public void updateData(List<SubjectInfo> newSubjects) {
        this.subjects = newSubjects;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSubjectBinding binding = ItemSubjectBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        var context = holder.itemView.getContext();
        SubjectInfo item = subjects.get(position);

        // Vinculación de nombre, horas y nota promedio
        holder.binding.tvName.setText(item.getName());
        holder.binding.tvDetails.setText(context.getString(R.string.adapter_subject_details, item.getHours()));
        holder.binding.tvGrade.setText(String.valueOf(item.getAverageGrade()));

        // Lógica para evaluar si la materia es promocionable y mostrar el estado correspondiente
        if (item.isPromotional()) {
            holder.binding.tvPromoStatus.setVisibility(android.view.View.VISIBLE);
            if (item.getAverageGrade() >= item.getPromotionGrade()) {
                holder.binding.tvPromoStatus.setText(context.getString(R.string.status_promoting));
                holder.binding.tvPromoStatus.setBackgroundTintList(android.content.res.ColorStateList.valueOf(
                        context.getResources().getColor(android.R.color.holo_green_dark)));
            } else {
                holder.binding.tvPromoStatus.setText(context.getString(R.string.status_to_final));
                holder.binding.tvPromoStatus.setBackgroundTintList(android.content.res.ColorStateList.valueOf(
                        context.getResources().getColor(android.R.color.holo_orange_dark)));
            }
        } else {
            holder.binding.tvPromoStatus.setVisibility(android.view.View.GONE);
        }

        // Configuración de clics para editar o eliminar
        holder.binding.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(item);
        });
        holder.binding.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(item);
        });
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    /**
     * ViewHolder para los elementos de asignatura.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemSubjectBinding binding;

        public ViewHolder(ItemSubjectBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
