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
import com.rechargeweb.rechargeweb.Adapters.BankAdapter;
import com.rechargeweb.rechargeweb.ViewModels.MainViewModel;
import com.rechargeweb.rechargeweb.Model.Bank;
import com.rechargeweb.rechargeweb.R;

import java.util.List;

public class BankNameBottomFragment extends BottomSheetDialogFragment implements BankAdapter.OnBankItemClickListener {

    RecyclerView bottomSheetRecycler;
    TextView textView;
    BankAdapter bankAdapter;
    MainViewModel mainViewModel;
    private OnClickListener clickListener;

    public interface OnClickListener {
        void onItemClick(Bank bank);
    }

    private static final String TAG = BankNameBottomFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bottom_sheet_dialog, container, false);

        bottomSheetRecycler = view.findViewById(R.id.operators_recyclerView);
        bottomSheetRecycler.setHasFixedSize(true);
        bottomSheetRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        textView = view.findViewById(R.id.unable_tv);

        bankAdapter = new BankAdapter(getContext(), BankNameBottomFragment.this);
        bottomSheetRecycler.setAdapter(bankAdapter);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getBankDetails().observe(this, new Observer<List<Bank>>() {
            @Override
            public void onChanged(List<Bank> banks) {

                if (banks != null) {
                    for (int i = 0;i<banks.size(); i++){
                        Bank bank = banks.get(i);
                        if (bank.getBankName() == null){
                            textView.setVisibility(View.VISIBLE);
                            textView.setText(bank.getAccountHolder());
                        }else {
                            Log.e(TAG, "List is full");
                            bankAdapter.setBankList(banks);
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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        clickListener = (OnClickListener)getActivity();
    }

    @Override
    public void onBankItemClick(int position) {

        Bank bank = bankAdapter.getBank(position);
        clickListener.onItemClick(bank);
        dismiss();

    }
}
