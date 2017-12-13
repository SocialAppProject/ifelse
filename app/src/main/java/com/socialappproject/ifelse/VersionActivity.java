package com.socialappproject.ifelse;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by junseon on 2017. 12. 13..
 */

public class VersionActivity extends AppCompatActivity {

    TextView _new_ver_tv, _present_ver_tv, _supportSDK_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version);

        _new_ver_tv = (TextView) findViewById(R.id.new_ver_tv);
        _present_ver_tv = (TextView) findViewById(R.id.present_ver_tv);
        _supportSDK_tv = (TextView) findViewById(R.id.support_sdk_tv);

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = "최신 버전: " + pInfo.versionName;
            _new_ver_tv.setText(version);

            String sdk_version = "지원환경 Android 6.ver(마시멜로) 이상";
            _supportSDK_tv.setText(sdk_version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


    }
}
