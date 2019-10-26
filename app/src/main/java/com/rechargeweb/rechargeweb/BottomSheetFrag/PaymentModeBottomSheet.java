package com.rechargeweb.rechargeweb.BottomSheetFrag;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.rechargeweb.rechargeweb.Adapters.PaymentModeAdapter;
import com.rechargeweb.rechargeweb.Constant.DummyData;
import com.rechargeweb.rechargeweb.R;

public class PaymentModeBottomSheet extends BottomSheetDialogFragment implements PaymentModeAdapter.OnPaymentItemClickcListener {

    RecyclerView bottomSheetRecycler;
    PaymentModeAdapter paymentModeAdapter;
    OnPaymentClickListener clickListener;

    public interface OnPaymentClickListener{

        void onPaymentClick(String string);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_sheet_dialog, container, false);

        bottomSheetRecycler = view.findViewById(R.id.operators_recyclerView);
        bottomSheetRecycler.setHasFixedSize(true);
        bottomSheetRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        paymentModeAdapter = new PaymentModeAdapter(getContext(),PaymentModeBottomSheet.this, new DummyData().getPaymentMode());
        bottomSheetRecycler.setAdapter(paymentModeAdapter);

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        clickListener = (OnPaymentClickListener)getActivity();
    }

    @Override
    public void onPaymentClick(int position) {

        String payemtMode = paymentModeAdapter.getPaymentMode(position);
        clickListener.onPaymentClick(payemtMode);
        dismiss();
    }
}
