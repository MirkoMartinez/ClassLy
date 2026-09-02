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
import com.example.classly.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnLogin.setOnClickListener(v -> {
            String email = (binding.etEmail.getText() != null) ? binding.etEmail.getText().toString().trim() : "";
            String password = (binding.etPassword.getText() != null) ? binding.etPassword.getText().toString().trim() : "";

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(getContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                // Aquí iría la lógica de Firebase. Por ahora simulamos éxito.
                ((MainActivity) requireActivity()).navigateToMain();
            }
        });

        binding.btnGoogle.setOnClickListener(v -> {
            // Lógica simulada de Google Sign-In
            ((MainActivity) requireActivity()).navigateToMain();
        });

        binding.btnNavigateRegister.setOnClickListener(v -> ((MainActivity) requireActivity()).showFragment(new RegisterFragment(), true));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
