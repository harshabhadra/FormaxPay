package com.rechargeweb.rechargeweb.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rechargeweb.rechargeweb.Model.CouponReport;
import com.rechargeweb.rechargeweb.R;

import java.util.List;

public class CouponReportAdapter extends RecyclerView.Adapter<CouponReportAdapter.CouponViewHolder> {

    private LayoutInflater inflater;
    private List<CouponReport>couponReportList;

    public CouponReportAdapter(Context context, List<CouponReport> couponReportList) {

        inflater = LayoutInflater.from(context);
        this.couponReportList = couponReportList;
    }

    @NonNull
    @Override
    public CouponViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CouponViewHolder(inflater.inflate(R.layout.coupon_report,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CouponViewHolder holder, int position) {

       CouponReport couponReport = couponReportList.get(position);
       if (couponReport != null){
           String psaID = couponReport.getPsaid() + " (" + couponReport.getName() + ")";
           holder.psaId.setText(psaID);
           holder.name.setText(couponReport.getName());
           String quantity = couponReport.getQuantity();

           String totalP = couponReport.getTotal_price();
           holder.price.setText(totalP);
           holder.quanity.setText(quantity);
           String tx = couponReport.getTxn_id();
           if (tx == null){
               holder.txn_id.setText("N/A");
           }else {
               holder.txn_id.setText(tx);
           }

           String couponStatus = couponReport.getStatus();
           holder.status.setText(couponStatus);
           if (couponStatus.equals("Approved")){
               holder.status.setTextColor(Color.parseColor("#009624"));
           }else {
               holder.status.setTextColor(Color.parseColor("#da4302"));
           }

           holder.created_on.setText(couponReport.getCreated_on());
       }
    }

    @Override
    public int getItemCount() {
        return couponReportList.size();
    }

    public class CouponViewHolder extends RecyclerView.ViewHolder{

        TextView psaId;
        TextView name;
        TextView quanity;
        TextView txn_id;
        TextView status;
        TextView created_on;
        TextView price;

        public CouponViewHolder(@NonNull View itemView) {
            super(itemView);

            psaId = itemView.findViewById(R.id.psa_id_tv);
            name = itemView.findViewById(R.id.name_tv);
            quanity = itemView.findViewById(R.id.quanitity_tv);
            txn_id = itemView.findViewById(R.id.txn_tv_c);
            status = itemView.findViewById(R.id.status_tv);
            created_on = itemView.findViewById(R.id.created_on_tv);
            price = itemView.findViewById(R.id.couponPrice);
        }
    }
}
