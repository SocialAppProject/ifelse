package com.socialappproject.ifelse;

/**
 * Created by Kimjungmin on 2017. 10. 16..
 */

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";

    private FirebaseAuth mFirebaseAuth;

    private RadioButton femaleRadioButton;

    @InjectView(R.id.input_email)
    EditText _emailText;
    @InjectView(R.id.input_password)
    EditText _passwordText;
    @InjectView(R.id.input_name)
    EditText _nameText;
    @InjectView(R.id.genderGroup)
    RadioGroup _genderRadioGroup;
    @InjectView(R.id.oldSpinner)
    Spinner _oldSpinner;
    @InjectView(R.id.btn_signup)
    Button _signupButton;
    @InjectView(R.id.link_login)
    TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        femaleRadioButton = (RadioButton) findViewById(R.id.option_female);
        setSpinner();

        ButterKnife.inject(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void register() {
        Log.d(TAG, "Register");

        if (!validate()) {
            onRegisterFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("회원가입중...");
        progressDialog.show();

        final User user = createUser();

        String email = user.getEmail();
        String password = _passwordText.getText().toString();

        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            onRegisterSuccess();
                            DatabaseManager.databaseReference.child("USER")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
                        } else
                            onRegisterFailed();

                        progressDialog.dismiss();
                    }
                });
    }

    public void onRegisterSuccess() {
        _signupButton.setEnabled(true);
        Toast.makeText(RegisterActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK, null);
        finish();
    }

    public void onRegisterFailed() {
        _signupButton.setEnabled(true);
        Toast.makeText(RegisterActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String name = _nameText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("올바른 이메일 형식을 입력하세요");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 10) {
            _passwordText.setError("6~10자리 사이의 비밀번호를 입력하세요");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (name.isEmpty() || name.length() < 2) {
            _nameText.setError("최소 2글자 이상 입력해주세요");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        return valid;
    }

    public User createUser() {
        String email = _emailText.getText().toString();
        String name = _nameText.getText().toString();
        int old = (int) _oldSpinner.getSelectedItem();
        int gender;
        if (femaleRadioButton.isChecked())
            gender = 0;
        else
            gender = 1;

        return new User(email, name, gender, old);
    }

    private void setSpinner() {
        List age = new ArrayList<>();
        for (int i = 1; i < 101; i++)
            age.add(i);

        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(
                this, android.R.layout.simple_spinner_item, age);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        _oldSpinner = (Spinner) findViewById(R.id.oldSpinner);
        _oldSpinner.setAdapter(adapter);
    }
}