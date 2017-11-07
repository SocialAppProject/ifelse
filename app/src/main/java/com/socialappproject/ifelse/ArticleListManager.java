package com.socialappproject.ifelse;

import android.content.Context;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kimjungmin on 2017. 11. 7..
 */

public class ArticleListManager {
    private static ArticleListManager articleListManager;
    private List<Article> articleList;

    public static ArticleListManager get(Context context) {
        if(articleListManager == null)
            articleListManager = new ArticleListManager(context);

        return articleListManager;
    }

    private ArticleListManager(Context context) {
        articleList = new ArrayList<>();

        DatabaseManager.databaseReference.child("ARTICLE").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Article article = dataSnapshot.getValue(Article.class);
                articleList.add(article);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                Article article = dataSnapshot.getValue(Article.class);
                for(int i = 0; i < articleList.size(); i++) {
                    if(articleList.get(i).getKey().equals(key))
                        articleList.set(i, article);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                for(int i = 0; i < articleList.size(); i++) {
                    if(articleList.get(i).getKey().equals(key))
                        articleList.remove(i);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Database Error : " + databaseError);
            }
        });
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public void clearArticleList() {
        articleList.clear();
    }

}
