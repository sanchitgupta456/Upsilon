package com.sanchit.Upsilon.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.sanchit.Upsilon.R;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SignUpActivity";
    private static final int RC_GET_AUTH_CODE = 9003;

    String appID = "upsilon-ityvn";

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        App app = new App(new AppConfiguration.Builder(appID)
                .build());

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final EditText cnfPasswordEditText = findViewById(R.id.confirmPassword);
        final EditText emailEditText = findViewById(R.id.email);
        final Button signUpButton = findViewById(R.id.signupBtn);
        findViewById(R.id.googleSignUp).setOnClickListener(this);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(SignUpActivity.this,"Hello",Toast.LENGTH_LONG);
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                app.getEmailPassword().registerUserAsync(email, password, it -> {
                    if (it.isSuccess()) {
                        Log.i(TAG,"Successfully registered user.");

                    } else {
                        Log.e(TAG,"Failed to register user: ${it.error}");
                    }
                });
            }
        });

        // For sample only: make sure there is a valid server client ID.
        validateServerClientID();

        // [START configure_signin]
        // Configure sign-in to request offline access to the user's ID, basic
        // profile, and Google Drive. The first time you request a code you will
        // be able to exchange it for an access token and refresh token, which
        // you should store. In subsequent calls, the code will only result in
        // an access token. By asking for profile access (through
        // DEFAULT_SIGN_IN) you will also get an ID Token as a result of the
        // code exchange.
        String serverClientId = getString(R.string.server_client_id);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
                .requestServerAuthCode(serverClientId)
                .requestEmail()
                .build();
        // [END configure_signin]
        mGoogleSignInClient = GoogleSignIn.getClient(SignUpActivity.this, gso);
        signOut();
    }

    private void getAuthCode() {
        // Start the retrieval process for a server auth code.  If requested, ask for a refresh
        // token.  Otherwise, only get an access token if a refresh token has been previously
        // retrieved.  Getting a new access token for an existing grant does not require
        // user consent.
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_GET_AUTH_CODE);
    }

    private void signOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            }
        });
    }

    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GET_AUTH_CODE) {
            // [START get_auth_code]
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                String authCode = account.getServerAuthCode();

                // Show signed-un UI

                // TODO(developer): send code to server and exchange for access/refresh/ID tokens

                Credentials googleCredentials = Credentials.google(authCode);

                final App app = new App(new AppConfiguration.Builder(appID)
                        .build());

                app.loginAsync(googleCredentials, new App.Callback<User>() {
                    @Override
                    public void onResult(App.Result<User> it) {
                        if (it.isSuccess()) {
                            Log.v(TAG, "Successfully authenticated using Google OAuth.");
                            User user = app.currentUser();
                        } else {
                            Log.e(TAG, it.getError().toString());
                        }
                    }
                });

            } catch (ApiException e) {
                Log.w(TAG, "Sign-in failed", e);
            }
            // [END get_auth_code]
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
            case R.id.googleSignUp:
                getAuthCode();
                break;

        }
    }

}