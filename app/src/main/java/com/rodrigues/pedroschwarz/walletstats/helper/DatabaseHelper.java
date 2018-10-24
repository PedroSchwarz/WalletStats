package com.rodrigues.pedroschwarz.walletstats.helper;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class DatabaseHelper {

    private static FirebaseFirestore getRootRef() {
        return FirebaseFirestore.getInstance();
    }

    public static Query getTransactionsRef(String dateKey) {
        return getRootRef().collection("Transactions").document(AuthHelper.getUserId()).collection(dateKey).orderBy("type", Query.Direction.DESCENDING);
    }

    public static CollectionReference getTransactionRef(String dateKey) {
        return getRootRef().collection("Transactions").document(AuthHelper.getUserId()).collection(dateKey);
    }

    public static DocumentReference getUserRef() {
        return getRootRef().collection("Users").document(AuthHelper.getUserId());
    }
}
