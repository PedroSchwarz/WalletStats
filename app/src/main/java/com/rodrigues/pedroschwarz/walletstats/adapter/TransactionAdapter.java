package com.rodrigues.pedroschwarz.walletstats.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rodrigues.pedroschwarz.walletstats.R;
import com.rodrigues.pedroschwarz.walletstats.model.Transaction;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private List<Transaction> transactions;
    private int REVENUE_TYPE = 0;
    private int EXPENSE_TYPE = 1;

    public TransactionAdapter(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == REVENUE_TYPE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_revenue, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        holder.itemAmount.setText(String.valueOf(transaction.getAmount()));
        holder.itemDesc.setText(transaction.getDesc());
        holder.itemDate.setText(transaction.getDate());
        holder.itemCat.setText(transaction.getCategory());
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    @Override
    public int getItemViewType(int position) {
        Transaction transaction = transactions.get(position);
        switch (transaction.getType()) {
            case "revenue":
                return REVENUE_TYPE;
            default:
                return EXPENSE_TYPE;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemAmount;
        TextView itemDesc;
        TextView itemDate;
        TextView itemCat;

        ViewHolder(View itemView) {
            super(itemView);
            itemAmount = itemView.findViewById(R.id.item_amount);
            itemDesc = itemView.findViewById(R.id.item_desc);
            itemDate = itemView.findViewById(R.id.item_date);
            itemCat = itemView.findViewById(R.id.item_cat);
        }
    }
}
