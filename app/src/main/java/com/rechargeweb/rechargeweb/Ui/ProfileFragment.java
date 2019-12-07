package com.rechargeweb.rechargeweb.Ui;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.rechargeweb.rechargeweb.Activity.HomeActivity;
import com.rechargeweb.rechargeweb.Model.Profile;
import com.rechargeweb.rechargeweb.Network.ApiService;
import com.rechargeweb.rechargeweb.Network.ApiUtills;
import com.rechargeweb.rechargeweb.R;

import org.reactivestreams.Subscriber;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private String id,user_id;
    private String auth;
    private static final String TAG = ProfileFragment.class.getSimpleName();

    private ApiService apiService;

    private boolean isConnected;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Log.e(TAG,"Profile Fragment");

        apiService = ApiUtills.getApiService();

        auth = getResources().getString(R.string.auth_key);
        HomeActivity activity = (HomeActivity) getActivity();
        id = activity.getSession_id();
        user_id = activity.getUser_id().toUpperCase().trim();

            getProfileDetails(id, auth);

        return view;
    }

    private void getProfileDetails(String id, String key) {

        apiService.getProfileDetails(id,key).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Profile>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Profile profile) {

                        if (profile != null) {

                        }else {

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Profile error: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "Profile Completed");
                    }
                });
    }
}
