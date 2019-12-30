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
import android.widget.Toast;

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
import com.rechargeweb.rechargeweb.ViewModels.MainViewModel;
import com.rechargeweb.rechargeweb.Model.Prepaid;
import com.rechargeweb.rechargeweb.R;

import java.util.List;

public class OperatorByStateSheet extends BottomSheetDialogFragment implements BottomSheetAdapter.OnBottomSheetClickListener {

    RecyclerView bottomSheetRecycler;
    TextView textView;
    BottomSheetAdapter bottomSheetAdapter;
    MainViewModel mainViewModel;
    OnOperatorByStateClickListener clickListener;

    private BottomSheetBehavior bottomSheetBehavior;

    String state;
    private static final String TAG = OperatorByStateSheet.class.getSimpleName();

    public OperatorByStateSheet(String state) {
        this.state = state;
    }

    public interface OnOperatorByStateClickListener{

        void onBoardItemClick(Prepaid prepaid);
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

        bottomSheetAdapter = new BottomSheetAdapter(getContext(), OperatorByStateSheet.this);
        bottomSheetRecycler.setAdapter(bottomSheetAdapter);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mainViewModel.getOperatorListByState("Electricity",state).observe(this, new Observer<List<Prepaid>>() {
            @Override
            public void onChanged(List<Prepaid> prepaids) {

                if (prepaids != null){
                    Log.e(TAG,"list is full");
                    for (int i=0; i<prepaids.size();i++){
                        Prepaid prepaid = prepaids.get(i);
                        if (prepaid.getId() == null){
                            textView.setVisibility(View.VISIBLE);
                            textView.setText(prepaid.getImage());
                            Toast.makeText(getContext(),prepaid.getImage(),Toast.LENGTH_LONG).show();
                        }else {
                            textView.setVisibility(View.GONE);
                            bottomSheetAdapter.setItemsList(prepaids);
                        }
                    }

                }else {
                    Log.e(TAG,"list is empty");
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
    public void onStart() {
        super.onStart();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        clickListener = (OnOperatorByStateClickListener)getActivity();
    }

    @Override
    public void onBottomSheetClick(int position) {

        Prepaid prepaid = bottomSheetAdapter.getOperator(position);
        clickListener.onBoardItemClick(prepaid);
        dismiss();
    }

    private static int getScreenHeight(){
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}
