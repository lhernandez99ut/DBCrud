package com.utrm.dbcrud;

import com.google.firebase.database.FirebaseDatabase;

public class Firebaseapp extends android.app.Application{

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
