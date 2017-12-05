package com.socialappproject.ifelse;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class FullScreenImageViewActivity extends AppCompatActivity {

    private String key;
    private int select;
    private ImageView full_iv;
    private Bitmap image;
    private Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image_view);

        update();
    }

    private void update() {
        Intent intent = getIntent();
        key = intent.getExtras().getString("key");
        select = intent.getExtras().getInt("select");

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

    private void updateView(Article article) {
        full_iv = (ImageView) findViewById(R.id.full_iv);

        if (select == 1)
            Glide.with(this).load(StorageManager.storageReference.child("Images").child(article.getKey())
                    .child("option_1")).into(full_iv);
        else
            Glide.with(this).load(StorageManager.storageReference.child("Images").child(article.getKey())
                    .child("option_2")).into(full_iv);
    }
}
