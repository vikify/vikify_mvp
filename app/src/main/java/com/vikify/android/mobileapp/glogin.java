package com.vikify.android.mobileapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class glogin extends AppCompatActivity {
    private static final String TAG = "Glogin";
    private FirebaseAuth mAuth;
    CallbackManager callbackManager;
    private static final String EMAIL = "email";
    Button phoneNumberButton;
    LoginButton loginButton;
    EditText phone_number;
    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    static final int RC_SIGN_IN = 1;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glogin);
        mAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();
        loginButton =  findViewById(R.id.login_button);
        loginButton.setReadPermissions("email","public_profile");
        FacebookSdk.sdkInitialize(getApplicationContext());
        phoneNumberButton = findViewById(R.id.phone_number_btn);


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.v(TAG, "Facebook login success");
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {
                Log.v(TAG,"Facebook login failure");
            }
        });

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



        phoneNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(glogin.this);
                LayoutInflater inflater = glogin.this.getLayoutInflater();
                final View main_view = inflater.inflate(R.layout.phone_number_verification_dialog, null);

                builder.setView(main_view);


                builder.setPositiveButton("Verify", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        phone_number = main_view.findViewById(R.id.phone);
                        String phone = phone_number.getText().toString();
                        if (phone.isEmpty()) {
                            Toast.makeText(glogin.this, "Enter a phone number", Toast.LENGTH_SHORT).show();
                        } else {

                            Toast.makeText(glogin.this, phone, Toast.LENGTH_SHORT).show();
                            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                    phone,        // Phone number to verify
                                    60,                 // Timeout duration
                                    TimeUnit.SECONDS,   // Unit of timeout
                                    glogin.this,               // Activity (for callback binding)
                                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                        @Override
                                        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                                            Log.d(TAG, "onVerificationCompleted:" + phoneAuthCredential);
                                            Intent intent = new Intent(getApplicationContext(), NavDraw.class);
                                            startActivity(intent);
                                            finish();
                                            signInWithPhoneAuthCredential(phoneAuthCredential);
                                        }

                                        @Override
                                        public void onVerificationFailed(FirebaseException e) {
                                            // This callback is invoked in an invalid request for verification is made,
                                            // for instance if the the phone number format is not valid.
                                            Log.w(TAG, "onVerificationFailed", e);

                                            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                                // Invalid request
                                                // ...
                                            } else if (e instanceof FirebaseTooManyRequestsException) {
                                                // The SMS quota for the project has been exceeded
                                                // ...
                                            }

                                            // Show a message and update the UI
                                            // ...
                                        }

//                                        @Override
//                                        public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                                            Log.d(TAG, "onCodeSent:" + verificationId);
//
//                                            // Save verification ID and resending token so we can use them later
//                                            mVerificationId = verificationId;
//                                            mResendToken = forceResendingToken;
//                                        }
                                    });        // OnVerificationStateChangedCallbacks
                        }
                    }
                }).show();

            }
        });


    }

    public void signIn() {
        Intent mSgnInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(mSgnInIntent, RC_SIGN_IN);
    }


    @Override
    protected void onStart() {
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//        if(account!=null) {
//            Intent intent=new Intent(this,NavDraw.class);
//            startActivity(intent);
//            finish();
//        }


        //Toast.makeText(getApplicationContext(),"Logged in already",Toast.LENGTH_SHORT).show();
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.v(TAG, "USER is:" + currentUser);
        if (currentUser != null) {

            Intent intent = new Intent(this, NavDraw.class);
            startActivity(intent);
            finish();
            //Toast.makeText(getApplicationContext(),"Logged in already",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            firebaseAuthWithGoogle(account);
            Intent intent = new Intent(getApplicationContext(), NavDraw.class);
            startActivity(intent);
            finish();
            // Signed in successfully, show authenticated UI.
            // updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            // updateUI(null);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "FirebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        Log.v(TAG, "Executed1");
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
//                            Intent intent=new Intent(getApplicationContext(),NavDraw.class);
//                            startActivity(intent);
                            Log.v(TAG, "Executed2");
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                        }

                        // ...
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        Intent intent = new Intent(getApplicationContext(), NavDraw.class);
        startActivity(intent);
        finish();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(glogin.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }
}
