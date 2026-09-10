package com.example.classly;

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
import com.example.classly.databinding.FragmentLoginBinding;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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

        // Configuramos el botón de entrar: validamos que los campos no estén vacíos
        binding.btnLogin.setOnClickListener(v -> {
            String email = (binding.etEmail.getText() != null) ? binding.etEmail.getText().toString().trim() : "";
            String password = (binding.etPassword.getText() != null) ? binding.etPassword.getText().toString().trim() : "";

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(getContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                // Si todo está ok, pasamos al contenido principal de la app
                ((MainActivity) requireActivity()).navigateToMain();
            }
        });

        // Este botón activa el flujo de Google usando la API de Credential Manager
        binding.btnGoogle.setOnClickListener(v -> signInWithGoogle());

        binding.btnNavigateRegister.setOnClickListener(v -> ((MainActivity) requireActivity()).showFragment(new RegisterFragment(), true));
    }

    // Aquí pedimos a Google que nos muestre el selector de cuentas del usuario
    private void signInWithGoogle() {
        CredentialManager credentialManager = CredentialManager.create(requireContext());

        // Configuramos qué queremos de Google (en este caso, el ID Token)
        GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(getString(R.string.google_client_id))
                .build();

        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build();

        Executor executor = Executors.newSingleThreadExecutor();
        Handler mainHandler = new Handler(Looper.getMainLooper());

        // Lanzamos la petición de forma asíncrona para no trabar la pantalla
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
                        mainHandler.post(() -> Toast.makeText(getContext(), "Error al iniciar sesión con Google", Toast.LENGTH_SHORT).show());
                    }
                }
        );
    }

    private void handleSignInResult(Credential credential) {
        if (credential instanceof GoogleIdTokenCredential) {
            GoogleIdTokenCredential googleIdTokenCredential = (GoogleIdTokenCredential) credential;
            String idToken = googleIdTokenCredential.getIdToken();
            String displayName = googleIdTokenCredential.getDisplayName();

            Log.d(TAG, "Google ID Token: " + idToken);
            
            new Handler(Looper.getMainLooper()).post(() -> {
                Toast.makeText(getContext(), "Bienvenido, " + displayName, Toast.LENGTH_SHORT).show();
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
