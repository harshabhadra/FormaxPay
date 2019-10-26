package com.rechargeweb.rechargeweb.BottomSheetFrag;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

public class BottomSheetFragment extends BottomSheetDialogFragment implements BottomSheetAdapter.OnBottomSheetClickListener {

    RecyclerView bottomSheetRecycler;
    TextView textView;
    BottomSheetAdapter bottomSheetAdapter;
    MainViewModel mainViewModel;
    OnPrepaidListClickLietener clickLietener;

    public interface OnPrepaidListClickLietener {
        void onPrepaidClick(Prepaid prepaid);
    }

    private static final String TAG = BottomSheetFragment.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        clickLietener = (OnPrepaidListClickLietener) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bottom_sheet_dialog, container, false);

        bottomSheetRecycler = view.findViewById(R.id.operators_recyclerView);
        bottomSheetRecycler.setHasFixedSize(true);
        bottomSheetRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        textView = view.findViewById(R.id.unable_tv);

        bottomSheetAdapter = new BottomSheetAdapter(getContext(), BottomSheetFragment.this);
        bottomSheetRecycler.setAdapter(bottomSheetAdapter);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        //Get list of Prepaid operator
        mainViewModel.getOperators("PrePaid-Mobile").observe(this, new Observer<List<Prepaid>>() {
            @Override
            public void onChanged(List<Prepaid> prepaids) {
                if (prepaids != null) {
                    for (int i =0; i<prepaids.size(); i++){
                        Prepaid prepaid = prepaids.get(i);
                        if (prepaid.getId() == null){
                            textView.setVisibility(View.VISIBLE);
                            textView.setText(prepaid.getImage());
                        }else {
                            Log.e(TAG, "List is Full");
                            textView.setVisibility(View.GONE);
                            bottomSheetAdapter.setItemsList(prepaids);
                        }
                    }

                } else {
                    Log.e(TAG, "List is empty");
                }
            }
        });
        return view;
    }

    @Override
    public void onBottomSheetClick(int position) {

        Prepaid prepaid = bottomSheetAdapter.getOperator(position);
        clickLietener.onPrepaidClick(prepaid);
        dismiss();
    }
}
