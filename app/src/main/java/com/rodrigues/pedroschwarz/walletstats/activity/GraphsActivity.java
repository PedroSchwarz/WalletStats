package com.rodrigues.pedroschwarz.walletstats.activity;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.rodrigues.pedroschwarz.walletstats.R;
import com.rodrigues.pedroschwarz.walletstats.helper.DatabaseHelper;
import com.rodrigues.pedroschwarz.walletstats.helper.DateHelper;
import com.rodrigues.pedroschwarz.walletstats.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class GraphsActivity extends AppCompatActivity {

    private MaterialCalendarView graphsCal;
    private BarChart graphsBar;
    private TextView graphsNoTrans;
    private ProgressBar graphsProg;

    private Double rTotal;
    private Double eTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphs);

        graphsCal = findViewById(R.id.graphs_cal);
        graphsBar = findViewById(R.id.graphs_bar);
        graphsNoTrans = findViewById(R.id.graphs_no_trans);
        graphsProg = findViewById(R.id.graphs_prog);

        configCal();
        getTransactions(DateHelper.getDateKey(DateHelper.getDateString()));
    }

    private void configCal() {
        graphsCal.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                String d = String.valueOf(date.getMonth() + 1) + String.valueOf(date.getYear());
                if (date.getMonth() + 1 < 10) {
                    d = "0" + String.valueOf(date.getMonth() + 1) + String.valueOf(date.getYear());
                }
                getTransactions(d);
            }
        });
    }

    private void configGraph() {
        graphsBar.getDescription().setEnabled(false);
        graphsBar.getLegend().setTextSize(12f);
        graphsBar.getLegend().setXEntrySpace(12f);
        graphsBar.animateY(1500);

        List<BarEntry> revenues = new ArrayList<>();
        revenues.add(new BarEntry(0f, Float.valueOf(rTotal.toString())));
        List<BarEntry> expenses = new ArrayList<>();
        expenses.add(new BarEntry(2f, Float.valueOf(eTotal.toString())));

        BarDataSet setRev = new BarDataSet(revenues, "Revenue Total");
        setRev.setColor(Color.parseColor("#388E3C"));
        setRev.setValueFormatter(new LargeValueFormatter());
        setRev.setValueTextSize(16f);
        BarDataSet setExp = new BarDataSet(expenses, "Expense Total");
        setExp.setColor(Color.parseColor("#E64A19"));
        setExp.setValueFormatter(new LargeValueFormatter());
        setExp.setValueTextSize(16f);

        BarData data = new BarData(setRev, setExp);

        data.setBarWidth(1.5f); // set custom bar width
        graphsBar.setData(data);
        graphsBar.setFitBars(true); // make the x-axis fit exactly all bars
        graphsBar.invalidate(); // refresh
    }

    private void getTransactions(String date) {
        graphsNoTrans.setVisibility(View.GONE);
        graphsBar.setVisibility(View.GONE);
        graphsProg.setVisibility(View.VISIBLE);
        rTotal = 0.0;
        eTotal = 0.0;
        DatabaseHelper.getTransactionsRef(date)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.getResult().isEmpty()) {
                    graphsProg.setVisibility(View.GONE);
                    graphsNoTrans.setVisibility(View.VISIBLE);
                } else {
                    for (DocumentSnapshot snapshot : task.getResult()) {
                        Transaction transaction = snapshot.toObject(Transaction.class);
                        if (transaction.getType().equals("revenue")) {
                            rTotal += transaction.getAmount();
                        } else {
                            eTotal += transaction.getAmount();
                        }
                    }
                    graphsProg.setVisibility(View.GONE);
                    graphsBar.setVisibility(View.VISIBLE);
                    configGraph();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        configGraph();
    }
}
