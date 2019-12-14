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

import java.util.List;

public class AddMoneyTermsAdapters extends RecyclerView.Adapter<AddMoneyTermsAdapters.AddMoneyViewHolder> {

    private Context context;
    List<Items> termsList;

    public AddMoneyTermsAdapters(Context context, List<Items> termsList) {
        this.context = context;
        this.termsList = termsList;
    }

    @NonNull
    @Override
    public AddMoneyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddMoneyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.money_terms_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddMoneyViewHolder holder, int position) {

        holder.termsImage.setImageResource(termsList.get(position).getImageUrl());
        holder.termsText.setText(termsList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return termsList.size();
    }

    public class AddMoneyViewHolder extends RecyclerView.ViewHolder {

        ImageView termsImage;
        TextView termsText;

        public AddMoneyViewHolder(@NonNull View itemView) {
            super(itemView);

            termsImage = itemView.findViewById(R.id.add_money_terms_image);
            termsText = itemView.findViewById(R.id.add_money_terms_tv);
        }
    }
}
