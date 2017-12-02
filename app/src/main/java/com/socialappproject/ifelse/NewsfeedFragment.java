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
    private SwipeRefreshLayout swipeRefreshLayout;
    private Bundle bundle;
    private int category_num;

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

        bundle = this.getArguments();

        if(bundle != null)
            category_num = bundle.getInt("category_num");

        getArticelsByCategory(category_num);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newsfeed, container, false);

        view.findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.currentUser.getStar() >= 5) {
                    Intent intent = new Intent(getActivity(), WriteActivity.class);
                    startActivityForResult(intent, REQUEST_WRITE);
                } else {
                    Toast.makeText(getActivity(), "별 점수가 부족합니다.\n 투표를 진행해주세요.", Toast.LENGTH_LONG).show();
                }
            }
        });

        newsfeedListView = (ListView) view.findViewById(R.id.newsfeed_listview);
        customAdapter = new CustomAdapter(this.getContext(), articleList);
        newsfeedListView.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();

        newsfeedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ArticleActivity.class);
                intent.putExtra("key", articleList.get(articleList.size()-position-1).getKey());
                startActivity(intent);
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
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

    private void getArticelsByCategory(int category) {
        switch(category) {
            case 0: //음식
                articleList = ArticleListManager.get(getContext()).getFood_articleList();
                break;
            case 1: //패션
                articleList = ArticleListManager.get(getContext()).getFashion_articleList();
                break;
            case 2: //연애
                articleList = ArticleListManager.get(getContext()).getLove_articleList();
                break;
            case 3: //진로 및 학업
                articleList = ArticleListManager.get(getContext()).getStudy_articleList();
                break;
            case 4: //엔터테인먼트
                articleList = ArticleListManager.get(getContext()).getEntertainment_articleList();
                break;
            case 5: //장소
                articleList = ArticleListManager.get(getContext()).getLocation_articleList();
                break;
            case 6: //뷰티
                articleList = ArticleListManager.get(getContext()).getBeauty_articleList();
                break;
            case 7: //기타
                articleList = ArticleListManager.get(getContext()).getEtc_articleList();
                break;
        }
    }
}
