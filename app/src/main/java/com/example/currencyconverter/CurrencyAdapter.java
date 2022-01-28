package com.example.currencyconverter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.ViewHolder>{
    private List<Currency> currencyList;

    public CurrencyAdapter(List<Currency> currencyList) {
        this.currencyList = currencyList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.currency_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Currency currency = currencyList.get(position);
        holder.tvName.setText(currency.getName());
        holder.tvCurrencyValue.setText(String.format("%d %s = %.4f RUB", currency.getNominal(), currency.getCharCode(), currency.getValue()));
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
    }
}
