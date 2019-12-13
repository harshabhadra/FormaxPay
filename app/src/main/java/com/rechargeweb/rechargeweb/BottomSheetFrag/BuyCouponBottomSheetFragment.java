package com.rechargeweb.rechargeweb.BottomSheetFrag;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.rechargeweb.rechargeweb.Activity.PanActivity;
import com.rechargeweb.rechargeweb.ViewModels.MainViewModel;
import com.rechargeweb.rechargeweb.Model.Coupon;
import com.rechargeweb.rechargeweb.R;


public class BuyCouponBottomSheetFragment extends BottomSheetDialogFragment {

    TextView psaIdTv;
    EditText nameET;
    EditText quantityET;
    Button buyCouponButton;
    TextView couponPriceTv;
    ProgressBar progressBar;

    String psaId, name, price, quantity, totalPrice;

    Double qty, iPrice, iTotalPrice;

    MainViewModel mainViewModel;

    String session_id, auth;

    public BuyCouponBottomSheetFragment(String psaId, String name, String price) {
        this.psaId = psaId;
        this.name = name;
        this.price = price;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.buy_coupon_sheet, container, false);

        psaIdTv = view.findViewById(R.id.psa_id_bc);
        nameET = view.findViewById(R.id.name_bc);
        quantityET = view.findViewById(R.id.quantity_bc);
        buyCouponButton = view.findViewById(R.id.buy_coupon_submit);
        couponPriceTv = view.findViewById(R.id.coupon_price);
        progressBar = view.findViewById(R.id.buy_coupon_progress);

        psaIdTv.setText(psaId);
        nameET.setText(name);
        couponPriceTv.setText("Rs/- " + price);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        //Buy coupon button click
        buyCouponButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = nameET.getText().toString().trim();
                quantity = quantityET.getText().toString().trim();

                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                if (inputManager != null) {
                    inputManager.hideSoftInputFromWindow(getView().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }

                if (!name.isEmpty() && !quantity.isEmpty()) {
                    qty = Double.parseDouble(quantity);
                    iPrice = Double.parseDouble(price);

                    iTotalPrice = qty*iPrice;

                    totalPrice = String.valueOf(iTotalPrice);
                    setConfirmationDialog();
                }else if (quantity.isEmpty()){
                    Toast.makeText(getContext(),"Please Enter Amount",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(),"Please Enter Name",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    //Make confirmation dialog
    private void setConfirmationDialog(){

        View layout = getLayoutInflater().inflate(R.layout.coupon_confrimdialog_layout,null);

        TextView nameTv = layout.findViewById(R.id.cc_name);
        TextView quantityTv = layout.findViewById(R.id.cc_quantity);
        TextView priceTv = layout.findViewById(R.id.cc_total_price);
        Button button = layout.findViewById(R.id.coupon_confirm_button);
        TextView totalPriceTv = layout.findViewById(R.id.coupon_confirm_total_price);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.CustomDialog);
        builder.setView(layout);

        final AlertDialog dialog = builder.create();
        dialog.show();

        nameTv.setText(name);
        quantityTv.setText(quantity);
        priceTv.setText("RS/- " + price);
        totalPriceTv.setText("RS/- " + totalPrice);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!name.isEmpty() && !quantity.isEmpty()){
                    nameET.setEnabled(false);
                    quantityET.setEnabled(false);
                    dialog.dismiss();
                    buyCouponButton.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    mainViewModel.getCouponPurchaseDetails(auth,session_id,name,quantity).observe(BuyCouponBottomSheetFragment.this, new Observer<Coupon>() {
                        @Override
                        public void onChanged(Coupon coupon) {
                            nameET.setEnabled(true);
                            quantityET.setEnabled(true);
                            if (coupon != null){
                                progressBar.setVisibility(View.GONE);
                                buyCouponButton.setVisibility(View.VISIBLE);

                                if (coupon.getCreated_on() == null){
                                    Toast.makeText(getContext(),coupon.getMessage(),Toast.LENGTH_LONG).show();
                                    dismiss();
                                }else {
                                    Toast.makeText(getContext(), coupon.getMessage(), Toast.LENGTH_SHORT).show();
                                    createPurchaseDialog(coupon);
                                }

                            }
                        }
                    });
                }
            }
        });
    }

    //Create coupon buy dialog
    private void createPurchaseDialog(Coupon coupon){

        View layout = getLayoutInflater().inflate(R.layout.coupon_purchase_dialog, null);

        TextView responseTv = layout.findViewById(R.id.cc_message);
        TextView txnIdTv = layout.findViewById(R.id.cc_txn_id);
        TextView statusTv = layout.findViewById(R.id.cc_status);
        TextView priceTv = layout.findViewById(R.id.cc_done_price);
        TextView totalPriceTv = layout.findViewById(R.id.cc_done_total_price);
        TextView createdOnTv = layout.findViewById(R.id.cc_date);

        Button okButton = layout.findViewById(R.id.cc_ok_button);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.CustomDialog);
        builder.setView(layout);

        final AlertDialog dialog = builder.create();
        dialog.show();

        responseTv.setText(coupon.getMessage());
        txnIdTv.setText(coupon.getTxn_id());
        statusTv.setText(coupon.getStatus());
        priceTv.setText(coupon.getPrice());
        totalPriceTv.setText(coupon.getTotoal_price());
        createdOnTv.setText(coupon.getCreated_on());

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dismiss();
            }
        });
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        PanActivity panActivity = (PanActivity) getActivity();
        if (panActivity != null) {
            session_id = panActivity.getSession_id();
            auth = panActivity.getAuth();
        }

    }
}
