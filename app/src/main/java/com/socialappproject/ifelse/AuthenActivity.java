package com.socialappproject.ifelse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by junseon on 2017. 12. 13..
 */

public class AuthenActivity extends AppCompatActivity {

    private static final String TAG = "AuthenActivity";

    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

    TextView _email_output, _name_output, _star_output, _gender_output, _old_output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authen);

        _email_output = (TextView) findViewById(R.id.email_output);
        _name_output = (TextView) findViewById(R.id.name_output);
        _star_output = (TextView) findViewById(R.id.star_output);
        _gender_output = (TextView) findViewById(R.id.gender_output);
        _old_output = (TextView) findViewById(R.id.old_output);


        String star = MainActivity.currentUser.getStar() + "개";
        String old = MainActivity.currentUser.getOld() + "세";
        String gender = (MainActivity.currentUser.getGender() == 0) ? "여성" : "남성";

        _email_output.setText(mFirebaseAuth.getCurrentUser().getEmail());
        _name_output.setText(MainActivity.currentUser.getName());
        _star_output.setText(star);
        _gender_output.setText(gender);
        _old_output.setText(old);




        findViewById(R.id.signOut_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                mFirebaseAuth.signOut();
                startActivity(new Intent(AuthenActivity.this, LoginActivity.class));
            }
        });

    }

}
