package com.socialappproject.ifelse;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Kimjungmin on 2017. 10. 30..
 */


public class DatabaseManager {
    public static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
}

