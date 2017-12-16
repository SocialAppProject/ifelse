package com.socialappproject.ifelse;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by junseon on 2017. 12. 13..
 */

public class VersionActivity extends AppCompatActivity {

    private TextView _new_ver_tv, _present_ver_tv, _supportSDK_tv, isRecentVersion_tv;
    private String recentVersion = "";
    private PackageInfo pInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version);

        _new_ver_tv = findViewById(R.id.new_ver_tv);
        _present_ver_tv = findViewById(R.id.present_ver_tv);
        _supportSDK_tv = findViewById(R.id.support_sdk_tv);
        isRecentVersion_tv = findViewById(R.id.isRecentVersion_tv);

        String sdk_version = "지원환경 Android 6.ver(마시멜로) 이상";
        _supportSDK_tv.setText(sdk_version);

        try {
            pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = "현재 버전: " + pInfo.versionName;

            _present_ver_tv.setText(version);

            if (recentVersion.equals(pInfo.versionName))
                isRecentVersion_tv.setText("최신 버전입니다!");
            else
                isRecentVersion_tv.setText("최신 버전이 아닙니다. 최신 버전으로 업데이트 해주세요!");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        DatabaseManager.databaseReference.child("VERSION").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                recentVersion = dataSnapshot.getValue().toString();
                _new_ver_tv.setText("최신 버전 : " + recentVersion);
                if (recentVersion.equals(pInfo.versionName))
                    isRecentVersion_tv.setText("최신 버전입니다!");
                else
                    isRecentVersion_tv.setText("최신 버전이 아닙니다. 최신 버전으로 업데이트 해주세요!");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
