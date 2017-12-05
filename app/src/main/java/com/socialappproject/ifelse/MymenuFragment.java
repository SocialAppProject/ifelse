package com.socialappproject.ifelse;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.socialappproject.ifelse.MainActivity.currentUser;

public class MymenuFragment extends Fragment {
    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private static final int REQUEST_WRITE = 0;
    private static final int REQUEST_ARTICLE = 1;

    private int flag;

    private TextView info_tv;
    private ListView mymenuListView;
    private CustomAdapter customAdapter;
    private List<Article> articleList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView written_tv;
    private TextView voted_tv;

    public MymenuFragment() {

    }

    public static MymenuFragment newInstance() {
        MymenuFragment fragment = new MymenuFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flag = 0;
        setArticleList(flag);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mymenu, container, false);
        info_tv = view.findViewById(R.id.info_tv);
        info_tv.setText(currentUser.getName() + " 님의 보유 star : " + currentUser.getStar() +"개");

        mymenuListView = (ListView) view.findViewById(R.id.mymenu_listview);
        customAdapter = new CustomAdapter(this.getContext(), articleList);
        mymenuListView.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();

        written_tv = view.findViewById(R.id.written_tv);
        written_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = 0;
                setArticleList(flag);
                customAdapter = new CustomAdapter(getContext(), articleList);
                mymenuListView.setAdapter(customAdapter);
                customAdapter.notifyDataSetChanged();
            }
        });
        voted_tv = view.findViewById(R.id.voted_tv);
        voted_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = 1;
                setArticleList(flag);
                customAdapter = new CustomAdapter(getContext(), articleList);
                mymenuListView.setAdapter(customAdapter);
                customAdapter.notifyDataSetChanged();
            }
        });

        mymenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ArticleActivity.class);
                intent.putExtra("key", articleList.get(articleList.size()-position-1).getKey());
                startActivity(intent);
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.my_swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                customAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
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

    public void setArticleList(int flag) {
        switch (flag) {
            case 0: //written
                articleList = ArticleListManager.get(getContext()).getWritten_articleList();
                break;
            case 1: //voted
                articleList = ArticleListManager.get(getContext()).getVoted_articleList();
                break;
        }
    }
}
