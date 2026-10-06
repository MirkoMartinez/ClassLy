package com.example.classly.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.classly.R;
import com.example.classly.data.models.AssignmentInfo;
import com.example.classly.databinding.ItemAssignmentBinding;
import java.util.List;

/**
 * Adaptador para RecyclerView encargado de mostrar la lista de trabajos prácticos (TPs).
 * Muestra la materia, título, descripción, fecha de vencimiento, estado de entrega y nota,
 * además de proveer callbacks para editar o eliminar cada elemento.
 */
public class AssignmentsAdapter extends RecyclerView.Adapter<AssignmentsAdapter.ViewHolder> {

    private List<AssignmentInfo> assignments;
    private OnAssignmentActionListener listener;

    public interface OnAssignmentActionListener {
        void onEdit(AssignmentInfo assignmentInfo);
        void onDelete(AssignmentInfo assignmentInfo);
    }

    public AssignmentsAdapter(List<AssignmentInfo> assignments, OnAssignmentActionListener listener) {
        this.assignments = assignments;
        this.listener = listener;
    }

    public void updateData(List<AssignmentInfo> newAssignments) {
        this.assignments = newAssignments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAssignmentBinding binding = ItemAssignmentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        var context = holder.itemView.getContext();
        AssignmentInfo item = assignments.get(position);

        holder.binding.tvSubject.setText(item.getSubject());
        holder.binding.tvTitle.setText(item.getTitle());
        holder.binding.tvDescription.setText(item.getDescription());
        holder.binding.tvDate.setText(context.getString(R.string.adapter_date, item.getDate()));
        holder.binding.tvDaysRemaining.setText(context.getString(R.string.adapter_days_remaining, item.getDaysRemaining()));
        
        holder.binding.cbSubmitted.setChecked(item.isSubmitted());

        if (item.getGrade() != null) {
            holder.binding.tvGrade.setVisibility(android.view.View.VISIBLE);
            holder.binding.tvGrade.setText(context.getString(R.string.adapter_exam_grade, String.valueOf(item.getGrade())));
        } else {
            holder.binding.tvGrade.setVisibility(android.view.View.VISIBLE);
            holder.binding.tvGrade.setText(context.getString(R.string.adapter_exam_pending));
        }

        if (item.getDaysRemaining() < 3 && !item.isSubmitted()) {
            holder.binding.tvDaysRemaining.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
        } else {
            holder.binding.tvDaysRemaining.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray));
        }

        holder.binding.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(item);
        });
        holder.binding.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(item);
        });
    }

    @Override
    public int getItemCount() {
        return assignments.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemAssignmentBinding binding;

        public ViewHolder(ItemAssignmentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
