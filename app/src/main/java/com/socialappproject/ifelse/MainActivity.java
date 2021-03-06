package com.socialappproject.ifelse;

/**
 * Created by Kimjungmin on 2017. 10. 15..
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_WRITE = 0;
    private static final int REQUEST_ARTICLE = 1;
    public static User currentUser;
    public static FragmentManager fm;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private BottomNavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            authenticate();
            ArticleListManager.get(getApplicationContext());
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "네트워크 오류", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } finally {
            SystemClock.sleep(1000);
            setContentView(R.layout.activity_main);
        }

        fm = getSupportFragmentManager();

        if (fm.findFragmentById(R.id.fragment_container) == null)
            fm.beginTransaction().replace(R.id.fragment_container, CategoryFragment.newInstance()).commit();

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
                fm.popBackStack();
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
                                    try {
                                        currentUser = new User(dataSnapshot.child("email").getValue().toString(),
                                                dataSnapshot.child("name").getValue().toString(),
                                                Integer.parseInt(dataSnapshot.child("gender").getValue().toString()),
                                                Integer.parseInt(dataSnapshot.child("old").getValue().toString()),
                                                Integer.parseInt(dataSnapshot.child("star").getValue().toString()));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    } finally {
                                        ArticleListManager.get(getApplicationContext()).update();
                                    }
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
                                    Toast.makeText(getApplicationContext(), "네트워크 오류", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {// 로그인 안되어있음
                    if (currentUser == null)
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    else {
                        moveTaskToBack(true);
                        finish();
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                }
            }
        };
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
}