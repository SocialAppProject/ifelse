package com.socialappproject.ifelse;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        endSplash();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void endSplash() { // splash 이미지로 앱이 시작하여 2초 유지 후 본래 테마로 복귀
        SystemClock.sleep(2000);
        setTheme(R.style.AppTheme);
    }

}
