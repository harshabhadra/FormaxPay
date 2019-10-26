package com.rechargeweb.rechargeweb.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rechargeweb.rechargeweb.Model.Items;
import com.rechargeweb.rechargeweb.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AllReportAdapter extends RecyclerView.Adapter<AllReportAdapter.AllReportViewHolder> {

    LayoutInflater inflater;
    List<Items> itemsList;
    OnReportItemClickListener clickListener;

    public interface OnReportItemClickListener {
        void onReportItemClick(int position);
    }

    public AllReportAdapter(Context context, OnReportItemClickListener clickListener, List<Items> itemsList) {
        inflater = LayoutInflater.from(context);
        this.clickListener = clickListener;
        this.itemsList = itemsList;
    }

    @NonNull
    @Override
    public AllReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AllReportViewHolder(inflater.inflate(R.layout.report_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AllReportViewHolder holder, int position) {

        if (itemsList != null) {
            Picasso.get().load(itemsList.get(position).getImageUrl()).into(holder.imageView);
            holder.textView.setText(itemsList.get(position).getName());
        }
    }

    @Override
    public int getItemCount() {
        if (itemsList != null) {
            return itemsList.size();
        } else {
            return 0;
        }
    }

    public void setItemsList(List<Items> itemsList) {
        this.itemsList = itemsList;
    }

    public Items getReportItem(int position) {
        return itemsList.get(position);
    }

    class AllReportViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView textView;

        public AllReportViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.report_item_image);
            textView = itemView.findViewById(R.id.report_item_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            clickListener.onReportItemClick(position);
        }
    }
}
