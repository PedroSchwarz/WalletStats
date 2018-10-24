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

public class RevenueActivity extends AppCompatActivity {

    private ConstraintLayout rLayout;
    private EditText rAmount;
    private ProgressBar rProg;
    private TextInputEditText rDate;
    private TextInputEditText rDesc;
    private Spinner rCat;

    private String category = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenue);

        rLayout = findViewById(R.id.r_layout);
        rAmount = findViewById(R.id.r_amount);
        rProg = findViewById(R.id.r_prog);
        rDate = findViewById(R.id.r_date);
        rDesc = findViewById(R.id.r_desc);
        rCat = findViewById(R.id.r_cat);
        Button rBtn = findViewById(R.id.r_btn);

        setCurrentDate();
        configSpinner();

        rBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateInputs();
            }
        });
    }

    private void validateInputs() {
        Double amount;
        String date = rDate.getText().toString();
        String desc = rDesc.getText().toString();
        amount = rAmount.getText().toString().isEmpty() ? 0.0 : Double.parseDouble(rAmount.getText().toString());

        if (!(date.isEmpty()) && !(desc.isEmpty())) {
            if (category != null) {
                rProg.setVisibility(View.VISIBLE);
                createTransaction(date, desc, amount);
            } else {
                Snackbar.make(rLayout, "You must choose the category.", Snackbar.LENGTH_LONG).show();
            }
        } else {
            Snackbar.make(rLayout, "You must enter the date and description.", Snackbar.LENGTH_LONG).show();
        }
    }

    private void createTransaction(String date, String desc, final Double amount) {
        DocumentReference transRef = DatabaseHelper.getTransactionRef(DateHelper.getDateKey(date)).document();
        Transaction transaction = new Transaction(transRef.getId(), desc, category, amount, date, "revenue");
        transRef.set(transaction).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    addAmountToUser(amount);
                } else {
                    rProg.setVisibility(View.GONE);
                    Snackbar.make(rLayout, "Something went wrong, try again later.", Snackbar.LENGTH_LONG).show();
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
                            Double oldAmount = task.getResult().getDouble("rAmount");
                            Double newAmount = amount + oldAmount;
                            DatabaseHelper.getUserRef().update("rAmount", newAmount)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                finish();
                                            } else {
                                                rProg.setVisibility(View.GONE);
                                                Snackbar.make(rLayout, "Something went wrong, try again later.", Snackbar.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        } else {
                            rProg.setVisibility(View.GONE);
                            Snackbar.make(rLayout, "Something went wrong, try again later.", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void setCurrentDate() {
        rDate.setText(DateHelper.getDateString());
    }

    private void configSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.revenues_cats, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rCat.setAdapter(adapter);

        rCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
