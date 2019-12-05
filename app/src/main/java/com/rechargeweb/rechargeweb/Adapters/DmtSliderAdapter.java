package com.rechargeweb.rechargeweb.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.rechargeweb.rechargeweb.R;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

public class DmtSliderAdapter extends SliderViewAdapter<DmtSliderAdapter.DmtSliderViewHolder> {

    private Context context;

    public DmtSliderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public DmtSliderViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflater = LayoutInflater.from(context).inflate(R.layout.slider_layout_item, null, false);
        return new DmtSliderViewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(DmtSliderViewHolder viewHolder, int position) {

        switch (position){
            case 0:
                Picasso.get().load(R.drawable.domestic_money_transfer).into(viewHolder.imageBackground);
                break;
            case 1:
                Picasso.get().load(R.drawable.dmt_money_transfer2).into(viewHolder.imageBackground);
                break;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    public class DmtSliderViewHolder extends SliderViewAdapter.ViewHolder{

        View itemView;
        ImageView imageBackground;

        public DmtSliderViewHolder(View itemView) {
            super(itemView);

            imageBackground = itemView.findViewById(R.id.slider_image);
            this.itemView = itemView;
        }
    }
}
