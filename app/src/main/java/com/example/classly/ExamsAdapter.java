package com.example.classly;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.classly.data.ExamInfo;
import com.example.classly.databinding.ItemExamBinding;
import java.util.List;

public class ExamsAdapter extends RecyclerView.Adapter<ExamsAdapter.ViewHolder> {

    private List<ExamInfo> exams;
    private OnExamActionListener listener;

    public interface OnExamActionListener {
        void onEdit(ExamInfo examInfo);
        void onDelete(ExamInfo examInfo);
    }

    public ExamsAdapter(List<ExamInfo> exams, OnExamActionListener listener) {
        this.exams = exams;
        this.listener = listener;
    }

    public void updateData(List<ExamInfo> newExams) {
        this.exams = newExams;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemExamBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExamInfo item = exams.get(position);
        holder.binding.tvSubject.setText(item.getSubject());
        holder.binding.tvBadge.setText(item.getType());
        holder.binding.tvDate.setText("Fecha: " + item.getDate());
        holder.binding.tvTopic.setText("Tema: " + item.getTopic());
        holder.binding.tvDaysRemaining.setText("Faltan " + item.getDaysRemaining() + " días");
        
        if (item.getDaysRemaining() < 7) {
            holder.binding.tvDaysRemaining.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.holo_red_dark));
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
        return exams.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemExamBinding binding;
        public ViewHolder(ItemExamBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
