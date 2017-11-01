package com.socialappproject.ifelse;

import android.*;
import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresPermission;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by junseon on 2017. 10. 18..
 */

public class WriteActivity extends AppCompatActivity {

    private static final String TAG = "ArticleActivity";

    private static final int REQUEST_CANCEL = 0;
    private static final int REQUEST_WRITE= 1;

    private static final int PERMISSIONS_REQUEST_CAMERA = 100;
    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 200;

    private Uri fileUri; // file url to store image
    private static final String IMAGE_DIRECTORY_NAME = "ifelse";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference articleRef = database.getReference("ARTICLE");

    //TODO: 아무것도 입력하지 않은 칸이 있으면 경고

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        findViewById(R.id.input_option1).setBackgroundResource(R.drawable.option_plus_128);
        findViewById(R.id.input_option2).setBackgroundResource(R.drawable.option_plus_128);

        findViewById(R.id.input_option1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(WriteActivity.this);
                dialog.setItems(R.array.option_ary, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0) {
                            Toast.makeText(WriteActivity.this, "카메라", Toast.LENGTH_SHORT).show();
                            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                                    requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CAMERA);
                                } else {
                                    requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CAMERA);
                                }
                            } else {
                                doCaptureImage();
                            }
                        }
                        else if(which == 1) {
                            Toast.makeText(WriteActivity.this, "사진", Toast.LENGTH_SHORT).show();
                            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                                } else {
                                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                                }
                            } else {
                                doTakeAlbum();
                            }
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


    public void doTakeAlbum(){
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

    }

    private void doCaptureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, getOutputMediaFile());
        } else {
            File file = new File(getOutputMediaFile().getPath());
            Uri photoUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
            startActivityForResult(intent, PERMISSIONS_REQUEST_CAMERA);
        }
    }

    private static File getOutputMediaFile() {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    doCaptureImage();

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
                    doTakeAlbum();

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
        if (requestCode == REQUEST_CANCEL) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(WriteActivity.this, "취소하셨습니다.", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == REQUEST_WRITE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(WriteActivity.this, "성공적으로 새 글이 작성되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    private void writeNewArticle(String title, String Description,
                                 String option1, String option2,
                                 Boolean option1_flag, Boolean option2_flag,
                                 Date startTime, Date endTime,
                                 int target_old, int target_gender, int category,
                                 String articleID, String userID) {
        Article article = new Article();

        articleRef.push().setValue(article);
    }


}
