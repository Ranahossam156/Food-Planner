package com.example.foodplaner.Features.Authentication.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodplaner.Database.MealsLocalDataSourceImplementation;
import com.example.foodplaner.Features.Authentication.presenter.AuthPresenter;
import com.example.foodplaner.Features.Authentication.presenter.AuthPresenterImplementation;
import com.example.foodplaner.R;
import com.example.foodplaner.model.MealRepository;
import com.example.foodplaner.model.MealRepositoryImplementation;
import com.example.foodplaner.network.MealsRemoteDataSourceImplementaion;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SigninFragment extends Fragment implements AuthView {
    private EditText emailEditText, passwordEditText;
    private Button signInButton, googleSignIn;
    private TextView doNotHaveAnAccount;
    private AuthPresenter authPresenter;
    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 123;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emailEditText = view.findViewById(R.id.emailTextFieldlogin);
        passwordEditText = view.findViewById(R.id.PasswordTextFieldlogin);
        signInButton = view.findViewById(R.id.signinButton);
        doNotHaveAnAccount = view.findViewById(R.id.doNotHaveAnAccount);
        googleSignIn = view.findViewById(R.id.googleButtonLogin);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // Ensure this ID is correct
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso);
        authPresenter = new AuthPresenterImplementation(getContext(), this);

        signInButton.setOnClickListener(v -> signInWithEmail());
        googleSignIn.setOnClickListener(v -> signInWithGoogle());
        doNotHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(
                        SigninFragmentDirections.actionSigninFragmentToLoginFragment()
                );
            }
        });
    }

    private void signInWithEmail() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (isFieldsValid()) {
            authPresenter.signInWithEmail(email, password);
            authPresenter.setGuest(false);
        }
    }

    private void signInWithGoogle() {
        googleSignInClient.signOut().addOnCompleteListener(task -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });}

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
        SharedPreferences sharedPrefs = getContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        sharedPrefs.edit().putBoolean("isGuest", false).apply();
        navigateToHome();
    }
    private void navigateToHome() {
        NavOptions navOptions = new NavOptions.Builder()
                .setPopUpTo(R.id.signinFragment, true)
                .build();

        NavDirections action = SigninFragmentDirections.actionSigninFragmentToHomeFragment();
        Navigation.findNavController(requireView()).navigate(action, navOptions);
    }
    @Override
    public void onBackupError(String message) {

    }

    @Override
    public void onRestoreError(String message) {
        Toast.makeText(getContext(), "Restore failed: " + message, Toast.LENGTH_SHORT).show();
        navigateToHome();
    }

    @Override
    public void onAuthSuccess(FirebaseUser user) {

    }

    @Override
    public void onAuthFailure(String message) {

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
}