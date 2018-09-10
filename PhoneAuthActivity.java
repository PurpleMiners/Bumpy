package clover.hamar_bumpy;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;



public class PhoneAuthActivity extends AppCompatActivity implements        View.OnClickListener {

    EditText mPhoneNumberField, mVerificationField;
    Button mStartButton, mVerifyButton, mResendButton;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String mVerificationId;
    static AnimationDrawable animationDrawable;
    Button next;
    EditText FirstName;
    EditText LastName;
    EditText pass;
    EditText confirmPass;
    TextView already;
    EditText email;
    User user;
    private static final String TAG = "PhoneAuthActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);
        mAuth = FirebaseAuth.getInstance();
        next=(Button)findViewById(R.id.regButton);
        FirstName=(EditText)findViewById(R.id.FirstName);
        LastName=(EditText)findViewById(R.id.LastName);
        pass=(EditText)findViewById(R.id.pass);
        confirmPass=(EditText)findViewById(R.id.pass);
        already=(TextView)findViewById(R.id.already);
        email=(EditText)findViewById(R.id.email);
        mPhoneNumberField = (EditText) findViewById(R.id.field_phone_number);
        mVerificationField = (EditText) findViewById(R.id.field_verification_code);
        mStartButton = (Button) findViewById(R.id.button_start_verification);
        mVerifyButton = (Button) findViewById(R.id.button_verify_phone);
        mResendButton = (Button) findViewById(R.id.button_resend);
        mVerifyButton.setVisibility(View.INVISIBLE);
        mResendButton.setVisibility(View.INVISIBLE);
        mVerificationField.setVisibility(View.INVISIBLE);
        mStartButton.setOnClickListener(this);
        mVerifyButton.setOnClickListener(this);
        mResendButton.setOnClickListener(this);
        ConstraintLayout constraintLayout=findViewById(R.id.phoneAct);
        animationDrawable = (AnimationDrawable)constraintLayout.getBackground();
        animationDrawable.setExitFadeDuration(1000);
        animationDrawable.setExitFadeDuration(1000);
        animationDrawable.start();
        already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PhoneAuthActivity.this,MainActivity.class));
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean ReturenedValue = validate();
                if (!ReturenedValue) {
                    final String uPass = pass.getText().toString().trim();
                    final String uEmail = email.getText().toString().trim();
//                    mAuth.createUserWithEmailAndPassword(uEmail,uPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if(task.isSuccessful()){
                    Toast.makeText(PhoneAuthActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    user = new User();
                    user.pwd = uPass;
                    user.uemail = uEmail;
                    user.mno = ((EditText) findViewById(R.id.field_phone_number)).getText().toString();
                    user.lname = ((EditText) findViewById(R.id.LastName)).getText().toString();
                    user.fname = ((EditText) findViewById(R.id.FirstName)).getText().toString();
                    MainActivity.myRef.child("users").child(user.uemail).setValue(user);

                }
//                            else{
//                                Toast.makeText(PhoneAuthActivity.this,"Registration Failed",Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });

                //}
                Intent i = new Intent(PhoneAuthActivity.this, MainActivity.class);
                startActivity(i);
            }
        });


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);
                signInWithPhoneAuthCredential(credential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    mPhoneNumberField.setError("Invalid phone number mario");
                } else if (e instanceof FirebaseTooManyRequestsException) {

                }
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            Toast.makeText(getApplicationContext(),"Verified",Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(PhoneAuthActivity.this, MainActivity.class));
//                            finish();
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                mVerificationField.setError("Invalid code.");
                            }
                        }
                    }
                });
    }


    private void startPhoneNumberVerification(String phoneNumber) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60,  TimeUnit.SECONDS,   this,  mCallbacks);        }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60,   TimeUnit.SECONDS,  this,  mCallbacks, token);      }

    private boolean validatePhoneNumber() {
        String phoneNumber = mPhoneNumberField.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            mPhoneNumberField.setError("Invalid phone number.");
            return false;
        }
        return true;
    }
    @Override
    public void onStart() {
        super.onStart();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null) {
//            startActivity(new Intent(PhoneAuthActivity.this, MainActivity.class));
//            finish();
//        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_start_verification:
                mVerificationField.setVisibility(View.VISIBLE);
                mResendButton.setVisibility(View.VISIBLE);
                mVerifyButton.setVisibility(View.VISIBLE);
                mStartButton.setVisibility(View.INVISIBLE);
                if (!validatePhoneNumber()) {
                    return;
                }
                startPhoneNumberVerification(mPhoneNumberField.getText().toString());
                break;
            case R.id.button_verify_phone:
                String code = mVerificationField.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    mVerificationField.setError("Cannot be empty.");
                    return;
                }

                verifyPhoneNumberWithCode(mVerificationId, code);
                break;
            case R.id.button_resend:
                resendVerificationCode(mPhoneNumberField.getText().toString(), mResendToken);
                break;
        }

    }

    private Boolean validate(){
        Boolean res=false;
        String fName=FirstName.getText().toString().trim();
        String lName=LastName.getText().toString().trim();
        String pas=pass.getText().toString().trim();
        String confirmPas=confirmPass.getText().toString().trim();

        if((fName.isEmpty()) || (lName.isEmpty()) || (!(pas.equals(confirmPas))) || pas.isEmpty() || confirmPas.isEmpty()) {
            if (fName.isEmpty() || lName.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Enter Name", Toast.LENGTH_SHORT).show();
            }

            if(pas.isEmpty()){
                Toast.makeText(this, "Enter Password ", Toast.LENGTH_SHORT).show();
            }
            if(confirmPas.isEmpty()){
                Toast.makeText(this, "Enter Confirm Password", Toast.LENGTH_SHORT).show();
            }
            if (!(pas.equals(confirmPas))) {
                Toast.makeText(this, "Password does not match with Confirm Password", Toast.LENGTH_SHORT).show();
            }

        }
        else{

            res=true;
        }
        return  res;
    }


}