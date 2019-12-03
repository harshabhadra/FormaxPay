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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.rechargeweb.rechargeweb.Activity.HomeActivity;
import com.rechargeweb.rechargeweb.Adapters.BankingAdapter;
import com.rechargeweb.rechargeweb.Adapters.ItemAdapter;
import com.rechargeweb.rechargeweb.Constant.DummyData;
import com.rechargeweb.rechargeweb.Gist.StatefulRecyclerView;
import com.rechargeweb.rechargeweb.Gist.Utility;
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
public class HomeFragment extends Fragment implements ItemAdapter.OnItemclickListener, BankingAdapter.OnBankingItemClickListener {


    private StatefulRecyclerView itemRecyclerView;
    private ItemAdapter itemAdapter;

    ApiService apiService;
    String id;
    String authKey;

    private StatefulRecyclerView bankingRecyclerView;
    private BankingAdapter bankingAdapter;

    OnHomeItemClickLisetener homeItemClickLisetener;

    private ProgressBar balanceLoading;

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


        HomeActivity activity = (HomeActivity) getActivity();
        if (activity != null) {
            id = activity.getSession_id();
        }
        apiService = ApiUtills.getApiService();

        DummyData dummyData = new DummyData();
        //Initializing progressbar
        balanceLoading = view.findViewById(R.id.balance_loading);

        //Setting up Slider View
        SliderView sliderView = view.findViewById(R.id.imageSlider);
        SliderAdapter sliderAdapter = new SliderAdapter(getContext());
        sliderView.setSliderAdapter(sliderAdapter);
        //Initializing RecyclerView
        itemRecyclerView = view.findViewById(R.id.item_recycler);
        itemRecyclerView.setHasFixedSize(true);
        int noOfc = Utility.calculateNoOfColumns(getContext(), 110);
        itemRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), noOfc));
        itemAdapter = new ItemAdapter(getContext(), HomeFragment.this, dummyData.getItemsList());
        itemRecyclerView.setAdapter(itemAdapter);

        //Initializing banking recyclerView
        bankingRecyclerView = view.findViewById(R.id.banking_recycler);
        bankingRecyclerView.setHasFixedSize(true);
        bankingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        bankingAdapter = new BankingAdapter(getContext(), dummyData.getBankingItems(), HomeFragment.this);
        bankingRecyclerView.setAdapter(bankingAdapter);

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

        Animation animation2 = AnimationUtils.loadAnimation(getContext(), R.anim.swing_up_left);
        bankingRecyclerView.setAnimation(animation2);
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
                        balanceLoading.setVisibility(View.GONE);
                        createBalanceDialog(details);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "error");
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        balanceLoading.setVisibility(View.GONE);
                        isLoading = false;
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void createBalanceDialog(Details details) {

        Log.e(TAG, "Balance dialog creating");
        View layout = getLayoutInflater().inflate(R.layout.wallet_layout, null);

        TextView businessName = layout.findViewById(R.id.business_name);
        TextView walletone = layout.findViewById(R.id.wallet1_tv);
        TextView wallettwo = layout.findViewById(R.id.wallet2_tv);
        FancyButton button = layout.findViewById(R.id.close);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.walletDialog);
        builder.setView(layout);

        final AlertDialog dialog = builder.create();
        dialog.show();

        businessName.setText(details.getBusiness_name());
        walletone.setText(details.getWallet_1());
        wallettwo.setText(details.getWallet_2());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                isLoading = false;
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

    @Override
    public void onBankItemclick(int position) {

        if (!isLoading) {
            Items items = bankingAdapter.getBankingItem(position);
            homeItemClickLisetener.onHomeItemclick(items);
        }
    }
}
