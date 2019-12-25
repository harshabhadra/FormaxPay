package com.rechargeweb.rechargeweb.BottomSheetFrag;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.rechargeweb.rechargeweb.DthCustomerInfo;
import com.rechargeweb.rechargeweb.R;
import com.rechargeweb.rechargeweb.ViewModels.PlanViewModel;
import com.rechargeweb.rechargeweb.databinding.FragmentDthCustomerInfoBottomSheetBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class DthCustomerInfoBottomSheet extends BottomSheetDialogFragment {

    private static final String TAG = DthCustomerInfoBottomSheet.class.getSimpleName();
    private String mobileNumber;
    private String operatorName;

    private PlanViewModel planViewModel;


    FragmentDthCustomerInfoBottomSheetBinding customerInfoBottomSheetBinding;

    public DthCustomerInfoBottomSheet() {
        // Required empty public constructor
    }

    public DthCustomerInfoBottomSheet(String mobileNumber, String operatorName) {
        this.mobileNumber = mobileNumber;
        this.operatorName = operatorName;
        Log.e(TAG,"number: " + mobileNumber + ", Operator code: " + operatorName);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        customerInfoBottomSheetBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_dth_customer_info_bottom_sheet, container, false);

        //Initializing PlanViewModel Class
        planViewModel = ViewModelProviders.of(this).get(PlanViewModel.class);

        //Check if the mobile number and operator is not empty
        if (!mobileNumber.isEmpty() && !operatorName.isEmpty()){
            getDthCustomerInfo(mobileNumber,operatorName);
        }else {
            Log.e(TAG,"Empty Parameters");
        }

        return customerInfoBottomSheetBinding.getRoot();
    }

    //Get Dth customer info
    private void getDthCustomerInfo(final String mobileNumeber, String operatorName) {

        planViewModel.getDthCustomerInfo(mobileNumeber, operatorName).observe(this, new Observer<DthCustomerInfo>() {
            @Override
            public void onChanged(DthCustomerInfo dthCustomerInfo) {
                if (dthCustomerInfo != null) {
                    Log.e(TAG, "Dth customer info is not null");
                    if (dthCustomerInfo.getCustomerName() != null) {
                        customerInfoBottomSheetBinding.group.setVisibility(View.VISIBLE);
                        customerInfoBottomSheetBinding.dthBottomLoading.setVisibility(View.GONE);
                        customerInfoBottomSheetBinding.setDthcustomerinfo(dthCustomerInfo);
                    } else {
                        Toast.makeText(getContext(), dthCustomerInfo.getBalance(), Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to Get Details, try Again", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }
        });
    }
}
