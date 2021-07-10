package com.sanchit.Upsilon.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.EventLog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sanchit.Upsilon.Interest.Interest;
import com.sanchit.Upsilon.MainActivity;
import com.sanchit.Upsilon.R;
import com.sanchit.Upsilon.Terms;
import com.sanchit.Upsilon.Upsilon;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SignUpActivity";
    private static final int RC_GET_AUTH_CODE = 9003;

    String appID = "upsilon-ityvn";

    TextInputEditText passwordEditText;
    TextInputEditText cnfPasswordEditText;
    TextInputEditText emailEditText;
    TextInputLayout emailLayout;
    TextInputLayout passwordLayout;
    TextInputLayout confirmPasswordLayout;
    CheckBox checkBox;
    App app;

    private GoogleSignInClient mGoogleSignInClient;

    private CallbackManager callbackManager1;
    LoginButton fbsignupButton;
    private RequestQueue queue;
    private String API ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        Objects.requireNonNull(this.getSupportActionBar()).hide();
        app = new App(new AppConfiguration.Builder(appID)
                .build());
        queue = Volley.newRequestQueue(getApplicationContext());
        API = ((Upsilon)this.getApplication()).getAPI();

//        final EditText usernameEditText = findViewById(R.id.username);
//        final TextInputEditText passwordEditText;
//        final TextInputEditText cnfPasswordEditText;
//        final TextInputEditText emailEditText;
//        final TextInputLayout emailLayout;
//        final TextInputLayout passwordLayout;
//        final TextInputLayout confirmPasswordLayout;
        passwordEditText = findViewById(R.id.password);
        cnfPasswordEditText = findViewById(R.id.confirmPassword);
        emailEditText = findViewById(R.id.email);
        emailLayout = findViewById(R.id.ll1);
        passwordLayout = findViewById(R.id.ll2);
        confirmPasswordLayout = findViewById(R.id.ll3);
        final Button signUpButton = findViewById(R.id.signupBtn);
//        final CheckBox checkBox;
        checkBox = findViewById(R.id.accept_tc);
        final TextView linkTC = findViewById(R.id.link_tc);
        fbsignupButton = findViewById(R.id.signup_button);
        final TextView memberalready = findViewById(R.id.alreadyAMember);
        final ImageView FacebookSignInImage = findViewById(R.id.facebookSignUp);
        findViewById(R.id.facebookSignUp).setOnClickListener(this);
        findViewById(R.id.googleSignUp).setOnClickListener(this);

        linkTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Terms.class);
                startActivity(intent);
            }
        });

        callbackManager1 = CallbackManager.Factory.create();
        fbsignupButton.registerCallback(callbackManager1, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.v("User","Facebook Sign Up"+loginResult.toString());

                String accessToken = loginResult.getAccessToken().getToken().toString();

                Credentials facebookCredentials  = Credentials.facebook(accessToken);
                app.loginAsync(facebookCredentials, it -> {
                    if (it.isSuccess()) {
                        Log.v("AUTH", "Successfully logged in to MongoDB Realm using Facebook OAuth.");
                        goToMainActivity();
                    } else {
                        Log.e("AUTH", "Failed to log in to MongoDB Realm", it.getError());
                    }
                });
            }

            @Override
            public void onCancel() {

                Log.v("Error","Cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                Log.v("Error","Error"+error.toString());
            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.toString().length()<8) {
                    passwordLayout.setErrorEnabled(true);
                    passwordLayout.setError("Less than required characters");
                } else {
                    passwordLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length()<8) {
                    passwordLayout.setErrorEnabled(true);
                    passwordLayout.setError("Less than required characters");
                } else {
                    passwordLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length()<8) {
                    passwordLayout.setErrorEnabled(true);
                    passwordLayout.setError("Less than required characters");
                } else {
                    passwordLayout.setErrorEnabled(false);
                }
            }
        });

        cnfPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(!passwordEditText.getText().toString().equals(s.toString())) {
                    confirmPasswordLayout.setErrorEnabled(true);
                    confirmPasswordLayout.setError("Passwords don't match");
                }
                else {
                    confirmPasswordLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!passwordEditText.getText().toString().equals(s.toString())) {
                    confirmPasswordLayout.setErrorEnabled(true);
                    confirmPasswordLayout.setError("Passwords don't match");
                }
                else {
                    confirmPasswordLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!passwordEditText.getText().toString().equals(s.toString())) {
                    confirmPasswordLayout.setErrorEnabled(true);
                    confirmPasswordLayout.setError("Passwords don't match");
                }
                else {
                    confirmPasswordLayout.setErrorEnabled(false);
                }
            }
        });
        
        cnfPasswordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    Log.d(TAG, "onEditorAction: done clicked");
                    signup();
                }
                return false;
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                signup();
            }
        });

        memberalready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLoginActivity();
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
                            goToMainActivity();
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
            case R.id.facebookSignUp:
                fbsignupButton.performClick();

        }
    }

    public void goToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void goToLoginActivity(){
        Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
        startActivity(intent);
    }

    public void signup() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirm = cnfPasswordEditText.getText().toString();

        if (email.length() == 0){
            Animation shake = AnimationUtils.loadAnimation(SignUpActivity.this, R.anim.shake);
            emailEditText.startAnimation(shake);
            emailEditText.setError("Please Enter an Email!");
            emailEditText.requestFocus();
            return;
        }
        else if (password.length() == 0){
            Animation shake = AnimationUtils.loadAnimation(SignUpActivity.this, R.anim.shake);
            passwordEditText.startAnimation(shake);
            passwordEditText.setText("");
            cnfPasswordEditText.setText("");
            passwordEditText.setError("Please Enter a Valid Password!");
            passwordEditText.requestFocus();
            return;
        }
//                else if (usernameEditText.getText().length() == 0){
//                    Animation shake = AnimationUtils.loadAnimation(SignUpActivity.this, R.anim.shake);
//                    usernameEditText.startAnimation(shake);
//                    usernameEditText.setError("Please Enter a Valid Username!");
//                    usernameEditText.requestFocus();
//                    return;
//                }
        else if (!password.equals(confirm)){
            Animation shake = AnimationUtils.loadAnimation(SignUpActivity.this, R.anim.shake);
            passwordEditText.startAnimation(shake);
            passwordEditText.setText("");
            cnfPasswordEditText.setText("");
            passwordEditText.setError("Passwords do not match!");
            passwordEditText.requestFocus();
            return;
        } else if (!checkBox.isChecked()){
            Toast.makeText(getApplicationContext(), "Please accept the terms and conditions", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", email);
            jsonBody.put("password", password);
            signupRequest(jsonBody,emailEditText);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        app.getEmailPassword().registerUserAsync(email, password, it -> {
//            if (it.isSuccess()) {
//                Log.i(TAG,"Successfully registered user.");
//                goToLoginActivity();
//
//            } else {
//                Log.e(TAG,"Failed to register user: ${it.error}");
//                Animation shake = AnimationUtils.loadAnimation(SignUpActivity.this, R.anim.shake);
//                emailEditText.startAnimation(shake);
//                emailEditText.setText("");
//                emailEditText.setError("Invalid email!");
//                emailEditText.requestFocus();
//            }
//        });
    }

    private void signupRequest(JSONObject credentials,EditText emailEditText) {
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, API+"/signup",credentials,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        try {
                            ((Upsilon)getApplication()).setToken(response.getString("token"));
                            Log.v(TAG, "Successfully authenticated using an email and password.");
                            goToMainActivity();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error response", error.toString());
                        Animation shake = AnimationUtils.loadAnimation(SignUpActivity.this, R.anim.shake);
                        emailEditText.startAnimation(shake);
                        emailEditText.setError(error.toString());
                        emailEditText.requestFocus();
                        Log.v(TAG, "SIGNUP FAILED!");
                        Log.e(TAG, error.toString());
                    }
                }
        );
        queue.add(jsonRequest);
    }

}