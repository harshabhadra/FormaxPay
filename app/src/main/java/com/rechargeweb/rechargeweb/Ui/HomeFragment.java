package com.rechargeweb.rechargeweb.Ui;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.rechargeweb.rechargeweb.Activity.HomeActivity;
import com.rechargeweb.rechargeweb.Adapters.ItemAdapter;
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
import mehdi.sakout.fancybuttons.FancyButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements ItemAdapter.OnItemclickListener {


    private StatefulRecyclerView itemRecyclerView;
    private ItemAdapter itemAdapter;
    private TextView retailerNameTv;
    private TextView retailerPhoneTV;
    private TextView walletOneTv;
    private TextView walletTwoTv;
    private String retailerName, retailerPhone, walletOne, walletTWo;

    private ApiService apiService;
    private String id;
    private String authKey;


    OnHomeItemClickLisetener homeItemClickLisetener;

    private boolean isLoading;

    private static final String TAG = HomeFragment.class.getSimpleName();

    public interface OnHomeItemClickLisetener {
        void onHomeItemclick(Items items);
    }

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

        //Initializing Retailer name, retailer phone, wallet one and wallet two text view
        retailerNameTv = view.findViewById(R.id.home_retailer_name);
        retailerPhoneTV = view.findViewById(R.id.home_retailer_phone);
        walletOneTv = view.findViewById(R.id.home_walllet_1_tv);
        walletTwoTv = view.findViewById(R.id.home_wallet_2_tv);

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
        sendRequest(id,authKey);
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
                        retailerName = details.getBusiness_name();
                        walletOne = details.getWallet_1();
                        walletTWo = details.getWallet_2();
                        retailerNameTv.setText(retailerName);
                        walletOneTv.setText(walletOne);
                        walletTwoTv.setText(walletTWo);
                        retailerPhoneTV.setText(details.getUser_type());
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
    }

    @Override
    public void onItemClick(int position) {

        if (!isLoading) {
            Items rItem = itemAdapter.getItem(position);
            homeItemClickLisetener.onHomeItemclick(rItem);
        }
    }
}
