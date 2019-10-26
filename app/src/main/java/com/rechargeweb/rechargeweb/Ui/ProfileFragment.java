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

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private String id,user_id;
    private String auth;
    private static final String TAG = ProfileFragment.class.getSimpleName();

    TextView businessNameTv,profileIdTv;
    ConstraintLayout profileLayout, passwordLayout;

    private CardView profileCard;
    private CardView passwordCard;

    private ApiService apiService;

    private boolean isConnected;

    Profile mProfile;

    public OnProfileItemClick profileItemClick;
    OnPassChangeLayoutClick passChangeLayoutClick;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public interface OnProfileItemClick{
        void onProfileItemClick(Profile profile);
    }

    public interface OnPassChangeLayoutClick{
        void onChangePassClick();
    }


    @Override
    public void onResume() {
        super.onResume();

        Animation leftAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.move_from_left);
        Animation rightAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.move_from_right);
        profileLayout.startAnimation(leftAnimation);
        passwordLayout.startAnimation(rightAnimation);
        passwordCard.startAnimation(rightAnimation);
        profileCard.startAnimation(leftAnimation);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Log.e(TAG,"Profile Fragment");
        businessNameTv = view.findViewById(R.id.business_name);
        profileIdTv = view.findViewById(R.id.profile_id);

        profileCard = view.findViewById(R.id.profile_layout_card);
        passwordCard = view.findViewById(R.id.password_layout_card);

        profileLayout = view.findViewById(R.id.profile_layout);
        passwordLayout = view.findViewById(R.id.change_pass_layout);
        apiService = ApiUtills.getApiService();

        auth = getResources().getString(R.string.auth_key);
        HomeActivity activity = (HomeActivity) getActivity();
        id = activity.getSession_id();
        user_id = activity.getUser_id().toUpperCase().trim();

            getProfileDetails(id, auth);

        return view;
    }

    private void getProfileDetails(String id, String key) {

        apiService.getProfileDetails(id, key).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Profile>() {
                    @Override
                    public void onCompleted() {

                        Log.e(TAG, "Profile Completed");
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e(TAG, "Profile error: " + e.getMessage());
                    }

                    @Override
                    public void onNext(Profile profile) {

                        mProfile = profile;
                        if (profile != null) {
                            businessNameTv.setText(mProfile.getBusinessName());
                            profileIdTv.setText(user_id);

                        }else {
                            Log.e(TAG,"Proifle is empty");
                        }
                        profileLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                profileItemClick.onProfileItemClick(mProfile);
                            }
                        });
                        passwordLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                passChangeLayoutClick.onChangePassClick();
                            }
                        });
                    }
                });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        profileItemClick = (OnProfileItemClick)getActivity();
        passChangeLayoutClick = (OnPassChangeLayoutClick)getActivity();
    }
}
