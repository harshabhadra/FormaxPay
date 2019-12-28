package com.rechargeweb.rechargeweb;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.rechargeweb.rechargeweb.Constant.DummyData;


/**
 * A simple {@link Fragment} subclass.
 */
public class StateListFragment extends BottomSheetDialogFragment implements StateListAdapter.OnStateClickListener{


    private RecyclerView stateListRecycler;
    private StateListAdapter stateListAdapter;
    private OnStateItemClickListener stateItemClickListener;

    public StateListFragment() {
        // Required empty public constructor
    }

    public interface OnStateItemClickListener{
        void onStateItemClick(String state);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_state_list, container, false);

        //Initialzing RecyclerView
        stateListRecycler = view.findViewById(R.id.state_list_recycler);
        stateListRecycler.setHasFixedSize(true);

        //Initializing Layout manager for recyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        //Initializing Dummy Data
        DummyData dummyData = new DummyData();

        stateListRecycler.setLayoutManager(linearLayoutManager);

        //Attach recyclerView with the adapter
        stateListAdapter = new StateListAdapter(getContext(),dummyData.getStateList(),StateListFragment.this);
        stateListRecycler.setAdapter(stateListAdapter);

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        stateItemClickListener = (OnStateItemClickListener)context;
    }

    @Override
    public void onStateListClick(int position) {

        String string = stateListAdapter.getStateList(position);
        stateItemClickListener.onStateItemClick(string);
        dismiss();
    }
}
