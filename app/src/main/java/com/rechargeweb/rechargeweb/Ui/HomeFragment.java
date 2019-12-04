package com.rechargeweb.rechargeweb.Ui;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rechargeweb.rechargeweb.Activity.AddMoneyActivity;
import com.rechargeweb.rechargeweb.Activity.HomeActivity;
import com.rechargeweb.rechargeweb.Adapters.AllReportAdapter;
import com.rechargeweb.rechargeweb.Adapters.ItemAdapter;
import com.rechargeweb.rechargeweb.Constant.Constants;
import com.rechargeweb.rechargeweb.Constant.DummyData;
import com.rechargeweb.rechargeweb.Gist.StatefulRecyclerView;
import com.rechargeweb.rechargeweb.Model.Details;
import com.rechargeweb.rechargeweb.Model.Items;
import com.rechargeweb.rechargeweb.Network.ApiService;
import com.rechargeweb.rechargeweb.Network.ApiUtills;
import com.rechargeweb.rechargeweb.R;
import com.rechargeweb.rechargeweb.SliderAdapter;
import com.smarteist.autoimageslider.SliderView;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements ItemAdapter.OnItemclickListener, AllReportAdapter.OnReportItemClickListener {


    private static final String TAG = HomeFragment.class.getSimpleName();

    private StatefulRecyclerView itemRecyclerView;
    private RecyclerView reportRecyclerView;

    private ItemAdapter itemAdapter;
    private AllReportAdapter allReportAdapter;

    private TextView walletOneTv;
    private TextView walletTwoTv;
    private ImageView addMoneyImgOne, addMoneyImgTwo;

    private String  walletOne, walletTWo;
    private ApiService apiService;
    private String id;
    private String authKey;
    private boolean isLoading;

    OnHomeItemClickLisetener homeItemClickLisetener;
    OnReportItemClickListener reportItemClickListener;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        isLoading = false;
        authKey = getResources().getString(R.string.auth_key);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //Initializing wallet one and wallet two text view
        walletOneTv = view.findViewById(R.id.home_walllet_1_tv);
        walletTwoTv = view.findViewById(R.id.home_wallet_2_tv);

        //Initializing Add Money img one and two
        addMoneyImgOne = view.findViewById(R.id.add_money_img_1);
        addMoneyImgTwo = view.findViewById(R.id.add_money_img_2);


        HomeActivity activity = (HomeActivity) getActivity();
        if (activity != null) {
            id = activity.getSession_id();
        }
        apiService = ApiUtills.getApiService();

        DummyData dummyData = new DummyData();
        //Initializing progressbar

        //Setting up Slider View
        SliderView sliderView = view.findViewById(R.id.imageSlider);
        SliderAdapter sliderAdapter = new SliderAdapter(getContext());
        sliderView.setSliderAdapter(sliderAdapter);
        //Initializing RecyclerView
        itemRecyclerView = view.findViewById(R.id.item_recycler);
        itemRecyclerView.setHasFixedSize(true);
        itemRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        itemAdapter = new ItemAdapter(getContext(), HomeFragment.this, dummyData.getItemsList());
        itemRecyclerView.setAdapter(itemAdapter);

        //Setting Report Recycler
        reportRecyclerView = view.findViewById(R.id.home_report_recycler);
        reportRecyclerView.setHasFixedSize(true);
        reportRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        allReportAdapter = new AllReportAdapter(getContext(), HomeFragment.this, dummyData.getReportList());
        reportRecyclerView.setAdapter(allReportAdapter);

        addMoneyImgOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddMoneyActivity.class);
                intent.putExtra(Constants.SESSION_ID,id);
                startActivity(intent);
            }
        });

        addMoneyImgTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddMoneyActivity.class);
                intent.putExtra(Constants.SESSION_ID,id);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(TAG, "onActivityCreated");
    }

    @Override
    public void onResume() {
        super.onResume();
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.move_from_right);
        itemRecyclerView.startAnimation(animation);
        sendRequest(id, authKey);
    }

    private void sendRequest(String id, String key) {
        apiService.setDetailsPost(id, key).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Details>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Details details) {
                        Log.e(TAG, "success: " + details.toString());
                        walletOne = details.getWallet_1();
                        walletTWo = details.getWallet_2();
                        walletOneTv.setText(walletOne);
                        walletTwoTv.setText(walletTWo);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "error");
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                        isLoading = false;
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeItemClickLisetener = (OnHomeItemClickLisetener) context;
        reportItemClickListener = (OnReportItemClickListener)getActivity();
    }

    @Override
    public void onItemClick(int position) {

        if (!isLoading) {
            Items rItem = itemAdapter.getItem(position);
            homeItemClickLisetener.onHomeItemclick(rItem);
        }
    }

    @Override
    public void onReportItemClick(int position) {

        Items items = allReportAdapter.getReportItem(position);
        reportItemClickListener.onReportItemClick(items);
    }

    public interface OnReportItemClickListener{
        void onReportItemClick(Items items);
    }

    public interface OnHomeItemClickLisetener {
        void onHomeItemclick(Items items);
    }
}
