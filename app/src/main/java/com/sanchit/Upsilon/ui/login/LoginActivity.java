package com.sanchit.Upsilon.ui.login;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.load.resource.drawable.DrawableResource;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.Login;
import com.facebook.login.LoginManager;
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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sanchit.Upsilon.MainActivity;
import com.sanchit.Upsilon.R;
import com.sanchit.Upsilon.Upsilon;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Objects;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "LoginActivity";
    private static final int RC_GET_AUTH_CODE = 9003;

    String appID = "upsilon-ityvn";

    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager callbackManager;
    LoginButton fbloginButton;
    ProgressBar loadingProgressBar;
    private CheckBox rememberMe;
    public static final String PREFS_NAME = "MyPrefsFile";
    private static String PREF_USERNAME = "username";
    private static String PREF_PASSWORD = "password";
    private String Email;
    private TextView ForgotPassword;
    private String forgotpasswordemail;
    private RequestQueue queue;
    private String API ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(this.getSupportActionBar()).hide();
        queue = Volley.newRequestQueue(getApplicationContext());
        API = ((Upsilon)this.getApplication()).getAPI();
        /*
        getSupportActionBar().setTitle(R.string.login_title_text);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getColor(R.color.colorPrimaryDark)));
         */
        final TextInputEditText usernameEditText = findViewById(R.id.username);
        final TextInputEditText passwordEditText = findViewById(R.id.password);
        MaterialButton loginButton = findViewById(R.id.login);
        fbloginButton = findViewById(R.id.login_button);
        final TextView signUpButton = (TextView) findViewById(R.id.signUp);
        loadingProgressBar = findViewById(R.id.loading);
        final ImageView GoogleSignInImage = findViewById(R.id.googleSignIn);
        final ImageView FacebookSignInImage = findViewById(R.id.facebookSignIn);
        ForgotPassword = (TextView) findViewById(R.id.textView3);
        rememberMe = (CheckBox) findViewById(R.id.checkRememberMe);

        findViewById(R.id.googleSignIn).setOnClickListener(this);
        findViewById(R.id.facebookSignIn).setOnClickListener(this);

        SharedPreferences pref = getApplication().getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        String username = pref.getString("user", null);
        String password = pref.getString("pass", null);
        if(username!=null && password!=null) {
            usernameEditText.setText(username);
            passwordEditText.setText(password);
        }
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

//        //Realm.init(this); // context, usually an Activity or Application
//        App app = new App(new AppConfiguration.Builder(appID)
//                .build());

        ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                forgotpasswordemail = usernameEditText.getText().toString();
                //app.getEmailPassword().sendResetPasswordEmail(forgotpasswordemail);
                Log.v("Email",forgotpasswordemail);
//                app.getEmailPassword().sendResetPasswordEmailAsync(forgotpasswordemail,result -> {
//                    if(result.isSuccess())
//                    {
//                        loadingProgressBar.setVisibility(View.GONE);
//                        Toast.makeText(getApplicationContext(), "An email has been sent to you. Please go through it for further instructions.", Toast.LENGTH_LONG).show();
//                        Log.v("Result",result.toString());
//                        Log.v("Success","Success");
//                    }
//                    else
//                    {
//                        Log.v("Error",result.getError().toString());
//                        loadingProgressBar.setVisibility(View.GONE);
//                    }
//                });
//
//                app.getEmailPassword().resendConfirmationEmailAsync(forgotpasswordemail,result -> {
//                    if(result.isSuccess())
//                    {
//                        Log.v("Result",result.toString());
//                        Log.v("Success","Success");
//                    }
//                    else
//                    {
//                        Log.v("Error",result.getError().toString());
//                    }
//                });
            }
        });

        callbackManager = CallbackManager.Factory.create();
        fbloginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.v("User","Facebook Signed In"+loginResult.toString());

                String accessToken = loginResult.getAccessToken().getToken().toString();

                Credentials facebookCredentials  = Credentials.facebook(accessToken);
//                app.loginAsync(facebookCredentials, it -> {
//                    if (it.isSuccess()) {
//                        Log.v("AUTH", "Successfully logged in to MongoDB Realm using Facebook OAuth.");
//                        goToMainActivity();
//                    } else {
//                        Log.e("AUTH", "Failed to log in to MongoDB Realm", it.getError());
//                    }
//                });
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                goToSignInActivity();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loadingProgressBar.setIndeterminate(true);
                loadingProgressBar.setProgress(0);
                String email = usernameEditText.getText().toString();
                Email = email;
                String password = passwordEditText.getText().toString();

                if(email.isEmpty())
                {
                    Animation shake = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.shake);
                    usernameEditText.startAnimation(shake);
                    usernameEditText.setError("Please Enter a Valid Email");
                    usernameEditText.requestFocus();
                    loadingProgressBar.setVisibility(View.INVISIBLE);
                }
                else if(password.isEmpty())
                {
                    Animation shake = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.shake);
                    passwordEditText.startAnimation(shake);
                    passwordEditText.setError("Please Enter a Valid Password");
                    passwordEditText.requestFocus();
                    loadingProgressBar.setVisibility(View.INVISIBLE);
                }
                else
                {
                    if(rememberMe.isChecked())
                    {
                        getApplication().getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
                                .edit()
                                .putString("user", email)
                                .putString("pass", password)
                                .apply();

                    }
                    try {
                        JSONObject jsonBody = new JSONObject();
                        jsonBody.put("email", email);
                        jsonBody.put("password", password);
                        login(jsonBody,passwordEditText);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
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
        mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);
        signOut();
    }



    private void getAuthCode() {
        // Start the retrieval process for a server auth code.  If requested, ask for a refresh
        // token.  Otherwise, only get an access token if a refresh token has been previously
        // retrieved.  Getting a new access token for an existing grant does not require
        // user consent.
        loadingProgressBar.setVisibility(View.VISIBLE);
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
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
                            /*MongoClient mongoClient =
                                    user.getMongoClient("mongodb-atlas");
                            MongoDatabase mongoDatabase =
                                    mongoClient.getDatabase("Upsilon");
                            MongoCollection<Document> mongoCollection =
                                    mongoDatabase.getCollection("UserData");

                            mongoCollection.insertOne(
                                    new Document("userid", user.getId()).append("favoriteColor", "pink"))
                                    .getAsync(result -> {
                                        if (result.isSuccess()) {
                                            Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
                                                    + result.get().getInsertedId());
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        } else {
                                            Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
                                        }
                                    });*/
                            loadingProgressBar.setVisibility(View.INVISIBLE);
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
            case R.id.googleSignIn:
                        getAuthCode();
                break;
            case R.id.facebookSignIn:
                fbloginButton.performClick();
        }
    }



    public void goToSignInActivity(){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void goToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("email",Email);
        startActivity(intent);
    }

    private void login(JSONObject credentials,EditText passwordEditText) {
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, API+"/signin",credentials,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        try {
                            ((Upsilon)getApplication()).setToken(response.getString("token"));
                            try {
                                Gson gson= new Gson();
                                com.sanchit.Upsilon.userData.User user = gson.fromJson(String.valueOf(response.get("user")), com.sanchit.Upsilon.userData.User.class);
                                ((Upsilon)getApplication()).setUser(user);
                                goToMainActivity();
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            }
                            Log.v(TAG, "Successfully authenticated using an email and password.");
                            loadingProgressBar.setVisibility(View.INVISIBLE);
//                            ((Upsilon)getApplication()).initialise();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error response", error.toString());
                        Animation shake = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.shake);
                        passwordEditText.startAnimation(shake);
                        passwordEditText.setError(error.toString());
                        passwordEditText.requestFocus();
                        loadingProgressBar.setVisibility(View.INVISIBLE);
                        Log.v(TAG, "LOGIN FAILED!");
                        Log.e(TAG, error.toString());
                    }
                }
        );
        queue.add(jsonRequest);
    }


}