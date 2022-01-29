package com.example.currencyconverter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.ViewHolder>{

    public interface OnItemClickListener {
        void onItemClick(Currency currency);
    }

    private List<Currency> currencyList;
    private OnItemClickListener listener;

    public CurrencyAdapter(List<Currency> currencyList, OnItemClickListener listener) {
        this.currencyList = currencyList;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.currency_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(currencyList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        if (currencyList == null)
            return 0;
        return currencyList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvCurrencyValue;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvCurrencyValue = (TextView) itemView.findViewById(R.id.tvCurrencyValue);
        }
        public void bind(final Currency currency, final OnItemClickListener listener) {
            tvName.setText(currency.getName());
            tvCurrencyValue.setText(String.format("%d %s = %.4f RUB", currency.getNominal(), currency.getCharCode(), currency.getValue()));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(currency);
                }
            });
        }
    }
}
