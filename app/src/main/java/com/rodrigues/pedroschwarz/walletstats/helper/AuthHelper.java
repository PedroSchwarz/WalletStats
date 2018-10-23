package com.rodrigues.pedroschwarz.walletstats.helper;

import com.google.firebase.auth.FirebaseAuth;

public class AuthHelper {

    public static FirebaseAuth getAuth() {
        return FirebaseAuth.getInstance();
    }

    public static String getUserId() {
        return getAuth().getCurrentUser().getUid();
    }
}
