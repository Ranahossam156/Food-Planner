package com.example.foodplaner.Features.Authentication.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodplaner.Features.Authentication.presenter.AuthPresenter;
import com.example.foodplaner.Features.Authentication.presenter.AuthPresenterImplementation;
import com.example.foodplaner.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

public class SignupFragment extends Fragment implements AuthView {
    private EditText emailEditText, passwordEditText, repeatPasswordEditText;
    private Button signupButton, googleSignup;
    private TextView AlreadyHaveAnAccount;
    private ProgressBar progressBar;
    private GoogleSignInClient googleSignInClient;
    private AuthPresenter authPresenter;
    private static final int RC_SIGN_IN = 123;

    public SignupFragment() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emailEditText = view.findViewById(R.id.emailTextFieldSignup);
        passwordEditText = view.findViewById(R.id.PasswordTextFieldSignup);
        repeatPasswordEditText = view.findViewById(R.id.RepeatPasswordTextFieldSignup);
        signupButton = view.findViewById(R.id.SignupButton);
        googleSignup = view.findViewById(R.id.googleButtonSignup);
        AlreadyHaveAnAccount = view.findViewById(R.id.AlreadyHaveAnAccountBtn);

        authPresenter = new AuthPresenterImplementation(getContext(),this);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions);

        signupButton.setOnClickListener(v -> signUpWithEmail());
        googleSignup.setOnClickListener(v -> signInWithGoogle());
        AlreadyHaveAnAccount.setOnClickListener(v ->
                Navigation.findNavController(view).navigate(SignupFragmentDirections.actionLoginFragmentToSigninFragment()));
    }

    private void signUpWithEmail() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (isFieldsValid()) {
            authPresenter.signUpWithEmail(email, password);
            authPresenter.setGuest(false);
        }
    }
    private boolean validateEmail() {
        String emailInput = emailEditText.getText().toString().trim();
        if (emailInput.isEmpty()) {
            emailEditText.setError("Field cannot be empty");
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            emailEditText.setError("Please enter a valid email address");
            return false;
        } else {
            emailEditText.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = passwordEditText.getText().toString().trim();
        if (passwordInput.isEmpty()) {
            passwordEditText.setError("Field cannot be empty");
            return false;
        } else {
            passwordEditText.setError(null);
            return true;
        }
    }

    private boolean isFieldsValid() {
        return validateEmail() && validatePassword();
    }
    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                authPresenter.signInWithGoogle(account.getIdToken());
                authPresenter.setGuest(false);
            } catch (ApiException e) {
                Toast.makeText(getContext(), "Google Sign-In Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackupSuccess() {

    }

    @Override
    public void onRestoreSuccess() {

    }

    @Override
    public void onBackupError(String message) {

    }

    @Override
    public void onRestoreError(String message) {

    }

    @Override
    public void onAuthSuccess(FirebaseUser user) {

        Toast.makeText(getContext(), "Signup Successful", Toast.LENGTH_SHORT).show();
        Navigation.findNavController(requireView()).navigate(SignupFragmentDirections.actionLoginFragmentToHomeFragment());
    }

    @Override
    public void onAuthFailure(String message) {

        Toast.makeText(getContext(), "Authentication Failed: " + message, Toast.LENGTH_SHORT).show();
    }
}
