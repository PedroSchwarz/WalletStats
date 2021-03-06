package com.rodrigues.pedroschwarz.walletstats.activity;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.rodrigues.pedroschwarz.walletstats.R;
import com.rodrigues.pedroschwarz.walletstats.helper.DatabaseHelper;
import com.rodrigues.pedroschwarz.walletstats.helper.DateHelper;
import com.rodrigues.pedroschwarz.walletstats.model.Transaction;

public class ExpenseActivity extends AppCompatActivity {

    private ConstraintLayout eLayout;
    private EditText eAmount;
    private ProgressBar eProg;
    private TextInputEditText eDate;
    private TextInputEditText eDesc;
    private Spinner eCat;

    private String category = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        eLayout = findViewById(R.id.e_layout);
        eAmount = findViewById(R.id.e_amount);
        eProg = findViewById(R.id.e_prog);
        eDate = findViewById(R.id.e_date);
        eDesc = findViewById(R.id.e_desc);
        eCat = findViewById(R.id.e_cat);
        Button eBtn = findViewById(R.id.e_btn);

        setCurrentDate();
        configSpinner();

        eBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateInputs();
            }
        });
    }

    private void validateInputs() {
        Double amount;
        String date = eDate.getText().toString();
        String desc = eDesc.getText().toString();
        amount = eAmount.getText().toString().isEmpty() ? 0.0 : Double.parseDouble(eAmount.getText().toString());

        if (!(date.isEmpty()) && !(desc.isEmpty())) {
            if (category != null) {
                eProg.setVisibility(View.VISIBLE);
                createTransaction(date, desc, amount);
            } else {
                Snackbar.make(eLayout, "You must choose the category.", Snackbar.LENGTH_LONG).show();
            }
        } else {
            Snackbar.make(eLayout, "You must enter the date and description.", Snackbar.LENGTH_LONG).show();
        }
    }

    private void createTransaction(String date, String desc, final Double amount) {
        DocumentReference transRef = DatabaseHelper.getTransactionRef(DateHelper.getDateKey(date)).document();
        Transaction transaction = new Transaction(transRef.getId(), desc, category, amount, date, "expense");
        transRef.set(transaction).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    addAmountToUser(amount);
                } else {
                    eProg.setVisibility(View.GONE);
                    Snackbar.make(eLayout, "Something went wrong, try again later.", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void addAmountToUser(final Double amount) {
        DatabaseHelper.getUserRef().get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Double oldAmount = task.getResult().getDouble("eAmount");
                            Double newAmount = amount + oldAmount;
                            DatabaseHelper.getUserRef().update("eAmount", newAmount)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                finish();
                                            } else {
                                                eProg.setVisibility(View.GONE);
                                                Snackbar.make(eLayout, "Something went wrong, try again later.", Snackbar.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        } else {
                            eProg.setVisibility(View.GONE);
                            Snackbar.make(eLayout, "Something went wrong, try again later.", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void setCurrentDate() {
        eDate.setText(DateHelper.getDateString());
    }

    private void configSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.expenses_cats, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eCat.setAdapter(adapter);

        eCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = adapterView.getItemAtPosition(i).toString();
                if (category.equals("Select the category")) {
                    category = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
