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

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private LayoutInflater inflater;
    private List<Items> itemsList = new ArrayList<>();
    public OnItemclickListener listener;

    public interface OnItemclickListener {
        public void onItemClick(int position);
    }

    public ItemAdapter(Context context, OnItemclickListener listener, List<Items> itemsList) {

        inflater = LayoutInflater.from(context);
        this.listener = listener;
        this.itemsList = itemsList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(inflater.inflate(R.layout.pick_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        if (itemsList != null) {
            holder.textView.setText(itemsList.get(position).getName());
            Picasso.get().load(itemsList.get(position).getImageUrl()).placeholder(R.mipmap.formax_icon).into(holder.imageView);
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

    public Items getItem(int position) {

        return itemsList.get(position);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView textView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.item_image);
            textView = itemView.findViewById(R.id.item_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            listener.onItemClick(position);

        }
    }
}
