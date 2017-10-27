package com.socialappproject.ifelse;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

/**
 * Created by junseon on 2017. 10. 18..
 */

public class WriteActivity extends AppCompatActivity {
    private static final String TAG = "ArticleActivity";
    private static final int REQUEST_CANCEL = 0;
    private static final int REQUEST_WRITE= 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        findViewById(R.id.input_option1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(WriteActivity.this);
                dialog.setItems(R.array.option_ary, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0) {
                            Toast.makeText(WriteActivity.this, "사진", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(WriteActivity.this, "글", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).show();


            }
        });
        findViewById(R.id.input_option2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(WriteActivity.this);
                dialog.setItems(R.array.option_ary, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0) {
                            Toast.makeText(WriteActivity.this, "사진", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(WriteActivity.this, "글", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).show();

            }
        });


        findViewById(R.id.check_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CheckBox) findViewById(R.id.check_all)).isChecked()) {
                    findViewById(R.id.check_men).setEnabled(false);
                    findViewById(R.id.check_women).setEnabled(false);
                } else {
                    findViewById(R.id.check_men).setEnabled(true);
                    findViewById(R.id.check_women).setEnabled(true);
                }
            }
        });

        findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivityForResult(intent, REQUEST_CANCEL);
            }
        });
        findViewById(R.id.write_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivityForResult(intent, REQUEST_WRITE);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CANCEL) {
            if (resultCode == RESULT_OK) {
            }
        }
        else if (requestCode == REQUEST_WRITE) {
            if (resultCode == RESULT_OK) {
            }
        }
    }


}
