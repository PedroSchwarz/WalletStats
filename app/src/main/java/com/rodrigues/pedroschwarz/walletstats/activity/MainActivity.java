package com.rodrigues.pedroschwarz.walletstats.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.rodrigues.pedroschwarz.walletstats.adapter.TransactionAdapter;
import com.rodrigues.pedroschwarz.walletstats.helper.AuthHelper;
import com.rodrigues.pedroschwarz.walletstats.helper.DatabaseHelper;
import com.rodrigues.pedroschwarz.walletstats.helper.DateHelper;
import com.rodrigues.pedroschwarz.walletstats.model.Transaction;
import com.rodrigues.pedroschwarz.walletstats.model.User;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar mainToolbar;
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

        mainToolbar = findViewById(R.id.main_toolbar);
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

        configToolbar();
        configRecycler();
        swipeListItems();
        configCalendar();
    }

    private void getTransactions(String date) {
        if (transactions.isEmpty()) {
            mainProg.setVisibility(View.VISIBLE);
        }
        DatabaseHelper.getTransactionsRef(date)
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

    private void configToolbar() {
        mainToolbar.setTitle("");
        mainToolbar.setElevation(0f);
        setSupportActionBar(mainToolbar);
    }

    private void configRecycler() {
        transactions = new ArrayList<>();
        adapter = new TransactionAdapter(transactions);

        mainRv.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        mainRv.setLayoutManager(new LinearLayoutManager(this));
        mainRv.setAdapter(adapter);
    }

    private void swipeListItems() {

        ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;

                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Delete transaction");
                builder.setMessage("Are you sure?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteTransaction(viewHolder);
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.create().show();
            }
        };

        new ItemTouchHelper(callback).attachToRecyclerView(mainRv);
    }

    private void deleteTransaction(RecyclerView.ViewHolder viewHolder) {
        int position = viewHolder.getAdapterPosition();
        final Transaction transaction = transactions.get(position);
        DatabaseHelper.getTransactionRef(DateHelper.getDateKey(transaction.getDate())).document(transaction.getId()).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            DatabaseHelper.getUserRef().get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                if (transaction.getType().equals("revenue")) {
                                                    Double oldAmount = task.getResult().getDouble("rAmount");
                                                    Double newAmount = oldAmount - transaction.getAmount();
                                                    DatabaseHelper.getUserRef().update("rAmount", newAmount);
                                                    updateUserAndList(transaction.getDate());
                                                } else {
                                                    Double oldAmount = task.getResult().getDouble("eAmount");
                                                    Double newAmount = oldAmount - transaction.getAmount();
                                                    DatabaseHelper.getUserRef().update("eAmount", newAmount);
                                                    updateUserAndList(transaction.getDate());
                                                }
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void updateUserAndList(String date) {
        getCurrentUser();
        getTransactions(DateHelper.getDateKey(date));
    }

    private void configCalendar() {
        mainCal.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                mainNoTrans.setVisibility(View.GONE);
                String d = String.valueOf(date.getMonth() + 1) + String.valueOf(date.getYear());
                if (date.getMonth() + 1 < 10) {
                    d = "0" + String.valueOf(date.getMonth() + 1) + String.valueOf(date.getYear());
                }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_chart:
                goToGraphs();
                return true;
            default:
                logoutUser();
                return true;
        }
    }

    private void goToGraphs() {
        startActivity(new Intent(this, GraphsActivity.class));
    }

    private void logoutUser() {
        AuthHelper.signOutUser();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
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
