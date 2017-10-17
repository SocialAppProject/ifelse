package com.socialappproject.ifelse;

/**
 * Created by Kimjungmin on 2017. 10. 15..
 */

import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.NonNull;
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

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_WRITE = 0;
    private static final int REQUEST_ARTICLE = 1;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Button mLogoutButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        endSplash();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Toast.makeText(getApplicationContext(), user.getEmail() + "님 안녕하세요!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
            }
        };

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mLogoutButton = (Button) findViewById(R.id.test_logoutbutton);
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseAuth.signOut();
            }
        });

        findViewById(R.id.test_writebutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WriteActivity.class);
                startActivityForResult(intent, REQUEST_WRITE);
            }
        });

        findViewById(R.id.test_articlebutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);
                startActivityForResult(intent, REQUEST_ARTICLE);
            }
        });
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

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_newsfeed:
                    Toast.makeText(MainActivity.this, "뉴스피드", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.navigation_mymenu:
                    Toast.makeText(MainActivity.this, "마이메뉴", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.navigation_statistic:
                    Toast.makeText(MainActivity.this, "통계", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.navigation_option:
                    Toast.makeText(MainActivity.this, "옵션", Toast.LENGTH_SHORT).show();
                    return true;
            }
            return false;
        }

    };

    private void endSplash() { // splash 이미지로 앱이 시작하여 2초 유지 후 본래 테마로 복귀
        SystemClock.sleep(2000);
        setTheme(R.style.AppTheme);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_WRITE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(MainActivity.this, "게시글을 성공적으로 작성하였습니다.", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == REQUEST_ARTICLE) {
            if (resultCode == RESULT_OK) {
            }
        }
    }


}