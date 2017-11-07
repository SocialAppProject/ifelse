package com.socialappproject.ifelse;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import static android.app.Activity.RESULT_OK;

public class MymenuFragment extends Fragment {
    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private static final int REQUEST_WRITE = 0;
    private static final int REQUEST_ARTICLE = 1;

    public MymenuFragment() {
        // Required empty public constructor
    }

    public static MymenuFragment newInstance() {
        MymenuFragment fragment = new MymenuFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mymenu, container, false);

        view.findViewById(R.id.test_logoutbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseAuth.signOut();
            }
        });

        view.findViewById(R.id.test_articlebutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ArticleActivity.class);
                startActivityForResult(intent, REQUEST_ARTICLE);
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
}
