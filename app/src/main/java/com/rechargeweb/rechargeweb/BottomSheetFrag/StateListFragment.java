package com.rechargeweb.rechargeweb.BottomSheetFrag;


import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.rechargeweb.rechargeweb.Constant.DummyData;
import com.rechargeweb.rechargeweb.R;
import com.rechargeweb.rechargeweb.StateListAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class StateListFragment extends BottomSheetDialogFragment implements StateListAdapter.OnStateClickListener {


    private BottomSheetBehavior bottomSheetBehavior;

    private RecyclerView stateListRecycler;
    private StateListAdapter stateListAdapter;
    private OnStateItemClickListener stateItemClickListener;

    private EditText searchEditText;
    private List<String>stateList;
    private List<String>searchList;

    public StateListFragment() {
        // Required empty public constructor
    }

    public interface OnStateItemClickListener {
        void onStateItemClick(String state);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initializing Dummy Data
        DummyData dummyData = new DummyData();
        stateList = dummyData.getStateList();
        searchList = dummyData.getStateList();
        //Attach recyclerView with the adapter
        stateListAdapter = new StateListAdapter(getContext(), stateList, StateListFragment.this);
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        // Inflate the layout for this fragment
        View view = View.inflate(getContext(), R.layout.fragment_state_list, null);

        LinearLayout linearLayout = view.findViewById(R.id.state_root);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        params.height = getScreenHeight();
        linearLayout.setLayoutParams(params);

        //Initialzing RecyclerView
        stateListRecycler = view.findViewById(R.id.state_list_recycler);
        stateListRecycler.setHasFixedSize(true);

        //Initializing Layout manager for recyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        stateListRecycler.setAdapter(stateListAdapter);
        stateListRecycler.setLayoutManager(linearLayoutManager);

        //Initializing Search Edit Text
        searchEditText = view.findViewById(R.id.search_state_edit);
        searchEditText.addTextChangedListener(textWatcher);

        bottomSheetDialog.setContentView(view);
        bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        return bottomSheetDialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        stateItemClickListener = (OnStateItemClickListener) context;
    }

    @Override
    public void onStart() {
        super.onStart();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onStateListClick(int position) {

        String string = stateListAdapter.getStateList(position);
        stateItemClickListener.onStateItemClick(string);
        dismiss();
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
                search(s.toString().toLowerCase());
        }
    };

    //Search a state
    private void search(String searchTerm){
        stateList.clear();
        for (String state : searchList){
            if (state.toLowerCase().contains(searchTerm)){
                stateList.add(state);
            }
        }
        stateListAdapter.notifyDataSetChanged();
    }

    //Get Screen Height
    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}
