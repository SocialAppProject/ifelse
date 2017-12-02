package com.socialappproject.ifelse;

/**
 * Created by Kimjungmin on 2017. 10. 15..
 */

import android.content.Intent;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class MainActivity extends FragmentActivity {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_WRITE = 0;
    private static final int REQUEST_ARTICLE = 1;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private BottomNavigationView mNavigationView;

    public static User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        authenticate();
        getUserInfo();
        endSplash();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //authenticate();
        //getUserInfo();

        if(getSupportFragmentManager().findFragmentById(R.id.fragment_container) == null)
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, NewsfeedFragment.newInstance()).commit();


        mNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        mNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.navigation_newsfeed:
                        selectedFragment = NewsfeedFragment.newInstance();
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

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
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

    /*TODO : BUG - register한뒤 로그인 안하고 그냥 앱 나갔다 오면 자동으로 로그인 되어있음
            회원가입시 authstatelistener가 로그인 된 상태로 인식하기 때문인거같음
     */
    private void authenticate() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {// 로그인 되어있음
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Toast.makeText(getApplicationContext(), user.getEmail() + "님 안녕하세요!",
                            Toast.LENGTH_SHORT).show();

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
