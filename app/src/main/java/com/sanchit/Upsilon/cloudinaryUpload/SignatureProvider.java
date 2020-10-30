package com.sanchit.Upsilon.cloudinaryUpload;

import android.content.Context;

import com.sanchit.Upsilon.cloudinaryUpload.Signature;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.UploadRequest;
import com.sanchit.Upsilon.ui.login.SignUpActivity;

import java.util.Map;

public interface SignatureProvider {
    Signature provideSignature(Map options);

    String getName();
}