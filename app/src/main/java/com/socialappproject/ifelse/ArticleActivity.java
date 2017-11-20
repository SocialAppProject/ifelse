package com.socialappproject.ifelse;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by junseon on 2017. 10. 17..
 */

public class ArticleActivity extends AppCompatActivity {
    private static final String TAG = "ArticleActivity";

    private TextView title_tv;
    private EditText category_et;
    private TextView description_tv;
    private TextView option_1_tv;
    private TextView option_2_tv;
    private EditText option_1_et;
    private EditText option_2_et;
    private Button option_1_button;
    private Button option_2_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        title_tv = (TextView) findViewById(R.id.art_title_tv);
        category_et = (EditText) findViewById(R.id.art_category_et);
        description_tv = (TextView) findViewById(R.id.art_description_tv);
        option_1_tv = (TextView) findViewById(R.id.art_option_1_tv);
        option_2_tv = (TextView) findViewById(R.id.art_option_2_tv);
        option_1_et = (EditText) findViewById(R.id.art_option_1_et);
        option_2_et = (EditText) findViewById(R.id.art_option_2_et);
        option_1_button = (Button) findViewById(R.id.art_option1_vote);
        option_2_button = (Button) findViewById(R.id.art_option2_vote);



    }

}
