package com.example.bangiaysaiiki.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bangiaysaiiki.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class EnterOtpActivity extends AppCompatActivity {

    private EditText otp;
    private Button sendOtp;
    private FirebaseAuth mAuth;
    private String mSdt, mVerificationId;
    private final String TAG = EnterOtpActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otp);

        otp = (EditText) findViewById(R.id.et_otp);
        sendOtp = (Button) findViewById(R.id.bt_xac_nhan);
        mAuth = FirebaseAuth.getInstance();
        getDataIntent();
        sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strOtp = otp.getText().toString();
                onClickGuiOTP(strOtp);
            }
        });
    }

    private void onClickGuiOTP(String otp) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
        signInWithPhoneAuthCredential(credential);
    }

    private void getDataIntent(){
        mSdt = getIntent().getStringExtra("sdt");
        mVerificationId = getIntent().getStringExtra("verificationId");
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                            goToSetInfo(user.getPhoneNumber().toString());
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(EnterOtpActivity.this,
                                        "The verification code entered was invalid",Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                });
    }
    private void goToMainActivity(String phoneNumber) {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("std", phoneNumber);
        startActivity(i);
    }

    private void goToSetInfo(String phoneNumber) {
        Intent i = new Intent(this, SignupActivity.class);
        i.putExtra("phone_number", phoneNumber);
        startActivity(i);
    }
}