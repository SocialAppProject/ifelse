package com.socialappproject.ifelse;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by junseon on 2017. 10. 17..
 */

// TODO: 댓글 기능 구현

public class ArticleActivity extends AppCompatActivity {
    private static final String TAG = "ArticleActivity";

    private String key;
    private Article article;

    private TextView title_tv;
    private EditText category_et;
    private TextView description_tv;
    private TextView option_1_tv;
    private TextView option_2_tv;
    private EditText option_1_et;
    private EditText option_2_et;
    private Button option_1_button;
    private Button option_2_button;
    private ImageView option_1_iv;
    private ImageView option_2_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        update();

        title_tv = (TextView) findViewById(R.id.art_title_tv);
        category_et = (EditText) findViewById(R.id.art_category_et);
        description_tv = (TextView) findViewById(R.id.art_description_tv);
        option_1_tv = (TextView) findViewById(R.id.art_option_1_tv);
        option_2_tv = (TextView) findViewById(R.id.art_option_2_tv);
        option_1_et = (EditText) findViewById(R.id.art_option_1_et);
        option_2_et = (EditText) findViewById(R.id.art_option_2_et);
        option_1_button = (Button) findViewById(R.id.art_option1_vote);
        option_2_button = (Button) findViewById(R.id.art_option2_vote);
        option_1_iv = (ImageView) findViewById(R.id.article_option_1_iv);
        option_2_iv = (ImageView) findViewById(R.id.article_option_2_iv);
    }

    private void update() {
        Intent intent = getIntent();
        key = intent.getExtras().getString("key");
        getArticle();
    }

    private void getArticle() {
        DatabaseManager.databaseReference.child("ARTICLE").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                article = dataSnapshot.getValue(Article.class);
                updateView(article);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateView(final Article article) {
        title_tv.setText(article.getTitle());
        category_et.setText(Category.get().getCategory_Name_byIndex(article.getCategory()));
        description_tv.setText(article.getDescription());
        if (article.getOption1_flag() == 1) {
            option_1_iv.setVisibility(View.VISIBLE);
            option_1_tv.setVisibility(View.INVISIBLE);
            Glide.with(this).load(StorageManager.storageReference.child("Images").child(article.getKey())
                    .child("option_1")).into(option_1_iv);
        } else {
            option_1_tv.setVisibility(View.VISIBLE);
            option_1_iv.setVisibility(View.INVISIBLE);
            option_1_tv.setBackground(null);
            option_1_tv.setText(article.getOption1());
        }

        if (article.getOption2_flag() == 1) {
            option_2_iv.setVisibility(View.VISIBLE);
            option_2_tv.setVisibility(View.INVISIBLE);
            Glide.with(this).load(StorageManager.storageReference.child("Images").child(article.getKey())
                    .child("option_2")).into(option_2_iv);
        } else {
            option_2_tv.setVisibility(View.VISIBLE);
            option_2_iv.setVisibility(View.INVISIBLE);
            option_2_tv.setBackground(null);
            option_2_tv.setText(article.getOption2());
        }

        final LinearLayout.LayoutParams option1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        option1.weight = Math.max(0.1f, article.getOption1_num());

        LinearLayout.LayoutParams option2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        option2.weight = Math.max(0.1f, article.getOption2_num());

        option_1_et.setLayoutParams(option1);
        option_2_et.setLayoutParams(option2);
        option_1_et.setText("" + article.getOption1_num());
        option_2_et.setText("" + article.getOption2_num());

        DatabaseManager.databaseReference.child("USER").child("").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                child("VOTED_ARTICLE").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if(postSnapshot.getValue().toString().equals(article.getKey())) {
                        option_1_button.setEnabled(false);
                        option_2_button.setEnabled(false);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseManager.databaseReference.child("USER").child("").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                child("WRITED_ARTICLE").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if(postSnapshot.getValue().toString().equals(article.getKey())) {
                        option_1_button.setEnabled(false);
                        option_2_button.setEnabled(false);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        option_1_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show(1);
            }
        });
        option_2_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show(2);
            }
        });

        option_1_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (article.getOption1_flag() == 1) {
                    Intent intent = new Intent(getApplicationContext(), FullScreenImageViewActivity.class);
                    intent.putExtra("key", key);
                    intent.putExtra("select", 1);
                    startActivity(intent);
                }
            }
        });

        option_2_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (article.getOption2_flag() == 1) {
                    Intent intent = new Intent(getApplicationContext(), FullScreenImageViewActivity.class);
                    intent.putExtra("key", key);
                    intent.putExtra("select", 2);
                    startActivity(intent);
                }
            }
        });
    }

    private void show(final int select) {
        String title;
        String message;

        if (select == 1)
            title = "첫번째 선택지";
        else
            title = "두번째 선택지";

        message = title + "에 투표하시겠습니까?";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("투표하기");
        builder.setMessage(message);
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        int num;

                        if (select == 1) {
                            num = article.getOption1_num();
                            num += 1;
                            DatabaseManager.databaseReference.child("ARTICLE").child(article.getKey())
                                    .child("option1_num").setValue(num);
                        } else {
                            num = article.getOption2_num();
                            num += 1;
                            DatabaseManager.databaseReference.child("ARTICLE").child(article.getKey())
                                    .child("option2_num").setValue(num);
                        }
                        dialog.dismiss();

                        MainActivity.currentUser.setStar(MainActivity.currentUser.getStar() + 1);
                        DatabaseManager.databaseReference.child("USER").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("star").setValue(MainActivity.currentUser.getStar());
                        DatabaseManager.databaseReference.child("USER").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("VOTED_ARTICLE").push().setValue(article.getKey());
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }
}