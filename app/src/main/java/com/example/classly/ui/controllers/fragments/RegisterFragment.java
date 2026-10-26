package com.example.classly.ui.controllers.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.classly.R;
import com.example.classly.ui.controllers.activities.MainActivity;
import com.example.classly.databinding.FragmentRegisterBinding;

/**
 * Fragmento de registro de nuevos usuarios en la aplicación.
 * Valida los campos obligatorios con validación estricta (Patterns.EMAIL_ADDRESS y seguridad de contraseña)
 * y permite regresar a la pantalla de inicio de sesión.
 */
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

        // Validación estricta y registro de usuario
        binding.btnRegister.setOnClickListener(v -> {
            String name = (binding.etName.getText() != null) ? binding.etName.getText().toString().trim() : "";
            String surname = (binding.etSurname.getText() != null) ? binding.etSurname.getText().toString().trim() : "";
            String email = (binding.etEmail.getText() != null) ? binding.etEmail.getText().toString().trim() : "";
            String password = (binding.etPassword.getText() != null) ? binding.etPassword.getText().toString().trim() : "";

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(surname) || 
                TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(getContext(), getString(R.string.toast_empty_fields), Toast.LENGTH_SHORT).show();
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(getContext(), "Formato de correo electrónico inválido", Toast.LENGTH_SHORT).show();
            } else if (password.length() < 6) {
                Toast.makeText(getContext(), "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            } else {
                ((MainActivity) requireActivity()).navigateToMain();
            }
        });
        
        // Regresar a la pantalla de inicio de sesión
        binding.btnNavigateLogin.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
