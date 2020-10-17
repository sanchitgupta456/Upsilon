package com.sanchit.Upsilon.ui.login;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.sanchit.Upsilon.R;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;

/*
 * The problem: I feel the main problem is that this link:
 * https://github.com/googlesamples/google-services/blob/master/android/signin/app/src/main/java/com/google/samples/quickstart/signin/IdTokenActivity.java
 * its  a starter implementation for this.
 * It doesn't actually implement login and auth.
 * It just obtains the token
 * Now we know auth process requires app.loginAsync, which means we need to somehow
 * use the app object which we created in OnCreate inside the handleSignInResult method
 * But since app is a local object, it can't be accessed in the handleSignInResult method
 * So three options,
 * 1. make the app a class attribute: which produces a null value error (try it if you want)
 * 2. declare a new app object in the handleSignInResult method.
 *    This produces a Google ApiError 10. Read about it here:
 *    https://stackoverflow.com/questions/47619229/google-sign-in-failed-com-google-android-gms-common-api-apiexception-10
 * 3. Pass the app object by reference to the handleSignInResult method (which is implemented)
 * option 3 is the only one that seems to produce a google account selection screen
 * But when we click on our google account, it gives ApiError 8
 * this stackoverflow article explains what that is:
 * https://stackoverflow.com/questions/35229264/android-google-sign-in-fails-with-error-code-8-no-message
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    public GoogleSignInClient mGoogleSignInClient;
    private static final int RC_GET_TOKEN = 9002;
    String appID = "upsilon-ityvn";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = (Button)findViewById(R.id.login);
        final Button signUpButton = (Button)findViewById(R.id.signUp);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        final ImageView GoogleSignIn = findViewById(R.id.googleSignIn);
        //Realm realm = Realm.getDefaultInstance();
        //realm.close();
        Realm.init(this); // context, usually an Activity or Application

        //This is the app object I was talking about
        App app = new App(new AppConfiguration.Builder(appID)
                .build());

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                goToSignInActivity();
            }
        });

        GoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle(app);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String email = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                Credentials emailPasswordCredentials = Credentials.emailPassword(email, password);

                app.loginAsync(emailPasswordCredentials, new App.Callback<User>() {
                    @Override
                    public void onResult(App.Result<User> it) {
                        if (it.isSuccess()) {
                            Log.v(TAG, "Successfully authenticated using an email and password.");

                            User user = app.currentUser();
                        } else {
                            Log.v(TAG, "LOGIN FAILED!");
                            Log.e(TAG, it.getError().toString());
                        }
                    }
                });
            }
        });
    }

    public void signInWithGoogle(App app){
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //This makes the menu for selecting the google account show up
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        Log.v(TAG, "Menu Showed up");

        //After google account is selected, this function tries to get an access token
        //RC_GET_TOKEN is just a number. It's not important.
        //Basically it represents the "coded number" for which process is calling startActivityForResult
        //Thats how startActivityForResult knows for what it is being called (this is like a callback)
        startActivityForResult(signInIntent, RC_GET_TOKEN, app);
    }


    public void startActivityForResult(Intent data, int code,  App app) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        //function which extracts the access key and starts the Auth process
        handleSignInResult(task, app);
    }

    private void handleSignInResult(@NonNull Task<GoogleSignInAccount> completedTask, App app) {
        try {
            //Get the account and its token
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String token = account.getIdToken();

            //Load credentials with token
            Credentials googleCredentials = Credentials.google(token);

            app.loginAsync(googleCredentials, it -> {
                if (it.isSuccess()) {
                    Log.v(TAG, "Successfully authenticated using Google OAuth.");
                    Toast toast = Toast.makeText(getApplicationContext(), "Success!!", Toast.LENGTH_SHORT);
                    toast.show();
                    User user = app.currentUser();
                } else {
                    Log.v(TAG, "FAILED authenticating with Google OAuth.");
                    Toast toast = Toast.makeText(getApplicationContext(), "Fail!!", Toast.LENGTH_SHORT);
                    toast.show();
                    Log.e(TAG, it.getError().toString());
                }
            });
            Log.w(TAG, "SUCCESS] Obtained access token");
        } catch (ApiException e) {
            Log.w(TAG, "FAILED to obtain access token] ", e);
        }
    }

    public void goToSignInActivity(){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}