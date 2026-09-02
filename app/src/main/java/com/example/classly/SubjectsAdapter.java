package com.example.classly;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.classly.data.SubjectInfo;
import com.example.classly.databinding.ItemSubjectBinding;
import java.util.List;

public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.ViewHolder> {

    private List<SubjectInfo> subjects;
    private OnSubjectActionListener listener;

    public interface OnSubjectActionListener {
        void onEdit(SubjectInfo subjectInfo);
        void onDelete(SubjectInfo subjectInfo);
    }

    public SubjectsAdapter(List<SubjectInfo> subjects, OnSubjectActionListener listener) {
        this.subjects = subjects;
        this.listener = listener;
    }

    public void updateData(List<SubjectInfo> newSubjects) {
        this.subjects = newSubjects;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemSubjectBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SubjectInfo item = subjects.get(position);
        holder.binding.tvName.setText(item.getName());
        holder.binding.tvDetails.setText("Créditos: " + item.getCredits() + " | Horas: " + item.getHours());
        holder.binding.tvGrade.setText(String.valueOf(item.getAverageGrade()));

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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemSubjectBinding binding;
        public ViewHolder(ItemSubjectBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
