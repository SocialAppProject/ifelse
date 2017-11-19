package com.socialappproject.ifelse;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.List;

import static android.app.Activity.RESULT_OK;


public class NewsfeedFragment extends Fragment {
    private static final int REQUEST_WRITE = 0;
    private static final int REQUEST_ARTICLE = 1;

    private ListView newsfeedListView;
    private CustomAdapter customAdapter;
    private List<Article> articleList;


    public NewsfeedFragment() {
        // Required empty public constructor
    }

    public static NewsfeedFragment newInstance() {
        NewsfeedFragment fragment = new NewsfeedFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        articleList = ArticleListManager.get(getContext()).getArticleList();
        update();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newsfeed, container, false);

        view.findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WriteActivity.class);
                startActivityForResult(intent, REQUEST_WRITE);
            }
        });

        newsfeedListView = (ListView) view.findViewById(R.id.newsfeed_listview);
        customAdapter = new CustomAdapter(this.getContext(), articleList);
        newsfeedListView.setAdapter(customAdapter);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_WRITE) {
            if (resultCode == RESULT_OK) {
            }
        } else if (requestCode == REQUEST_ARTICLE) {
            if (resultCode == RESULT_OK) {
            }
        }
    }

    public void update() {
        DatabaseManager.databaseReference.child("ARTICLE").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                customAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                customAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                customAdapter.notifyDataSetChanged();
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
}
