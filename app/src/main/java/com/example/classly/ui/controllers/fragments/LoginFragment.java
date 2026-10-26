package com.example.classly.ui.controllers.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;
import androidx.fragment.app.Fragment;
import com.example.classly.R;
import com.example.classly.ui.controllers.activities.MainActivity;
import com.example.classly.databinding.FragmentLoginBinding;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Fragmento de inicio de sesión (Login).
 * Permite autenticarse mediante correo y contraseña (con validación estricta de formato y seguridad)
 * o a través de Google Sign-In con Credential Manager API.
 */
public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private static final String TAG = "LoginFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ocultar la barra de navegación inferior en la pantalla de login
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setBottomNavVisibility(false);
        }

        // Botón de inicio de sesión con correo y contraseña con validaciones estrictas
        binding.btnLogin.setOnClickListener(v -> {
            String email = (binding.etEmail.getText() != null) ? binding.etEmail.getText().toString().trim() : "";
            String password = (binding.etPassword.getText() != null) ? binding.etPassword.getText().toString().trim() : "";

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(getContext(), getString(R.string.toast_empty_fields), Toast.LENGTH_SHORT).show();
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(getContext(), "Formato de correo electrónico inválido", Toast.LENGTH_SHORT).show();
            } else if (password.length() < 6) {
                Toast.makeText(getContext(), "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            } else {
                ((MainActivity) requireActivity()).navigateToMain();
            }
        });

        // Botón de inicio de sesión con Google
        binding.btnGoogle.setOnClickListener(v -> signInWithGoogle());

        // Navegar a la pantalla de registro
        binding.btnNavigateRegister.setOnClickListener(v -> ((MainActivity) requireActivity()).showFragment(new RegisterFragment(), true));
    }

    /**
     * Inicia el flujo de autenticación con Google utilizando Credential Manager.
     */
    private void signInWithGoogle() {
        CredentialManager credentialManager = CredentialManager.create(requireContext());

        GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(getString(R.string.google_client_id))
                .build();

        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build();

        Executor executor = Executors.newSingleThreadExecutor();
        Handler mainHandler = new Handler(Looper.getMainLooper());

        credentialManager.getCredentialAsync(
                requireContext(),
                request,
                null,
                executor,
                new androidx.credentials.CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
                    @Override
                    public void onResult(GetCredentialResponse result) {
                        handleSignInResult(result.getCredential());
                    }

                    @Override
                    public void onError(@NonNull GetCredentialException e) {
                        Log.e(TAG, "Error en Google Sign-In", e);
                        mainHandler.post(() -> Toast.makeText(getContext(), getString(R.string.toast_google_error), Toast.LENGTH_SHORT).show());
                    }
                }
        );
    }

    /**
     * Maneja el resultado obtenido de Google Sign-In y procesa la credencial de ID Token.
     */
    private void handleSignInResult(Credential credential) {
        if (credential instanceof GoogleIdTokenCredential) {
            GoogleIdTokenCredential googleIdTokenCredential = (GoogleIdTokenCredential) credential;
            String idToken = googleIdTokenCredential.getIdToken();
            String displayName = googleIdTokenCredential.getDisplayName();

            Log.d(TAG, "Google ID Token: " + idToken);
            
            new Handler(Looper.getMainLooper()).post(() -> {
                Toast.makeText(getContext(), getString(R.string.toast_welcome, displayName), Toast.LENGTH_SHORT).show();
                ((MainActivity) requireActivity()).navigateToMain();
            });
        } else {
            Log.e(TAG, "Tipo de credencial no esperado");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
