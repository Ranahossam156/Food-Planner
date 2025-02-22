package com.example.foodplaner.Features.Authentication.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodplaner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class SignupFragment extends Fragment {
    EditText emailEditText, passwordEditText, repeatPasswordEditText;
    Button signupButton;
    TextView AlreadyHaveAnAccount;
    FirebaseAuth auth;
    ProgressBar progressBar;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[@#$%^&+=])" +
                    "(?=\\S+$)" +
                    ".{4,}" +
                    "$");

    public SignupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View bottomNav = getActivity().findViewById(R.id.bottomNavigationView);
        if (bottomNav != null) {
            bottomNav.setVisibility(View.GONE);
        }
        emailEditText =view.findViewById(R.id.emailTextFieldSignup);
        passwordEditText =view.findViewById(R.id.PasswordTextFieldSignup);
        repeatPasswordEditText =view.findViewById(R.id.RepeatPasswordTextFieldSignup);
        signupButton=view.findViewById(R.id.SignupButton);
        AlreadyHaveAnAccount=view.findViewById(R.id.AlreadyHaveAnAccountBtn);
        progressBar=view.findViewById(R.id.progressBar);
        progressBar.setVisibility(view.GONE);
        auth=FirebaseAuth.getInstance();
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email,password,repeatedPassword;
                email =String.valueOf(emailEditText.getText());
                password=String.valueOf(passwordEditText.getText());
                if(isFieldsValid())
                {
                    progressBar.setVisibility(view.VISIBLE);
                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(view.GONE);
                                    if (task.isSuccessful()) {
                                        Log.d("TAG", "createUserWithEmail:success");
                                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                        transaction.replace(R.id.fragmentContainerView, new SigninFragment());
                                        transaction.addToBackStack(null);
                                        transaction.commit();
//                                        FirebaseUser user = auth.getCurrentUser();
//                                        updateUI(user);
                                    } else {
                                        Log.w("TAG", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(getContext(), "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        //updateUI(null);
                                    }
                                }
                            });
                }
            }
        });
        AlreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainerView, new SigninFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    private boolean validateEmail() {

        String emailInput = emailEditText.getText().toString().trim();

        if (emailInput.isEmpty()) {
            emailEditText.setError("Field can not be empty");
            return false;
        }

        else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
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
            passwordEditText.setError("Field can not be empty");
            return false;
        }
        else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            passwordEditText.setError("Password is too weak");
            return false;
        } else {
            passwordEditText.setError(null);
            return true;
        }
    }
    private boolean isFieldsValid(){
        if(!validateEmail()||!validatePassword())
        {
            return false;
        }
        else if (!repeatPasswordEditText.getText().toString().trim().equals(passwordEditText.getText().toString().trim())) {
            repeatPasswordEditText.setError("Password not match");
            return false;
        }
        else if (repeatPasswordEditText.getText().toString().trim().isEmpty()) {
            repeatPasswordEditText.setError("Field can not be empty");
            return false;
        }
        return true;
    }

    public void confirmInput(View v) {
        if (!validateEmail() | !validatePassword()) {
            return;
        }
        String input = "Email: " + emailEditText.getText().toString();
        input += "\n";
        input += "Password: " + passwordEditText.getText().toString();
        Toast.makeText(getContext(), input, Toast.LENGTH_SHORT).show();
    }
}