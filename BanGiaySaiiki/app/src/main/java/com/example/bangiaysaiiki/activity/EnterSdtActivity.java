package com.example.bangiaysaiiki.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bangiaysaiiki.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class EnterSdtActivity extends AppCompatActivity {

    public static final String TAG = EnterSdtActivity.class.getName();
    private EditText et_sdt;
    private TextView tv_ma;
    private Button bt_sendOtp;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_sdt);

        unitUi();

        bt_sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(et_sdt.getText().toString().trim()==null ||
                        et_sdt.getText().toString().trim().length() == 0){
                    Toast.makeText(EnterSdtActivity.this, "vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
                }
                else {
                    String strSdt = tv_ma.getText().toString() + et_sdt.getText().toString().trim();
                    onCreateVerifyPhoneNumber(strSdt);
                }
            }
        });

    }

    public void unitUi(){
        et_sdt = findViewById(R.id.et_sdt);
        tv_ma = findViewById(R.id.tv_ma);
        bt_sendOtp = findViewById(R.id.bt_sendOtp);
        mAuth = FirebaseAuth.getInstance();
    }

    private void onCreateVerifyPhoneNumber(String strSdt) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(strSdt)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(EnterSdtActivity.this,"Verification failed", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(verificationId, forceResendingToken);
                                goToEnterOtp(strSdt, verificationId);
                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
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
                            goToSetInfo(user.getPhoneNumber());
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(EnterSdtActivity.this,"The verification code entered was invalid", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void goToSetInfo(String phoneNumber) {
        Intent i = new Intent(this, SignupActivity.class);
        i.putExtra("phone_number", phoneNumber);
        startActivity(i);
    }

    private void goToEnterOtp(String strSdt, String verificationId) {
        Intent i = new Intent(this, SignupActivity.class);
        i.putExtra("phone_number", strSdt);
        i.putExtra("verification_id", verificationId);
        startActivity(i);
    }

}