package com.sanchit.Upsilon.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class googleAuth {
    private static final int RC_GET_TOKEN = 9002;
    private String token = "";

    public GoogleSignInClient mGoogleSignInClient;

    public Intent signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        return signInIntent;
    }

    public int getRcGetToken(){
        return RC_GET_TOKEN;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public String getToken(){
        return token;
    }
}
