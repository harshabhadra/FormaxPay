package com.rechargeweb.rechargeweb.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rechargeweb.rechargeweb.Constant.Constants;
import com.rechargeweb.rechargeweb.Model.Password;
import com.rechargeweb.rechargeweb.Network.ApiService;
import com.rechargeweb.rechargeweb.Network.ApiUtills;
import com.rechargeweb.rechargeweb.R;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ChangePassActivity extends AppCompatActivity {

    private static final String TAG = ChangePassActivity.class.getSimpleName();

    EditText cureentPass, newPass, confirmNewPass;
    Button changePasswordButton;

    String pass, newP, conNewP;

    String session_id,auth;

    ApiService apiService;

    private AlertDialog alertDialog;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

         auth = getResources().getString(R.string.auth_key);
        //Initializing Api services
        apiService = ApiUtills.getApiService();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradientbakcthree));
        //Getting Intent values
        Intent intent = getIntent();
        if (intent.hasExtra(Constants.SESSION_ID)){
            //Getting Session id
            setTitle("Change Password");
            session_id = intent.getStringExtra(Constants.SESSION_ID);
        }

        cureentPass = findViewById(R.id.current_password);
        newPass = findViewById(R.id.new_password);
        confirmNewPass = findViewById(R.id.confirm_new_password);

        cureentPass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
            }
        });

        newPass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        confirmNewPass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        changePasswordButton = findViewById(R.id.change_password_button);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.move_from_left);
        changePasswordButton.startAnimation(animation);

        //Change password button click
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pass = cureentPass.getText().toString().trim();
                newP = newPass.getText().toString().trim();
                conNewP = confirmNewPass.getText().toString().trim();

                if (!pass.isEmpty() && !newP.isEmpty() && !conNewP.isEmpty() ){

                    if (newP.equals(conNewP)){

                        //Create confirmation dialog
                        createConfirmDialog(pass,newP);
                    }else {
                        Toast.makeText(getApplicationContext(),"Password MisMatch",Toast.LENGTH_SHORT).show();
                    }
                }else if (pass.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please Enter Current Password",Toast.LENGTH_SHORT).show();
                }else if (newP.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please Enter New Password",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(),"Please Confirm New Password",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Method to create confirmation dialog
    private void createConfirmDialog(final String passw, final String nPassw){

        View layout = getLayoutInflater().inflate(R.layout.passchangeconfirm_layout,null);

        TextView cPass = layout.findViewById(R.id.dialog_current_pass);
        TextView nPass = layout.findViewById(R.id.dialog_new_password);
        Button confirmButton = layout.findViewById(R.id.dialog_change_pass_button);
        Button cancelButoon = layout.findViewById(R.id.dialog_cancel_pass_button);

        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.CustomDialog);
        builder.setView(layout);
        final AlertDialog dialog = builder.create();

        dialog.show();

        cPass.setText(passw);
        nPass.setText(nPassw);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Log.e(TAG,"Auth is : " + auth);
                alertDialog = createAlertDialog(ChangePassActivity.this);
                alertDialog.show();
                changeCurrentPassword(session_id,auth,passw,nPassw);
            }
        });

        cancelButoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePassActivity.this,ChangePassActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                intent.putExtra(Constants.SESSION_ID,session_id);
                startActivity(intent);
                finish();
            }
        });

    }

    //Change Password Network request
    private void changeCurrentPassword(String session_id,String auth, String cPass,String nPass){

        apiService.changePassword(session_id,auth,cPass,nPass,nPass).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Password>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Password password) {
                        alertDialog.dismiss();
                        if (password != null) {
                            Log.e(TAG, "Password Change Success: " + password.getMessage());
                            if (password.getMessage().equals("Success")) {
                                Toast.makeText(getApplicationContext(), "Password Reset Successful", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }else {
                            Toast.makeText(getApplicationContext(),"Password reset Failed", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        alertDialog.dismiss();
                        Log.e(TAG,"Password Change error: " + e.getMessage());
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //Create Loading Dialog
    private AlertDialog createAlertDialog(Context context){
        View layout = getLayoutInflater().inflate(R.layout.loading_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setView(layout);
        return builder.create();
    }
}
