package com.rechargeweb.rechargeweb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StateListAdapter extends RecyclerView.Adapter<StateListAdapter.StateListViewHolder> {

    private List<String>stateList;
    private LayoutInflater inflater;
    OnStateClickListener stateClickListener;

    public StateListAdapter(Context context, List<String> stateList,OnStateClickListener stateClickListener) {

        inflater = LayoutInflater.from(context);
        this.stateList = stateList;
        this.stateClickListener = stateClickListener;
    }

    public interface OnStateClickListener{
        void onStateListClick(int position);
    }
    @NonNull
    @Override
    public StateListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StateListViewHolder(inflater.inflate(R.layout.state_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull StateListViewHolder holder, int position) {

        if (stateList != null){
            holder.textView.setText(stateList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return stateList.size();
    }

    public String getStateList(int position) {
        return stateList.get(position);
    }

    public class StateListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textView;

        public StateListViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.state_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            stateClickListener.onStateListClick(position);
        }
    }
}
