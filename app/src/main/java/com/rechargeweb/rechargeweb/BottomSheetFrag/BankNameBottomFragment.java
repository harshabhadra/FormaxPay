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
import android.widget.LinearLayout;
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
import com.rechargeweb.rechargeweb.Adapters.BankAdapter;
import com.rechargeweb.rechargeweb.ViewModels.MainViewModel;
import com.rechargeweb.rechargeweb.Model.Bank;
import com.rechargeweb.rechargeweb.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BankNameBottomFragment extends BottomSheetDialogFragment implements BankAdapter.OnBankItemClickListener {

    RecyclerView bottomSheetRecycler;
    TextView textView;
    BankAdapter bankAdapter;
    MainViewModel mainViewModel;
    private OnClickListener clickListener;

    private BottomSheetBehavior bottomSheetBehavior;

    public interface OnClickListener {
        void onItemClick(Bank bank);
    }

    private static final String TAG = BankNameBottomFragment.class.getSimpleName();

    @NotNull
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
        clickListener = (OnClickListener)context;
    }

    @Override
    public void onBankItemClick(int position) {

        Bank bank = bankAdapter.getBank(position);
        clickListener.onItemClick(bank);
        dismiss();

    }

    @Override
    public void onStart() {
        super.onStart();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    //Get Screen Height
    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}
