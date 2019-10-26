package com.rechargeweb.rechargeweb.BottomSheetFrag;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    String state;
    private static final String TAG = OperatorByStateSheet.class.getSimpleName();

    public OperatorByStateSheet(String state) {
        this.state = state;
    }

    public interface OnOperatorByStateClickListener{

        void onBoardItemClick(Prepaid prepaid);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bottom_sheet_dialog, container, false);

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

        return view;
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
}
