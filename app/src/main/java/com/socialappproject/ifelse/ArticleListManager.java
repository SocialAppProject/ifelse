package com.socialappproject.ifelse;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kimjungmin on 2017. 11. 7..
 */

public class ArticleListManager {
    private FirebaseAuth firebaseAuth;
    private String UID;

    private static ArticleListManager articleListManager;
    private List<Article> articleList;

    private List<Article> food_articleList;
    private List<Article> fashion_articleList;
    private List<Article> love_articleList;
    private List<Article> study_articleList;
    private List<Article> entertainment_articleList;
    private List<Article> location_articleList;
    private List<Article> beauty_articleList;
    private List<Article> etc_articleList;

    private List<String> written_keys;
    private List<String> voted_keys;
    private List<Article> written_articleList;
    private List<Article> voted_articleList;


    public static ArticleListManager get(Context context) {
        if (articleListManager == null)
            articleListManager = new ArticleListManager(context);

        return articleListManager;
    }

    private ArticleListManager(Context context) {
        firebaseAuth = FirebaseAuth.getInstance();
        UID = firebaseAuth.getCurrentUser().getUid();

        articleList = new ArrayList<>();
        food_articleList = new ArrayList<>();
        fashion_articleList = new ArrayList<>();
        love_articleList = new ArrayList<>();
        study_articleList = new ArrayList<>();
        entertainment_articleList = new ArrayList<>();
        location_articleList = new ArrayList<>();
        beauty_articleList = new ArrayList<>();
        etc_articleList = new ArrayList<>();

        written_keys = new ArrayList<>();
        voted_keys = new ArrayList<>();

        written_articleList = new ArrayList<>();
        voted_articleList = new ArrayList<>();

        attachListenerToWholeList();
        attachListenerToMyList();
    }

    private void attachListenerToWholeList() {
        DatabaseManager.databaseReference.child("ARTICLE").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Article article = dataSnapshot.getValue(Article.class);
                articleList.add(article);
                categorize_add(article);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Article article = dataSnapshot.getValue(Article.class);
                change(article, articleList);
                categorize_change(article);
                change(article, written_articleList);
                change(article, voted_articleList);
            }

            // Todo : 삭제시 Storage에서도 파일 삭제
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Article article = dataSnapshot.getValue(Article.class);
                remove(article, articleList);
                categorize_remove(article);
                remove(article, written_articleList);
                try {
                    DatabaseManager.databaseReference.child("USER").child(FirebaseAuth.getInstance()
                            .getCurrentUser().getUid()).child("VOTED_ARTICLE").child(article.getKey()).removeValue();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                remove(article, voted_articleList);
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

    private void attachListenerToMyList() {
        DatabaseManager.databaseReference.child("USER").child(UID).child("WRITED_ARTICLE")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        DatabaseManager.databaseReference.child("ARTICLE").child(dataSnapshot.getValue().toString())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        written_articleList.add((Article) dataSnapshot.getValue(Article.class));
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

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

        DatabaseManager.databaseReference.child("USER").child(UID).child("VOTED_ARTICLE")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        DatabaseManager.databaseReference.child("ARTICLE").child(dataSnapshot.getValue().toString())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        voted_articleList.add((Article) dataSnapshot.getValue(Article.class));
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

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
    }

    private void categorize_add(Article article) {
        switch (article.getCategory()) {
            case 0: //음식
                food_articleList.add(article);
                break;
            case 1: //패션
                fashion_articleList.add(article);
                break;
            case 2: //연애
                love_articleList.add(article);
                break;
            case 3: //진로 및 학업
                study_articleList.add(article);
                break;
            case 4: //엔터테인먼트
                entertainment_articleList.add(article);
                break;
            case 5: //장소
                location_articleList.add(article);
                break;
            case 6: //뷰티
                beauty_articleList.add(article);
                break;
            case 7: //기타
                etc_articleList.add(article);
                break;
        }
    }

    public void categorize_change(Article article) {
        switch (article.getCategory()) {
            case 0: //음식
                change(article, food_articleList);
                break;
            case 1: //패션
                change(article, fashion_articleList);
                break;
            case 2: //연애
                change(article, love_articleList);
                break;
            case 3: //진로 및 학업
                change(article, study_articleList);
                break;
            case 4: //엔터테인먼트
                change(article, entertainment_articleList);
                break;
            case 5: //장소
                change(article, location_articleList);
                break;
            case 6: //뷰티
                change(article, beauty_articleList);
                break;
            case 7: //기타
                change(article, etc_articleList);
                break;
        }
    }

    public void change(Article article, List<Article> articles) {
        for (int i = 0; i < articles.size(); i++) {
            if (articles.get(i).getKey().equals(article.getKey()))
                articles.set(i, article);
        }
    }

    public void categorize_remove(Article article) {
        switch (article.getCategory()) {
            case 0: //음식
                remove(article, food_articleList);
                break;
            case 1: //패션
                remove(article, fashion_articleList);
                break;
            case 2: //연애
                remove(article, love_articleList);
                break;
            case 3: //진로 및 학업
                remove(article, study_articleList);
                break;
            case 4: //엔터테인먼트
                remove(article, entertainment_articleList);
                break;
            case 5: //장소
                remove(article, location_articleList);
                break;
            case 6: //뷰티
                remove(article, beauty_articleList);
                break;
            case 7: //기타
                remove(article, etc_articleList);
                break;
        }
    }

    public void remove(Article article, List<Article> articles) {
        for (int i = 0; i < articles.size(); i++) {
            if (articles.get(i).getKey().equals(article.getKey()))
                articles.remove(i);
        }
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public void clearArticleList() {
        articleList.clear();
    }

    public List<Article> getFood_articleList() {
        return food_articleList;
    }

    public List<Article> getFashion_articleList() {
        return fashion_articleList;
    }

    public List<Article> getLove_articleList() {
        return love_articleList;
    }

    public List<Article> getStudy_articleList() {
        return study_articleList;
    }

    public List<Article> getEntertainment_articleList() {
        return entertainment_articleList;
    }

    public List<Article> getLocation_articleList() {
        return location_articleList;
    }

    public List<Article> getBeauty_articleList() {
        return beauty_articleList;
    }

    public List<Article> getEtc_articleList() {
        return etc_articleList;
    }

    public List<Article> getWritten_articleList() {
        return written_articleList;
    }

    public List<Article> getVoted_articleList() {
        return voted_articleList;
    }

    public boolean isWrittenArticle(String key) {
        for (int i = 0; i < written_articleList.size(); i++) {
            if (written_articleList.get(i).getKey().equals(key))
                return true;
        }
        return false;
    }
}