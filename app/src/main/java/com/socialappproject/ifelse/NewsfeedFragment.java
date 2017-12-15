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
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class NewsfeedFragment extends Fragment {
    private static final int REQUEST_WRITE = 0;
    private static final int REQUEST_ARTICLE = 1;

    private ListView newsfeedListView;
    private CustomAdapter customAdapter;
    private List<Article> articleList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Toolbar toolbar;
    private Bundle bundle;
    public static int category_num;

    public NewsfeedFragment() {

    }

    public static NewsfeedFragment newInstance() {
        NewsfeedFragment fragment = new NewsfeedFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bundle = this.getArguments();

        if (bundle != null)
            category_num = bundle.getInt("category_num");

        getArticlesByCategory(category_num);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.newsfeed_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by_time:
                Comparator<Article> timeComparator = new Comparator<Article>() {
                    @Override
                    public int compare(Article o1, Article o2) {
                        return o1.getTime().compareTo(o2.getTime());
                    }
                };

                Collections.sort(articleList, timeComparator);
                customAdapter.notifyDataSetChanged();
                break;

            case R.id.sort_by_close:
                Comparator<Article> closeComparator = new Comparator<Article>() {
                    @Override
                    public int compare(Article o1, Article o2) {
                        double rate1 = Math.abs(1 - ((o1.getOption1_num() + 0.0001f) / (o1.getOption2_num() + 0.0001f)));
                        double rate2 = Math.abs(1 - ((o2.getOption1_num() + 0.0001f) / (o2.getOption2_num() + 0.0001f)));

                        if (rate1 > rate2)
                            return -1;
                        else if (rate1 == rate2) {
                            if ((o1.getOption1_num() + o1.getOption2_num() >= (o2.getOption1_num() + o2.getOption2_num())))
                                return 1;
                            else
                                return -1;
                        } else
                            return 1;
                    }
                };

                Collections.sort(articleList, closeComparator);
                customAdapter.notifyDataSetChanged();
                break;

            case R.id.sort_by_votenum:
                Comparator<Article> voteNumComparator = new Comparator<Article>() {
                    @Override
                    public int compare(Article o1, Article o2) {
                        int num1 = o1.getOption1_num() + o1.getOption2_num();
                        int num2 = o2.getOption1_num() + o2.getOption2_num();

                        if (num1 > num2)
                            return 1;
                        else if (num1 == num2)
                            return 0;
                        else
                            return -1;
                    }
                };

                Collections.sort(articleList, voteNumComparator);
                customAdapter.notifyDataSetChanged();
                break;
        }

        customAdapter = new CustomAdapter(this.getContext(), articleList);
        newsfeedListView.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newsfeed, container, false);

        view.findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.currentUser.getStar() >= 5) {
                    Intent intent = new Intent(getActivity(), WriteActivity.class);
                    startActivityForResult(intent, REQUEST_WRITE);
                } else {
                    Toast.makeText(getActivity(), "별 점수가 부족합니다.\n 투표를 진행해주세요.", Toast.LENGTH_LONG).show();
                }
            }
        });

        newsfeedListView = view.findViewById(R.id.newsfeed_listview);
        customAdapter = new CustomAdapter(this.getContext(), articleList);
        newsfeedListView.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();

        newsfeedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ArticleActivity.class);
                intent.putExtra("key", articleList.get(articleList.size() - position - 1).getKey());
                startActivity(intent);
            }
        });

        swipeRefreshLayout = view.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                customAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        setHasOptionsMenu(true);
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(Category.get().getCategory_Name_byIndex(category_num));
        toolbar.setTitleTextColor(Color.WHITE);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

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

    private void getArticlesByCategory(int category) {
        switch (category) {
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
