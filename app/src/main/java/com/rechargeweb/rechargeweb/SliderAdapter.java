package com.rechargeweb.rechargeweb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderViewHolder> {

    private static final String TAG = SliderAdapter.class.getSimpleName();
    private Context context;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public SliderViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflater = LayoutInflater.from(context).inflate(R.layout.slider_layout_item, null, false);
        return new SliderViewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(SliderViewHolder viewHolder, int position) {

        switch (position){
            case 0:
            case 1:
                Picasso.get().load(R.drawable.add_money2).into(viewHolder.imageViewBackground);
                break;
            case 2:
            case 3:
                Glide.with(viewHolder.itemView)
                        .load(R.drawable.add_money3)
                        .into(viewHolder.imageViewBackground);
                break;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    public class SliderViewHolder extends SliderViewAdapter.ViewHolder{

        View itemView;
        ImageView imageViewBackground;

        public SliderViewHolder(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.slider_image);
            this.itemView = itemView;
        }
    }
}
