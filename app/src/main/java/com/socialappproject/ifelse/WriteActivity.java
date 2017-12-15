package com.socialappproject.ifelse;

import android.*;
import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.InjectView;

/**
 * Created by junseon on 2017. 10. 18..
 */

public class WriteActivity extends AppCompatActivity {

    private static final String TAG = "WriteActivity";

    private static final int REQUEST_CANCEL = 0;
    private static final int REQUEST_WRITE = 1;
    private static final int PERMISSIONS_REQUEST_CAMERA = 100;
    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 200;

    private Uri fileUri; // file url to store image
    private String mCurrentPhotoPath;
    private static final String IMAGE_DIRECTORY_NAME = "ifelse";

    private FirebaseAuth mFirebaseAuth;

    private Article article = new Article();

    private static final DateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");

    private Uri uri_option1;
    private Uri uri_option2;

    private Bitmap bitmap_option1;
    private Bitmap bitmap_option2;

    private int flag1 = 0;
    private int flag2 = 0; // 1-카메라, 2-갤러리

    Spinner _spinner;
    TextView _option1, _option2;
    EditText _title, _description;
    RadioGroup _radioGroup;
    RangeSeekBar _old;
    ProgressDialog progressDialog;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        _title = (EditText) findViewById(R.id.input_title);
        _description = (EditText) findViewById(R.id.input_description);

        _option1 = (TextView) findViewById(R.id.input_option1);
        _option2 = (TextView) findViewById(R.id.input_option2);

        _spinner = (Spinner) findViewById(R.id.spin_category);
        _radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        _old = (RangeSeekBar) findViewById(R.id.old_seekBar);

        toolbar = findViewById(R.id.write_toolbar);

        toolbar.setTitle("게시글 작성");
        toolbar.setTitleTextColor(Color.WHITE);
        mFirebaseAuth = FirebaseAuth.getInstance();

        // Application of the Array to the Spinner
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.category_ary));
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _spinner.setAdapter(spinnerArrayAdapter);
        _spinner.setSelection(NewsfeedFragment.category_num);

        _option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(WriteActivity.this);
                dialog.setItems(R.array.option_ary, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        clickedOptionBtn(which, 1);
                    }
                }).show();


            }
        });

        _option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(WriteActivity.this);
                dialog.setItems(R.array.option_ary, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        clickedOptionBtn(which, 2);
                    }
                }).show();

            }
        });

        findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.write_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (_title.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "제목을 입력해주세요.", Toast.LENGTH_LONG).show();
                } else if (_description.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "내용을 입력해주세요.", Toast.LENGTH_LONG).show();
                } else if (_radioGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "성별을 체크해주세요.", Toast.LENGTH_LONG).show();
                } else if (article.getOption1_flag() == 0) {
                    Toast.makeText(getApplicationContext(), "첫번째 선택지를 채워주세요.", Toast.LENGTH_LONG).show();
                } else if (article.getOption2_flag() == 0) {
                    Toast.makeText(getApplicationContext(), "두번째 선택지를 채워주세요.", Toast.LENGTH_LONG).show();
                } else {

                    progressDialog = new ProgressDialog(WriteActivity.this);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setMessage("업로드중..");
                    progressDialog.show();

                    final DatabaseReference articleRef = DatabaseManager.databaseReference.child("ARTICLE").push();

                    Log.d(TAG, "satisfied conditions");

                    article.setTitle(_title.getText().toString().trim());
                    article.setDescription(_description.getText().toString().trim());
                    article.setCategory(_spinner.getSelectedItemPosition());
                    article.setUserID(mFirebaseAuth.getCurrentUser().getEmail());
                    article.setArticleID("test");
                    article.setTarget_min_old((int) _old.getSelectedMinValue());
                    article.setTarget_max_old((int) _old.getSelectedMaxValue());
                    Calendar.getInstance().getTimeInMillis();
                    Date currentTime = Calendar.getInstance().getTime();
                    article.setTime(sdf.format(currentTime));
                    article.setKey(articleRef.getKey());

                    if (article.getOption1_flag() == 1) {
                        article.setOption1("null");
                        StorageReference filePath = StorageManager.storageReference.child("Images").child(articleRef.getKey()).child("option_1");
                        if (flag1 == 1) {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap_option1.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] data = baos.toByteArray();

                            UploadTask uploadTask = filePath.putBytes(data);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    String url = taskSnapshot.getDownloadUrl().toString();
                                    DatabaseManager.databaseReference.child("ARTICLE").child(article.getKey()).child("option1").setValue(url);
                                }
                            });
                        } else if (flag1 == 2) {
                            filePath.putFile(uri_option1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    String url = taskSnapshot.getDownloadUrl().toString();
                                    DatabaseManager.databaseReference.child("ARTICLE").child(article.getKey()).child("option1").setValue(url);
                                }
                            });
                        }
                    } else if (article.getOption1_flag() == 2)
                        article.setOption1(_option1.getText().toString().trim());

                    if (article.getOption2_flag() == 1) {
                        article.setOption2("null");
                        StorageReference filePath = StorageManager.storageReference.child("Images").child(articleRef.getKey()).child("option_2");
                        if (flag2 == 1) {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap_option1.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] data = baos.toByteArray();

                            UploadTask uploadTask = filePath.putBytes(data);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    String url = taskSnapshot.getDownloadUrl().toString();
                                    DatabaseManager.databaseReference.child("ARTICLE").child(article.getKey()).child("option2").setValue(url);
                                }
                            });
                        } else if(flag2 == 2) {
                            filePath.putFile(uri_option2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    String url = taskSnapshot.getDownloadUrl().toString();
                                    DatabaseManager.databaseReference.child("ARTICLE").child(article.getKey()).child("option2").setValue(url);
                                }
                            });
                        }
                    } else if (article.getOption2_flag() == 2)
                        article.setOption2(_option2.getText().toString().trim());

                    articleRef.setValue(article);

                    MainActivity.currentUser.setStar(MainActivity.currentUser.getStar() - 5);
                    DatabaseManager.databaseReference.child("USER").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("star").setValue(MainActivity.currentUser.getStar());
                    DatabaseManager.databaseReference.child("USER").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("WRITED_ARTICLE").child(article.getKey()).setValue(article.getKey());

                    progressDialog.dismiss();
                    finish();
                }
            }
        });

        _radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = (RadioButton) findViewById(i);

                int gender;

                switch (i) {
                    case R.id.gender_all:
                        gender = 2;
                        break;
                    case R.id.gender_men:
                        gender = 1;
                        break;
                    case R.id.gender_women:
                        gender = 0;
                        break;
                    default:
                        gender = -1;
                        break;
                }
                article.setTarget_gender(gender); // sssss
                Toast.makeText(getApplicationContext(), "" + gender + radioButton.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    public void clickedOptionBtn(int which, final int option_num) {

        if (option_num == 1) {
            _option1.setText("");
            _option1.setBackgroundResource(R.drawable.ic_add_24dp);
            article.setOption1_flag(0);
        } else if (option_num == 2) {
            _option2.setText("");
            _option2.setBackgroundResource(R.drawable.ic_add_24dp);
            article.setOption2_flag(0);
        }

        if (which == 0) {
            Toast.makeText(WriteActivity.this, "카메라", Toast.LENGTH_SHORT).show();
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CAMERA);
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CAMERA);
                }
            } else {
                if (option_num == 1) {
                    article.setOption1_flag(1);
                    flag1 = 1;
                } else if (option_num == 2) {
                    article.setOption2_flag(1);
                    flag2 = 1;
                }
                dispatchTakePictureIntent(option_num);
            }
        } else if (which == 1) {
            Toast.makeText(WriteActivity.this, "사진", Toast.LENGTH_SHORT).show();
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                } else {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
            } else {
                if (option_num == 1) {
                    article.setOption1_flag(1);
                    flag1 = 2;
                } else if (option_num == 2) {
                    article.setOption2_flag(1);
                    flag2 = 2;
                }
                doTakeAlbum(option_num);
            }
        } else {
            Toast.makeText(WriteActivity.this, "글", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder optionTextDialog = new AlertDialog.Builder(WriteActivity.this);

            optionTextDialog.setTitle("텍스트를 써주세요.");       // 제목 설정

            // EditText 삽입하기
            final EditText editText = new EditText(WriteActivity.this);
            optionTextDialog.setView(editText);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);

            // 확인 버튼 설정
            optionTextDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // Text 값 받아서 로그 남기기
                    String value = editText.getText().toString();
                    if (option_num == 1) {
                        _option1.setBackgroundResource(0);
                        _option1.setText(value);
                        article.setOption1_flag(2);
                    } else if (option_num == 2) {
                        _option2.setBackgroundResource(0);
                        _option2.setText(value);
                        article.setOption2_flag(2);
                    }
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    dialog.dismiss();     //닫기
                    // Event
                }
            });

            // 취소 버튼 설정
            optionTextDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.v(TAG, "No Btn Click");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    dialog.dismiss();     //닫기
                    // Event
                }
            });

            optionTextDialog.show();


        }
    }


    public void doTakeAlbum(int option_num) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE + option_num);

    }

    private void dispatchTakePictureIntent(int option_num) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, PERMISSIONS_REQUEST_CAMERA + option_num);

            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    article.setOption1_flag(1);
                    dispatchTakePictureIntent(1);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "사용을 원하신다면, 앱 사용 권한을 허가해주시기 바랍니다.", Toast.LENGTH_LONG).show();
                    finish();

                }
                return;
            }

            case PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    article.setOption1_flag(1);
                    doTakeAlbum(1);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    Toast.makeText(this, "사용을 원하신다면, 앱 사용 권한을 허가해주시기 바랍니다.", Toast.LENGTH_LONG).show();
                    finish();

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CANCEL)
            Toast.makeText(this, "취소하셨습니다.", Toast.LENGTH_LONG).show();
        else if (requestCode == REQUEST_WRITE)
            Toast.makeText(this, "성공적으로 글을 작성하셨습니다.", Toast.LENGTH_LONG).show();

        if (requestCode == PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE + 1 && resultCode == RESULT_OK && null != data) {
            uri_option1 = data.getData();
            _option1.setBackground(new BitmapDrawable(getResources(),
                    BitmapFactory.decodeFile(getPicturePath(data))));
        } else if (requestCode == (PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE + 2) && resultCode == RESULT_OK && null != data) {
            uri_option2 = data.getData();
            _option2.setBackground(new BitmapDrawable(getResources(),
                    BitmapFactory.decodeFile(getPicturePath(data))));
        }

        if (requestCode == (PERMISSIONS_REQUEST_CAMERA + 1) && resultCode == RESULT_OK && null != data) {
            Bundle extras = data.getExtras();
            bitmap_option1 = (Bitmap) extras.get("data");
            Drawable drawable = new BitmapDrawable(getResources(), bitmap_option1);
            _option1.setBackground(drawable);

        } else if (requestCode == (PERMISSIONS_REQUEST_CAMERA + 2) && resultCode == RESULT_OK && null != data) {
            Bundle extras = data.getExtras();
            bitmap_option2 = (Bitmap) extras.get("data");
            Drawable drawable = new BitmapDrawable(getResources(), bitmap_option2);
            _option2.setBackground(drawable);
        }
    }


    public String getPicturePath(Intent data) {

        Uri selectedImage = data.getData();

        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);

        Log.d(TAG, picturePath);


        cursor.close();

        return picturePath;
    }
}