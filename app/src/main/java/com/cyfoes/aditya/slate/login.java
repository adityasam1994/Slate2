package com.cyfoes.aditya.slate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken;
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;
import com.hbb20.CountryCodePicker.PhoneNumberValidityChangeListener;
import java.util.concurrent.TimeUnit;
import spencerstudios.com.bungeelib.Bungee;

public class login extends AppCompatActivity {
    ImageView btnback;
    CountryCodePicker ccp;
    DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Users");
    EditText etcode;
    EditText etphone;
    /* access modifiers changed from: private */
    public FirebaseAuth fauth;
    Boolean number_valid = Boolean.valueOf(false);

    /* renamed from: pd */
    ProgressDialog f59pd;
    /* access modifiers changed from: private */
    public String phoneVarificationId;
    Button resendcode;
    /* access modifiers changed from: private */
    public ForceResendingToken resendingToken;
    Button sendcode;
    SharedPreferences spref;
    private OnVerificationStateChangedCallbacks varificationCallbacks;
    Button varify;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_login);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle((CharSequence) "Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.spref = getSharedPreferences("userdetails", 0);
        this.f59pd = new ProgressDialog(this);
        this.sendcode = (Button) findViewById(R.id.btnsend);
        this.ccp = (CountryCodePicker) findViewById(R.id.code_picker);
        this.resendcode = (Button) findViewById(R.id.btnresend);
        this.varify = (Button) findViewById(R.id.btnvarify);
        this.etcode = (EditText) findViewById(R.id.etcode);
        this.etphone = (EditText) findViewById(R.id.etphone);
        this.ccp.registerCarrierNumberEditText(this.etphone);
        this.ccp.setPhoneNumberValidityChangeListener(new PhoneNumberValidityChangeListener() {
            public void onValidityChanged(boolean z) {
                if (login.this.etphone.getText().toString().length() <= 1) {
                    return;
                }
                if (z) {
                    login.this.number_valid = Boolean.valueOf(true);
                    return;
                }
                login.this.number_valid = Boolean.valueOf(false);
            }
        });
        this.fauth = FirebaseAuth.getInstance();
        this.resendcode.setEnabled(false);
        this.varify.setEnabled(false);
        if (this.fauth.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
        }
        this.sendcode.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (login.this.number_valid.booleanValue()) {
                    login.this.sendCode();
                } else {
                    Toast.makeText(login.this, "Invalid number", Toast.LENGTH_SHORT).show();
                }
            }
        });
        this.resendcode.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                login.this.resendToken();
            }
        });
        this.varify.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                login.this.varifyCode();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        finish();
        return true;
    }

    public void finish() {
        super.finish();
        Bungee.zoom(this);
    }

    /* access modifiers changed from: private */
    public void sendCode() {
        this.f59pd.setMessage("Sending Code...");
        this.f59pd.show();
        String fullNumberWithPlus = this.ccp.getFullNumberWithPlus();
        setupVarificationCallacks();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(fullNumberWithPlus, 60, TimeUnit.SECONDS, (Activity) this, this.varificationCallbacks);
    }

    private void setupVarificationCallacks() {
        this.varificationCallbacks = new OnVerificationStateChangedCallbacks() {
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                login.this.resendcode.setEnabled(false);
                login.this.varify.setEnabled(true);
                login.this.etcode.setText("");
                login.this.signInWithPhoneAuthCredentials(phoneAuthCredential);
            }

            public void onVerificationFailed(FirebaseException firebaseException) {
                if (firebaseException instanceof FirebaseAuthInvalidCredentialsException) {
                    login login = login.this;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Invalid Credentials");
                    sb.append(firebaseException);
                    Toast.makeText(login, sb.toString(), Toast.LENGTH_SHORT).show();
                } else if (firebaseException instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(login.this, "Message quota exausted", Toast.LENGTH_SHORT).show();
                }
            }

            public void onCodeSent(String str, ForceResendingToken forceResendingToken) {
                login.this.phoneVarificationId = str;
                login.this.resendingToken = forceResendingToken;
                login.this.varify.setEnabled(true);
                login.this.sendcode.setEnabled(false);
                login.this.resendcode.setEnabled(true);
                login.this.f59pd.dismiss();
            }
        };
    }

    public void varifyCode() {
        this.f59pd.setMessage("Logging you in...");
        this.f59pd.show();
        signInWithPhoneAuthCredentials(PhoneAuthProvider.getCredential(this.phoneVarificationId, this.etcode.getText().toString()));
    }

    /* access modifiers changed from: private */
    public void signInWithPhoneAuthCredentials(PhoneAuthCredential phoneAuthCredential) {
        this.fauth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    login.this.f59pd.dismiss();
                    login.this.etcode.setText("");
                    login.this.resendcode.setEnabled(false);
                    login.this.sendcode.setEnabled(false);
                    login.this.varify.setEnabled(false);
                    ((AuthResult) task.getResult()).getUser();
                    login.this.spref.edit().putString("username", "").commit();
                    login.this.spref.edit().putString(PhoneAuthProvider.PROVIDER_ID, login.this.fauth.getCurrentUser().getPhoneNumber().toString()).commit();
                    login.this.startActivity(new Intent(login.this, MainActivity.class));
                } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(login.this, "Invalid token", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void resendToken() {
        this.f59pd.setMessage("Resending Code...");
        this.f59pd.show();
        String fullNumberWithPlus = this.ccp.getFullNumberWithPlus();
        setupVarificationCallacks();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(fullNumberWithPlus, 60, TimeUnit.SECONDS, (Activity) this, this.varificationCallbacks);
    }

    public void onBackPressed() {
        super.onBackPressed();
        Bungee.zoom(this);
    }
}
