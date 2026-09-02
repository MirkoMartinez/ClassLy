package com.example.classly;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.classly.data.ClassInfo;
import com.example.classly.databinding.ItemClassBinding;
import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    private List<ClassInfo> classes;
    private OnClassActionListener listener;

    public interface OnClassActionListener {
        void onEdit(ClassInfo classInfo);
        void onDelete(ClassInfo classInfo);
    }

    public ScheduleAdapter(List<ClassInfo> classes, OnClassActionListener listener) {
        this.classes = classes;
        this.listener = listener;
    }

    public void updateData(List<ClassInfo> newClasses) {
        this.classes = newClasses;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemClassBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ClassInfo item = classes.get(position);
        holder.binding.tvSubject.setText(item.getSubject());
        holder.binding.tvTimeRoom.setText(item.getTime() + " - " + item.getRoom());
        holder.binding.tvProfessor.setText("Prof: " + item.getProfessor());

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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemClassBinding binding;
        public ViewHolder(ItemClassBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
