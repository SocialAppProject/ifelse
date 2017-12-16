package com.socialappproject.ifelse;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.socialappproject.ifelse.MainActivity.currentUser;

public class MymenuFragment extends Fragment {
    private static final int REQUEST_WRITE = 0;
    private static final int REQUEST_ARTICLE = 1;

    private int flag;

    private ListView mymenuListView;
    private CustomAdapter customAdapter;
    private List<Article> articleList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Toolbar toolbar;

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.mymenu_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.written_articles:
                toolbar.setTitle("내가 작성한 게시물");
                flag = 0;
                setArticleList(flag);
                customAdapter = new CustomAdapter(getContext(), articleList);
                mymenuListView.setAdapter(customAdapter);
                customAdapter.notifyDataSetChanged();
                break;

            case R.id.voted_articles:
                toolbar.setTitle("내가 투표한 게시물");
                flag = 1;
                setArticleList(flag);
                customAdapter = new CustomAdapter(getContext(), articleList);
                mymenuListView.setAdapter(customAdapter);
                customAdapter.notifyDataSetChanged();
                break;
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        customAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mymenu, container, false);

        mymenuListView = view.findViewById(R.id.mymenu_listview);
        customAdapter = new CustomAdapter(this.getContext(), articleList);
        mymenuListView.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();

        mymenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ArticleActivity.class);
                intent.putExtra("key", articleList.get(articleList.size()-position-1).getKey());
                startActivity(intent);
            }
        });

        swipeRefreshLayout = view.findViewById(R.id.my_swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                customAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        setHasOptionsMenu(true);
        toolbar = view.findViewById(R.id.my_toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("내가 작성한 게시물");

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
