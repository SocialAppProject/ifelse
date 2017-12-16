package com.socialappproject.ifelse;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by junseon on 2017. 12. 13..
 */

public class VersionActivity extends AppCompatActivity {

    TextView _new_ver_tv, _present_ver_tv, _supportSDK_tv, isRecentVersion_tv;
    private String recentVersion = "1.0.0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version);

        _new_ver_tv = (TextView) findViewById(R.id.new_ver_tv);
        _present_ver_tv = (TextView) findViewById(R.id.present_ver_tv);
        _supportSDK_tv = (TextView) findViewById(R.id.support_sdk_tv);
        isRecentVersion_tv = findViewById(R.id.isRecentVersion_tv);

        _new_ver_tv.setText("최신 버전 : " + recentVersion);
        String sdk_version = "지원환경 Android 6.ver(마시멜로) 이상";
        _supportSDK_tv.setText(sdk_version);

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = "현재 버전: " + pInfo.versionName;

            _present_ver_tv.setText(version);

            if(recentVersion.equals(pInfo.versionName))
                isRecentVersion_tv.setText("최신 버전입니다!");
            else
                isRecentVersion_tv.setText("최신 버전이 아닙니다. 최신 버전으로 업데이트 해주세요!");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


    }
}
