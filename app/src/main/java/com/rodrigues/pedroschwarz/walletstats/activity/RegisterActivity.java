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
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.rodrigues.pedroschwarz.walletstats.R;
import com.rodrigues.pedroschwarz.walletstats.helper.AuthHelper;
import com.rodrigues.pedroschwarz.walletstats.helper.DatabaseHelper;
import com.rodrigues.pedroschwarz.walletstats.model.User;

public class RegisterActivity extends AppCompatActivity {

    private ConstraintLayout regLayout;
    private EditText regName;
    private ProgressBar regProg;
    private TextInputEditText regEmail;
    private TextInputEditText regPass;
    private TextInputEditText regConPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regLayout = findViewById(R.id.reg_layout);
        regName = findViewById(R.id.reg_name);
        regProg = findViewById(R.id.reg_prog);
        regEmail = findViewById(R.id.reg_email);
        regPass = findViewById(R.id.reg_pass);
        regConPass = findViewById(R.id.reg_con_pass);
        Button regBtn = findViewById(R.id.reg_btn);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = regName.getText().toString();
                String email = regEmail.getText().toString();
                String pass = regPass.getText().toString();
                String conPass = regConPass.getText().toString();

                if (!(name.isEmpty()) && !(email.isEmpty()) && !(pass.isEmpty()) && !(conPass.isEmpty())) {
                    if (pass.equals(conPass)) {
                        regProg.setVisibility(View.VISIBLE);
                        registerUser(name, email, pass);
                    } else {
                        Snackbar.make(regLayout, "Both passwords must match.", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(regLayout, "The fields cannot be empty.", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void registerUser(final String name, String email, String pass) {
        AuthHelper.getAuth().createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(AuthHelper.getUserId(), name, 0.0, 0.0);
                            createUser(user);
                        } else {
                            regProg.setVisibility(View.GONE);

                            String error = "";

                            try {
                                throw task.getException();
                            } catch (FirebaseAuthUserCollisionException e) {
                                error = "This email is already in use.";
                            } catch (FirebaseAuthWeakPasswordException e) {
                                error = "Weak password, try another one.";
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                error = "Invalid email, try another one.";
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Snackbar.make(regLayout, error, Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void createUser(User user) {
        DatabaseHelper.getUserRef().set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    goToMain();
                } else {
                    regProg.setVisibility(View.GONE);
                    Snackbar.make(regLayout, "Something went wrong, try again later.", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void goToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
