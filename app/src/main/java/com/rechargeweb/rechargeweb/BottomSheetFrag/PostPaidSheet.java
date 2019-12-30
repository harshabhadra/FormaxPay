package com.rechargeweb.rechargeweb.BottomSheetFrag;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.rechargeweb.rechargeweb.Adapters.BottomSheetAdapter;
import com.rechargeweb.rechargeweb.Model.Prepaid;
import com.rechargeweb.rechargeweb.R;
import com.rechargeweb.rechargeweb.ViewModels.MainViewModel;

import java.util.List;

public class PostPaidSheet extends BottomSheetDialogFragment implements BottomSheetAdapter.OnBottomSheetClickListener {

    private RecyclerView bottomSheetRecycler;
    private TextView textView;
    private BottomSheetAdapter bottomSheetAdapter;
    private MainViewModel mainViewModel;
    OnPostPaidClickListener paidClickListener;
    private BottomSheetBehavior bottomSheetBehavior;
    private static final String TAG = PostPaidSheet.class.getSimpleName();

    public interface OnPostPaidClickListener {
        void onPostPaidClick(Prepaid prepaid);
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
        textView = view.findViewById(R.id.unable_tv);

        bottomSheetAdapter = new BottomSheetAdapter(getContext(), PostPaidSheet.this);
        bottomSheetRecycler.setAdapter(bottomSheetAdapter);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        //Get list of Post paid operator
        mainViewModel.getOperators("PostPaid-Mobile").observe(this, new Observer<List<Prepaid>>() {
            @Override
            public void onChanged(List<Prepaid> prepaids) {
                if (prepaids != null) {
                    for (int i = 0; i < prepaids.size(); i++) {
                        Prepaid prepaid = prepaids.get(i);
                        if (prepaid.getId() == null) {
                            textView.setVisibility(View.VISIBLE);
                            textView.setText(prepaid.getImage());
                        } else {
                            textView.setVisibility(View.GONE);
                            Log.e(TAG, "List is Full");
                            bottomSheetAdapter.setItemsList(prepaids);
                        }
                    }
                } else {
                    Log.e(TAG, "List is empty");
                }
            }
        });

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
        paidClickListener = (OnPostPaidClickListener) context;
    }

    @Override
    public void onBottomSheetClick(int position) {

        Prepaid prepaid = bottomSheetAdapter.getOperator(position);
        paidClickListener.onPostPaidClick(prepaid);
        dismiss();
    }

    //Get Screen Height
    private static int getScreenHeight(){
       return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}

