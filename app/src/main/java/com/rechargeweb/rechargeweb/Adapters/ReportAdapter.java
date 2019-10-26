package com.rechargeweb.rechargeweb.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rechargeweb.rechargeweb.Model.Recharge;
import com.rechargeweb.rechargeweb.R;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    LayoutInflater inflater;
    List<Recharge> rechargeList;
    private static final String TAG = ReportAdapter.class.getSimpleName();

    public ReportAdapter(Context context, List<Recharge> rechargeList) {

        inflater = LayoutInflater.from(context);
        this.rechargeList = rechargeList;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReportViewHolder(inflater.inflate(R.layout.stauts_message, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {

        if (rechargeList != null) {
            holder.numberTv.setText(rechargeList.get(position).getNumber());
            holder.amount.setText(rechargeList.get(position).getAmount());
            holder.timeTv.setText(rechargeList.get(position).getCreated_on());
            holder.status.setText(rechargeList.get(position).getStatus());
            holder.txnTv.setText(rechargeList.get(position).getTxn_id());
            holder.optTxnId.setText(rechargeList.get(position).getOpt_txn_id());
        }

    }

    @Override
    public int getItemCount() {
        if (rechargeList != null) {
            int size = rechargeList.size();
            Log.e(TAG, "Size is: " + size);
            return rechargeList.size();
        } else {
            return 0;
        }
    }

    public void setRechargeList(List<Recharge> rechargeList) {
        this.rechargeList = rechargeList;
        notifyDataSetChanged();
    }

    class ReportViewHolder extends RecyclerView.ViewHolder {

        TextView numberTv;
        TextView amount;
        TextView timeTv;
        TextView status;
        TextView txnTv;
        TextView optTxnId;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            numberTv = itemView.findViewById(R.id.number_tv);
            amount = itemView.findViewById(R.id.recharge_amount);
            timeTv = itemView.findViewById(R.id.date_time_recharge);
            status = itemView.findViewById(R.id.recharge_status);
            txnTv = itemView.findViewById(R.id.txn_id);
            optTxnId = itemView.findViewById(R.id.opt_txn_id);
        }
    }
}
