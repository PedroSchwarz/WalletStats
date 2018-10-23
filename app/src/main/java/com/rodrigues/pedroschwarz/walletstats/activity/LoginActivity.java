package com.rodrigues.pedroschwarz.walletstats.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.rodrigues.pedroschwarz.walletstats.R;
import com.rodrigues.pedroschwarz.walletstats.helper.AuthHelper;

public class LoginActivity extends AppCompatActivity {

    private ConstraintLayout logLayout;
    private ProgressBar logProg;
    private TextInputEditText logEmail;
    private TextInputEditText logPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        checkAuth();

        logLayout = findViewById(R.id.log_layout);
        logProg = findViewById(R.id.log_prog);
        logEmail = findViewById(R.id.log_email);
        logPass = findViewById(R.id.log_pass);
        Button logBtn = findViewById(R.id.log_btn);
        Button logRegBtn = findViewById(R.id.log_reg_btn);

        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = logEmail.getText().toString();
                String pass = logPass.getText().toString();

                if (!(email.isEmpty()) && !(pass.isEmpty())) {
                    logProg.setVisibility(View.VISIBLE);
                    loginUser(email, pass);
                } else {
                    Snackbar.make(logLayout, "You must enter the email and password.", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        logRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onGoToReg();
            }
        });
    }

    private void checkAuth() {
        if (AuthHelper.getAuth().getCurrentUser() != null) {
            goToMain();
        }
    }

    private void loginUser(String email, String pass) {
        AuthHelper.getAuth().signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                             goToMain();
                        } else {
                            String error = "";

                            logProg.setVisibility(View.GONE);

                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                error = "Invalid email/password.";
                            } catch (FirebaseAuthInvalidUserException e) {
                                error = "Account not found or was blocked.";
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Snackbar.make(logLayout, error, Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void goToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void onGoToReg() {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}
