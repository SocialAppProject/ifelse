package com.socialappproject.ifelse;


import android.content.ComponentCallbacks;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {

    private String[] category_hash;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Toolbar toolbar;
    private ArrayList<MyData> myDataset;

    public CategoryFragment() {
        category_hash = new String[8];

        for (int i = 0; i < 8; i++)
            category_hash[i] = "# " + Category.get().getCategory_Name_byIndex(i);
    }

    public static CategoryFragment newInstance() {
        CategoryFragment fragment = new CategoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myDataset = new ArrayList<>();

        myDataset.add(new MyData(category_hash[0], R.drawable.food));
        myDataset.add(new MyData(category_hash[1], R.drawable.fashion));
        myDataset.add(new MyData(category_hash[2], R.drawable.love));
        myDataset.add(new MyData(category_hash[3], R.drawable.study));
        myDataset.add(new MyData(category_hash[4], R.drawable.ent));
        myDataset.add(new MyData(category_hash[5], R.drawable.location));
        myDataset.add(new MyData(category_hash[6], R.drawable.beauty));
        myDataset.add(new MyData(category_hash[7], R.drawable.etc));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        mRecyclerView = view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new CategoryAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

        toolbar = view.findViewById(R.id.cat_toolbar);
        toolbar.setTitle("카테고리");
        toolbar.setTitleTextColor(Color.WHITE);

        return view;
    }
}