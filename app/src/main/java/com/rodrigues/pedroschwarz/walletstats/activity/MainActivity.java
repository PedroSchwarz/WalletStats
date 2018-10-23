package com.rodrigues.pedroschwarz.walletstats.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.rodrigues.pedroschwarz.walletstats.R;
import com.rodrigues.pedroschwarz.walletstats.activity.ExpenseActivity;
import com.rodrigues.pedroschwarz.walletstats.activity.RevenueActivity;
import com.rodrigues.pedroschwarz.walletstats.adapter.TransactionAdapter;
import com.rodrigues.pedroschwarz.walletstats.helper.DatabaseHelper;
import com.rodrigues.pedroschwarz.walletstats.helper.DateHelper;
import com.rodrigues.pedroschwarz.walletstats.model.Transaction;
import com.rodrigues.pedroschwarz.walletstats.model.User;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView mainName;
    private TextView mainTAmount;
    private MaterialCalendarView mainCal;
    private RecyclerView mainRv;
    private FloatingActionMenu mainFabMenu;
    private TextView mainNoTrans;
    private ProgressBar mainProg;

    private List<Transaction> transactions;
    private TransactionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainName = findViewById(R.id.main_name);
        mainTAmount = findViewById(R.id.main_t_amount);
        mainCal = findViewById(R.id.main_cal);
        mainRv = findViewById(R.id.main_rv);
        mainFabMenu = findViewById(R.id.main_fab_menu);
        FloatingActionButton mainRFab = findViewById(R.id.main_r_fab);
        FloatingActionButton mainEFab = findViewById(R.id.main_e_fab);
        mainNoTrans = findViewById(R.id.main_no_trans);
        mainProg = findViewById(R.id.main_prog);

        mainRFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onGoAddRevenue();
            }
        });

        mainEFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onGoAddExpense();
            }
        });

        configRecycler();
        configCalendar();
    }

    private void getTransactions(String date) {
        if (transactions.isEmpty()) {
            mainProg.setVisibility(View.VISIBLE);
        }
        DatabaseHelper.getTransactionRef(date)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                transactions.clear();
                for (DocumentSnapshot snapshot : task.getResult()) {
                    Transaction transaction = snapshot.toObject(Transaction.class);
                    transactions.add(transaction);
                }
                updateUI();
            }
        });
    }

    private void getCurrentUser() {
        DatabaseHelper.getUserRef().get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            User user = task.getResult().toObject(User.class);
                            mainName.setText(user.getName());
                            Double rAmount = user.getrAmount();
                            Double eAmount = user.geteAmount();
                            mainTAmount.setText(String.valueOf(rAmount - eAmount));
                        }
                    }
                });
    }

    private void updateUI() {
        adapter.notifyDataSetChanged();
        mainProg.setVisibility(View.GONE);
        if (transactions.isEmpty()) {
            mainNoTrans.setVisibility(View.VISIBLE);
        } else {
            mainNoTrans.setVisibility(View.GONE);
        }
    }

    private void configRecycler() {
        transactions = new ArrayList<>();
        adapter = new TransactionAdapter(transactions);

        mainRv.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        mainRv.setLayoutManager(new LinearLayoutManager(this));
        mainRv.setAdapter(adapter);
    }

    private void configCalendar() {
        mainCal.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                mainNoTrans.setVisibility(View.GONE);
                String d = String.valueOf(date.getMonth() + 1) + String.valueOf(date.getYear());
                getTransactions(d);
            }
        });
    }

    private void onGoAddRevenue() {
        mainFabMenu.close(true);
        startActivity(new Intent(this, RevenueActivity.class));
    }

    private void onGoAddExpense() {
        mainFabMenu.close(true);
        startActivity(new Intent(this, ExpenseActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        getTransactions(DateHelper.getDateKey(DateHelper.getDateString()));
        getCurrentUser();
    }

    @Override
    public void onBackPressed() {
        if (mainFabMenu.isOpened()) {
            mainFabMenu.close(true);
        } else {
            super.onBackPressed();
        }
    }
}
