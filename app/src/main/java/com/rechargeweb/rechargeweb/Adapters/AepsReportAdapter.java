package com.rechargeweb.rechargeweb.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.rechargeweb.rechargeweb.AepsReport;
import com.rechargeweb.rechargeweb.R;
import com.rechargeweb.rechargeweb.databinding.AepsReportItemBinding;

import java.util.ArrayList;
import java.util.List;

public class AepsReportAdapter extends RecyclerView.Adapter<AepsReportAdapter.AepsViewHolder> {

    private Context context;
    private List<AepsReport>aepsReportList = new ArrayList<>();

    public AepsReportAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public AepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AepsReportItemBinding aepsReportItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.aeps_report_item,parent,false);
        return new AepsViewHolder(aepsReportItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AepsViewHolder holder, int position) {

        if (aepsReportList != null){

            AepsReport aepsReport = aepsReportList.get(position);
            holder.reportItemBinding.setAeps(aepsReport);
        }
    }

    @Override
    public int getItemCount() {
        return aepsReportList.size();
    }

    public void setAepsReportList(List<AepsReport> aepsReportList) {
        this.aepsReportList = aepsReportList;
        notifyDataSetChanged();
    }

    class AepsViewHolder extends RecyclerView.ViewHolder{

        AepsReportItemBinding reportItemBinding;
        public AepsViewHolder(@NonNull AepsReportItemBinding reportItemBinding) {
            super(reportItemBinding.getRoot());

            this.reportItemBinding = reportItemBinding;
        }
    }
}
