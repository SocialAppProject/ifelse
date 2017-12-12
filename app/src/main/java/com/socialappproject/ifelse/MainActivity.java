package com.socialappproject.ifelse;

/**
 * Created by Kimjungmin on 2017. 10. 15..
 */

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_WRITE = 0;
    private static final int REQUEST_ARTICLE = 1;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private BottomNavigationView mNavigationView;

    public static User currentUser;
    public static FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            authenticate();
            getUserInfo();
            ArticleListManager.get(getApplicationContext());
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "네트워크 오류", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        finally {
            endSplash();
        }
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();

        if (fm.findFragmentById(R.id.fragment_container) == null)
            fm.beginTransaction().add(R.id.fragment_container, CategoryFragment.newInstance()).commit();

        mNavigationView = findViewById(R.id.navigation);
        mNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.navigation_newsfeed:
                        selectedFragment = CategoryFragment.newInstance();
                        break;
                    case R.id.navigation_mymenu:
                        selectedFragment = MymenuFragment.newInstance();
                        break;
                    case R.id.navigation_statistic:
                        selectedFragment = StatisticFragment.newInstance();
                        break;
                    case R.id.navigation_setting:
                        selectedFragment = SettingFragment.newInstance();
                        break;
                }

                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.fragment_container, selectedFragment);
                transaction.commit();
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_WRITE) {
            if (resultCode == RESULT_OK) {
            }
        } else if (requestCode == REQUEST_ARTICLE) {
            if (resultCode == RESULT_OK) {
            }
        }
    }

    private void authenticate() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {// 로그인 되어있음
                    DatabaseManager.databaseReference.child("USER").orderByChild("email").equalTo(user.getEmail()).
                            addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                                    currentUser = new User(dataSnapshot.child("email").getValue().toString(),
                                            dataSnapshot.child("name").getValue().toString(),
                                            Integer.parseInt(dataSnapshot.child("gender").getValue().toString()),
                                            Integer.parseInt(dataSnapshot.child("old").getValue().toString()),
                                            Integer.parseInt(dataSnapshot.child("star").getValue().toString()));
                                }

                                @Override
                                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                    currentUser.setStar(Integer.parseInt(dataSnapshot.child("star").getValue().toString()));
                                }

                                @Override
                                public void onChildRemoved(DataSnapshot dataSnapshot) {

                                }

                                @Override
                                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }

                                // ...
                            });
                } else {// 로그인 안되어있음
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
            }
        };
    }

    private void getUserInfo() {

    }

    private void endSplash() {
        SystemClock.sleep(2000);
        setTheme(R.style.AppTheme);
    }

    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }
}