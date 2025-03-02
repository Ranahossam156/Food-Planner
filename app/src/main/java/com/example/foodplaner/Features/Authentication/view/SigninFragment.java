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

public class SigninFragment extends Fragment implements AuthView{

    EditText emailEditText, passwordEditText;
    Button signInButton, googleSignIn;
    TextView doNotHaveAnAccount;
    ProgressBar progressBar;
    FirebaseAuth auth;
    AuthPresenter authPresenter;
    GoogleSignInClient googleSignInClient;
    FirebaseUser user;
    private static final int RC_SIGN_IN = 123;

    public SigninFragment() {
        // Required empty public constructor
    }

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

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions);
        auth = FirebaseAuth.getInstance();
        emailEditText = view.findViewById(R.id.emailTextFieldlogin);
        passwordEditText = view.findViewById(R.id.PasswordTextFieldlogin);
        signInButton = view.findViewById(R.id.signinButton);
        doNotHaveAnAccount = view.findViewById(R.id.doNotHaveAnAccount);
        googleSignIn = view.findViewById(R.id.googleButtonLogin);
        auth=FirebaseAuth.getInstance();
        //auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        authPresenter=new AuthPresenterImplementation(getContext(),this);


        doNotHaveAnAccount.setOnClickListener(view1 -> {
            Navigation.findNavController(requireView())
                    .navigate(SigninFragmentDirections.actionSigninFragmentToLoginFragment());
        });

        signInButton.setOnClickListener(view12 -> signInWithEmail());

        googleSignIn.setOnClickListener(view13 -> signInWithGoogle());
    }

    private void signInWithEmail() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (isFieldsValid()) {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            SharedPreferences sharedPrefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                            sharedPrefs.edit().putBoolean("isGuest", false).apply();
                            user = auth.getCurrentUser();
                            if (user != null) {
                                authPresenter.restoreDataOnLogin(user.getUid());
                            } else {
                                Log.e("TAG", "User is null after login");
                            }
                        } else {
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }

    private void signInWithGoogle() {
        googleSignInClient.signOut().addOnCompleteListener(task -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
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
                        SharedPreferences sharedPrefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                        sharedPrefs.edit().putBoolean("isGuest", false).apply();
                        user = auth.getCurrentUser();
                        if (user != null) {
                            authPresenter.restoreDataOnLogin(user.getUid()); // Now user is not null
                        } else {
                            Log.e("TAG", "User is null after login");
                        }
                    } else {
                        Log.w("TAG", "signInWithEmail:failure", task.getException());
                        Toast.makeText(getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }

                });
    }
    private void navigateToHome() {
        NavOptions navOptions = new NavOptions.Builder()
                .setPopUpTo(R.id.signinFragment, true)
                .build();

        NavDirections action = SigninFragmentDirections.actionSigninFragmentToHomeFragment();
        Navigation.findNavController(requireView()).navigate(action, navOptions);
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

    @Override
    public void onBackupSuccess() {

    }

    @Override
    public void onRestoreSuccess() {
        navigateToHome();
    }

    @Override
    public void onBackupError(String message) {

    }

    @Override
    public void onRestoreError(String message) {

    }
}
