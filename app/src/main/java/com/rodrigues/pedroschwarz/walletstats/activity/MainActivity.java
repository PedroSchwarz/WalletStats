package com.rodrigues.pedroschwarz.walletstats.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.rodrigues.pedroschwarz.walletstats.R;
import com.rodrigues.pedroschwarz.walletstats.activity.ExpenseActivity;
import com.rodrigues.pedroschwarz.walletstats.activity.RevenueActivity;
import com.rodrigues.pedroschwarz.walletstats.adapter.TransactionAdapter;
import com.rodrigues.pedroschwarz.walletstats.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView mainName;
    private TextView mainTAmount;
    private MaterialCalendarView mainCal;
    private RecyclerView mainRv;
    private FloatingActionMenu mainFabMenu;

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

        mainName.setText("Pedro Schwarz");

        configRecycler();
        configCalendar();
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
                String d = String.valueOf(date.getMonth() + 1) + String.valueOf(date.getYear());
                Toast.makeText(MainActivity.this, d, Toast.LENGTH_LONG).show();
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
    public void onBackPressed() {
        if (mainFabMenu.isOpened()) {
            mainFabMenu.close(true);
        } else {
            super.onBackPressed();
        }
    }
}
