package com.rechargeweb.rechargeweb.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rechargeweb.rechargeweb.Model.Prepaid;
import com.rechargeweb.rechargeweb.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetAdapter extends RecyclerView.Adapter<BottomSheetAdapter.BottomViewHolder> {

    LayoutInflater inflater;
    List<Prepaid> itemsList = new ArrayList<>();
    OnBottomSheetClickListener clickListener;

    public interface OnBottomSheetClickListener {
        void onBottomSheetClick(int position);
    }

    public BottomSheetAdapter(Context context, OnBottomSheetClickListener clickListener) {

        this.clickListener = clickListener;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public BottomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BottomViewHolder(inflater.inflate(R.layout.bottom_sheet_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BottomViewHolder holder, int position) {

        if (itemsList != null) {

            if (!(itemsList.get(position).getImage().equals(""))) {
                Picasso.get().load(itemsList.get(position).getImage())
                        .placeholder(R.mipmap.formax_round_icon)
                        .error(R.mipmap.formax_icon)
                        .into(holder.imageView);
            }
            if (itemsList.get(position).getOperatorName().equals("")) {
                holder.textView.setText("No Operator Available");
            } else {
                holder.textView.setText(itemsList.get(position).getOperatorName());
            }
        }
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public Prepaid getOperator(int position) {

        Prepaid prepaid = itemsList.get(position);
        return prepaid;
    }

    public void setItemsList(List<Prepaid> itemsList) {
        if (itemsList != null) {
            this.itemsList = itemsList;
            notifyDataSetChanged();
        }
    }

    class BottomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView textView;

        public BottomViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.bottom_image);
            textView = itemView.findViewById(R.id.bottom_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            clickListener.onBottomSheetClick(position);
        }
    }
}
