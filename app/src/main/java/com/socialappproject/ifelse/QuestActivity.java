package com.socialappproject.ifelse;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by junseon on 2017. 12. 13..
 */

public class QuestActivity extends AppCompatActivity {

    private String help = "문의사항이 있으시면 언제든 연락해 주시기 바랍니다";
    private String ourEmail = "ifelsedevelop@gmail.com";
    private TextView help_tv;
    private TextView email_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest);

        help_tv = findViewById(R.id.help_tv);
        help_tv.setText(help);

        email_tv = findViewById(R.id.ouremail_tv);
        email_tv.setText(ourEmail);
    }
}
