package com.example.foodplaner.Features.Authentication.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodplaner.Features.Home.view.HomeFragment;
import com.example.foodplaner.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class SigninFragment extends Fragment {

    EditText emailEditText, passwordEditText;
    Button signInButton, googleSignIn;
    TextView doNotHaveAnAccount;
    ProgressBar progressBar;
    FirebaseAuth auth;
    GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 123;

    public SigninFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_id_firebase))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions);
        auth = FirebaseAuth.getInstance();
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
        progressBar = view.findViewById(R.id.progressBar2);
        googleSignIn = view.findViewById(R.id.googleButtonLogin);
        progressBar.setVisibility(View.GONE);

        doNotHaveAnAccount.setOnClickListener(view1 -> {
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainerView, new SignupFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        signInButton.setOnClickListener(view12 -> signInWithEmail());

        googleSignIn.setOnClickListener(view13 -> signInWithGoogle());
    }

    private void signInWithEmail() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (isFieldsValid()) {
            progressBar.setVisibility(View.VISIBLE);
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            navigateToHome();
                        } else {
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
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
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w("TAG", "Google sign in failed", e);
                Toast.makeText(getContext(), "Google Sign-In Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        Log.d("TAG", "signInWithCredential:success");
                        navigateToHome();
                    } else {
                        Log.w("TAG", "signInWithCredential:failure", task.getException());
                        Toast.makeText(getContext(), "Authentication Failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void navigateToHome() {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerView, new HomeFragment());
        transaction.addToBackStack(null);
        transaction.commit();
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
