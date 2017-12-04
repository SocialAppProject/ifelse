package com.socialappproject.ifelse;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Kimjungmin on 2017. 12. 4..
 */

public class StorageManager {
    public static StorageReference storageReference = FirebaseStorage.getInstance().getReference();
}
