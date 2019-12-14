package com.rechargeweb.rechargeweb.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rechargeweb.rechargeweb.R;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

public class AepsSliderAdapter extends SliderViewAdapter<AepsSliderAdapter.AepsViewHolder> {

    private Context context;

    public AepsSliderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public AepsViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflater = LayoutInflater.from(context).inflate(R.layout.slider_layout_item, null, false);
        return new AepsViewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(AepsViewHolder viewHolder, int position) {
        switch (position) {

            case 0:
            case 1:
                Picasso.get().load(R.drawable.aeps_banner).into(viewHolder.imageView);
                break;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    public class AepsViewHolder extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageView;

        public AepsViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.slider_image);
            this.itemView = itemView;
        }
    }
}
