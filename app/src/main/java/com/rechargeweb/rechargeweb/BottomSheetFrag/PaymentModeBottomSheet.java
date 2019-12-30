package com.rechargeweb.rechargeweb.BottomSheetFrag;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.rechargeweb.rechargeweb.Adapters.PaymentModeAdapter;
import com.rechargeweb.rechargeweb.Constant.DummyData;
import com.rechargeweb.rechargeweb.R;

public class PaymentModeBottomSheet extends BottomSheetDialogFragment implements PaymentModeAdapter.OnPaymentItemClickcListener {

    private RecyclerView bottomSheetRecycler;
    private PaymentModeAdapter paymentModeAdapter;
    private OnPaymentClickListener clickListener;

    private BottomSheetBehavior bottomSheetBehavior;

    public interface OnPaymentClickListener {

        void onPaymentClick(String string);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        BottomSheetDialog dialog = (BottomSheetDialog)super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(),R.layout.fragment_bottom_sheet_dialog,null);

        ConstraintLayout rootLayout = view.findViewById(R.id.bank_root);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)rootLayout.getLayoutParams();
        layoutParams.height = getScreenHeight();
        rootLayout.setLayoutParams(layoutParams);

        bottomSheetRecycler = view.findViewById(R.id.operators_recyclerView);
        bottomSheetRecycler.setHasFixedSize(true);
        bottomSheetRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        paymentModeAdapter = new PaymentModeAdapter(getContext(), PaymentModeBottomSheet.this, new DummyData().getPaymentMode());
        bottomSheetRecycler.setAdapter(paymentModeAdapter);

        ImageView closeImage = view.findViewById(R.id.close_bottom_sheet_iv);
        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        dialog.setContentView(view);
        bottomSheetBehavior = BottomSheetBehavior.from((View)view.getParent());
        return dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        clickListener = (OnPaymentClickListener) getActivity();
    }

    @Override
    public void onStart() {
        super.onStart();

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onPaymentClick(int position) {

        String payemtMode = paymentModeAdapter.getPaymentMode(position);
        clickListener.onPaymentClick(payemtMode);
        dismiss();
    }

    //Getting screen height
    private static int getScreenHeight(){
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}
