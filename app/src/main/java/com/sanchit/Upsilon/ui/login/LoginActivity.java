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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
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
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "LoginActivity";
    private static final int RC_GET_TOKEN = 9002;
    private GoogleSignInClient mGoogleSignInClient;
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
        final ImageView GoogleSignInImage = findViewById(R.id.googleSignIn);
        findViewById(R.id.googleSignIn).setOnClickListener(this);

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

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //String email = usernameEditText.getText().toString();
                //String password = passwordEditText.getText().toString();
                Toast.makeText(LoginActivity.this,"Hello",Toast.LENGTH_LONG);
                /*Credentials emailPasswordCredentials = Credentials.emailPassword(email, password);

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
                });*/
            }
        });
// For sample only: make sure there is a valid server client ID.
        validateServerClientID();
        // [START configure_signin]
        // Request only the user's ID token, which can be used to identify the
        // user securely to your backend. This will contain the user's basic
        // profile (name, profile picture URL, etc) so you should not need to
        // make an additional call to personalize your application.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        // [END configure_signin]
        // Build GoogleAPIClient with the Google Sign-In API and the above options.
        mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);
       /*GoogleSignInImage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Toast.makeText(LoginActivity.this,"Hello",Toast.LENGTH_LONG);


           }
       });*/

    }

    private void getIdToken() {
        // Show an account picker to let the user choose a Google account from the device.
        // If the GoogleSignInOptions only asks for IDToken and/or profile and/or email then no
        // consent screen will be shown here.
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_GET_TOKEN);
    }

    private void refreshIdToken() {
        // Attempt to silently refresh the GoogleSignInAccount. If the GoogleSignInAccount
        // already has a valid token this method may complete immediately.
        //
        // If the user has not previously signed in on this device or the sign-in has expired,
        // this asynchronous branch will attempt to sign in the user silently and get a valid
        // ID token. Cross-device single sign on will occur in this branch.
        mGoogleSignInClient.silentSignIn()
                .addOnCompleteListener(this, new OnCompleteListener<GoogleSignInAccount>() {
                    @Override
                    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                        handleSignInResult(task);
                    }
                });
    }

    // [START handle_sign_in_result]
    private void handleSignInResult(@NonNull Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();

            // TODO(developer): send ID Token to server and validate

            Credentials googleCredentials = Credentials.google(idToken);

            App app = new App(new AppConfiguration.Builder(appID)
                    .build());

            app.loginAsync(googleCredentials, it -> {
                if (it.isSuccess()) {
                    Log.v(TAG, "Successfully authenticated using Google OAuth.");
                    User user = app.currentUser();
                } else {
                    Log.e(TAG, it.getError().toString());
                }
            });

            //updateUI(account);
        } catch (ApiException e) {
            Log.w(TAG, "handleSignInResult:error", e);
            //updateUI(null);
        }
    }
    // [END handle_sign_in_result]

    /*private void signOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                updateUI(null);
            }
        });
    }*/

    /*private void revokeAccess() {
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GET_TOKEN) {
            // [START get_id_token]
            // This task is always completed immediately, there is no need to attach an
            // asynchronous listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
            // [END get_id_token]
        }
    }

    /**
     * Validates that there is a reasonable server client ID in strings.xml, this is only needed
     * to make sure users of this sample follow the README.
     */
    private void validateServerClientID() {
        String serverClientId = getString(R.string.server_client_id);
        String suffix = ".apps.googleusercontent.com";
        if (!serverClientId.trim().endsWith(suffix)) {
            String message = "Invalid server client ID in strings.xml, must end with " + suffix;

            Log.w(TAG, message);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.googleSignIn:
                getIdToken();
                break;

        }
    }



    public void goToSignInActivity(){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}