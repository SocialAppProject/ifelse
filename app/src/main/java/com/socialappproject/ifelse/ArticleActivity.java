package com.socialappproject.ifelse;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by junseon on 2017. 10. 17..
 */

// TODO: 댓글 기능 구현

public class ArticleActivity extends AppCompatActivity {
    private static final String TAG = "ArticleActivity";

    private String key;
    private Article article;

    private TextView description_tv;
    private TextView option_1_tv;
    private TextView option_2_tv;
    private EditText option_1_et;
    private EditText option_2_et;
    private Button option_1_button;
    private Button option_2_button;
    private ImageView option_1_iv;
    private ImageView option_2_iv;
    private EditText comment_et;
    private ImageButton comment_bt;
    private ListView comments_lv;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        update();

        description_tv = findViewById(R.id.art_description_tv);
        option_1_tv = findViewById(R.id.art_option_1_tv);
        option_2_tv = findViewById(R.id.art_option_2_tv);
        option_1_et = findViewById(R.id.art_option_1_et);
        option_2_et = findViewById(R.id.art_option_2_et);
        option_1_button = findViewById(R.id.art_option1_vote);
        option_2_button = findViewById(R.id.art_option2_vote);
        option_1_iv = findViewById(R.id.article_option_1_iv);
        option_2_iv = findViewById(R.id.article_option_2_iv);
        comment_et = findViewById(R.id.comment_et);
        comment_bt = findViewById(R.id.comment_bt);
        comments_lv = findViewById(R.id.comments_lv);
        toolbar = findViewById(R.id.art_toolbar);
        setSupportActionBar(toolbar);

        commentList = new ArrayList<>();

        commentAdapter = new CommentAdapter(this, commentList);
        comments_lv.setAdapter(commentAdapter);
        commentAdapter.notifyDataSetChanged();

        comment_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comment comment = new Comment();
                comment.setName(MainActivity.currentUser.getName());
                comment.setText(comment_et.getText().toString());
                DatabaseManager.databaseReference.child("ARTICLE").child(key).child("Comment").push()
                        .setValue(comment);
                comment_et.setText("");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(comment_et.getWindowToken(), 0);
            }
        });

        DatabaseManager.databaseReference.child("ARTICLE").child(key).child("Comment").
                addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        commentList.add(dataSnapshot.getValue(Comment.class));
                        commentAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (ArticleListManager.get(getApplicationContext()).isWrittenArticle(article.getKey()))
            getMenuInflater().inflate(R.menu.article_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_article:
                removeArticle();
                break;

        }
        return true;
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
        if(article == null)
            return;

        toolbar.setTitle("[ " + Category.get().getCategory_Name_byIndex(article.getCategory()) + " ]   "
         + article.getTitle());
        toolbar.setTitleTextColor(Color.WHITE);

        description_tv.setText(article.getDescription());
        if (article.getOption1_flag() == 1) {
            option_1_iv.setVisibility(View.VISIBLE);
            option_1_tv.setVisibility(View.INVISIBLE);
            Activity activity = ArticleActivity.this;
            if(activity.isFinishing())
                return;
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
            Activity activity = ArticleActivity.this;
            if(activity.isFinishing())
                return;
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
                        option_1_button.setVisibility(View.INVISIBLE);
                        option_2_button.setVisibility(View.INVISIBLE);
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
                        option_1_button.setVisibility(View.INVISIBLE);
                        option_2_button.setVisibility(View.INVISIBLE);
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
                        DatabaseManager.databaseReference.child("USER").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("VOTED_ARTICLE").child(article.getKey()).setValue(article.getKey());
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
//Todo : 삭제시 스토리지에서 사진도 삭제하는거 의논해서 삭제
    private void removeArticle() {
        try {
            DatabaseManager.databaseReference.child("ARTICLE").child(article.getKey()).removeValue();
            DatabaseManager.databaseReference.child("USER").child(FirebaseAuth.getInstance()
                    .getCurrentUser().getUid()).child("WRITED_ARTICLE").child(article.getKey()).removeValue();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}