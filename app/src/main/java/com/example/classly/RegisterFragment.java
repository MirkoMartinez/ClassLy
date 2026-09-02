package com.example.classly;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.classly.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnRegister.setOnClickListener(v -> {
            String name = (binding.etName.getText() != null) ? binding.etName.getText().toString().trim() : "";
            String surname = (binding.etSurname.getText() != null) ? binding.etSurname.getText().toString().trim() : "";
            String email = (binding.etEmail.getText() != null) ? binding.etEmail.getText().toString().trim() : "";
            String password = (binding.etPassword.getText() != null) ? binding.etPassword.getText().toString().trim() : "";

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(surname) || 
                TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(getContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                // Aquí iría el registro real con Firebase
                ((MainActivity) requireActivity()).navigateToMain();
            }
        });
        
        binding.btnNavigateLogin.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
