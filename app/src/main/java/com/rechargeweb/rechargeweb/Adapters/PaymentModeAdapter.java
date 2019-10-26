package com.rechargeweb.rechargeweb.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rechargeweb.rechargeweb.R;

import java.util.ArrayList;
import java.util.List;

public class PaymentModeAdapter extends RecyclerView.Adapter<PaymentModeAdapter.PaymentModeViewHolder> {

    private LayoutInflater inflater;
    private List<String>stringList = new ArrayList<>();
    OnPaymentItemClickcListener clickcListener;

    public interface OnPaymentItemClickcListener{
        void onPaymentClick(int position);
    }
    public PaymentModeAdapter(Context context,OnPaymentItemClickcListener clickcListener,List<String>stringList) {
        inflater = LayoutInflater.from(context);
        this.clickcListener = clickcListener;
        this.stringList = stringList;

    }

    @NonNull
    @Override
    public PaymentModeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PaymentModeViewHolder(inflater.inflate(R.layout.payment_mode_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentModeViewHolder holder, int position) {

        if (stringList!= null){
            holder.textView.setText(stringList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    //Get string
    public String getPaymentMode(int position){
        return stringList.get(position);
    }
    //Set string list
    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
        notifyDataSetChanged();
    }

    class PaymentModeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView;
        public PaymentModeViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.payment_mode_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            clickcListener.onPaymentClick(position);
        }
    }
}
