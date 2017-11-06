package com.socialappproject.ifelse;

import android.*;
import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by junseon on 2017. 10. 18..
 */

public class WriteActivity extends AppCompatActivity {

    private static final String TAG = "WriteActivity";

    private static final int REQUEST_CANCEL = 0;
    private static final int REQUEST_WRITE= 1;

    private static final int PERMISSIONS_REQUEST_CAMERA = 100;
    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 200;

    private Uri fileUri; // file url to store image
    private String mCurrentPhotoPath;
    private static final String IMAGE_DIRECTORY_NAME = "ifelse";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference articleRef = database.getReference("ARTICLE");

    private Article article = new Article();

    //TODO: 아무것도 입력하지 않은 칸이 있으면 경고

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        findViewById(R.id.input_option1).setBackgroundResource(R.drawable.option_plus_128);
        findViewById(R.id.input_option1).setOnClickListener(new View.OnClickListener() {
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
        findViewById(R.id.input_option2).setBackgroundResource(R.drawable.option_plus_128);
        findViewById(R.id.input_option2).setOnClickListener(new View.OnClickListener() {
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

    public void clickedOptionBtn(int which, int option_num) {
        if(which == 0) {
            Toast.makeText(WriteActivity.this, "카메라", Toast.LENGTH_SHORT).show();
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CAMERA);
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CAMERA);
                }
            } else {
                dispatchTakePictureIntent(option_num);
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
                doTakeAlbum(option_num);
            }
        }
        else {
            Toast.makeText(WriteActivity.this, "글", Toast.LENGTH_SHORT).show();
        }
    }


    public void doTakeAlbum(int option_num){
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE + option_num);

    }

    private void dispatchTakePictureIntent(int option_num) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d(TAG, "error in photoFile");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, PERMISSIONS_REQUEST_CAMERA + option_num);
            }
        }

    }

    private File createImageFile() throws IOException {

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);
        File image = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
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

        if (requestCode == PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE + 1 && resultCode == RESULT_OK && null != data) {
            findViewById(R.id.input_option1).setBackground(new BitmapDrawable(getResources(),
                    BitmapFactory.decodeFile(getPicturePath(data))));
        }
        else if (requestCode == (PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE + 2) && resultCode == RESULT_OK && null != data) {
            findViewById(R.id.input_option2).setBackground(new BitmapDrawable(getResources(),
                    BitmapFactory.decodeFile(getPicturePath(data))));
        }

        if (requestCode == PERMISSIONS_REQUEST_CAMERA + 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            findViewById(R.id.input_option1).setBackground(new BitmapDrawable(getResources(), imageBitmap));
        }
        else if (requestCode == PERMISSIONS_REQUEST_CAMERA + 2 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            findViewById(R.id.input_option2).setBackground(new BitmapDrawable(getResources(), imageBitmap));
        }
    }



    public String getPicturePath(Intent data) {

        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();

        return picturePath;
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
