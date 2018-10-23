package com.rodrigues.pedroschwarz.walletstats.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.rodrigues.pedroschwarz.walletstats.R;
import com.rodrigues.pedroschwarz.walletstats.activity.ExpenseActivity;
import com.rodrigues.pedroschwarz.walletstats.activity.RevenueActivity;

public class MainActivity extends AppCompatActivity {

    private TextView mainName;
    private TextView mainTAmount;
    private MaterialCalendarView mainCal;
    private RecyclerView mainRv;
    private FloatingActionMenu mainFabMenu;
    private FloatingActionButton mainRFab;
    private FloatingActionButton mainEFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainName = findViewById(R.id.main_name);
        mainTAmount = findViewById(R.id.main_t_amount);
        mainCal = findViewById(R.id.main_cal);
        mainRv = findViewById(R.id.main_rv);
        mainFabMenu = findViewById(R.id.main_fab_menu);
        mainRFab = findViewById(R.id.main_r_fab);
        mainEFab = findViewById(R.id.main_e_fab);

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
    }

    private void onGoAddRevenue() {
        startActivity(new Intent(this, RevenueActivity.class));
    }

    private void onGoAddExpense() {
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
