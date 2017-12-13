package com.socialappproject.ifelse;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import static android.app.Activity.RESULT_OK;

public class SettingFragment extends Fragment implements View.OnClickListener { // 계정, 앱정보, 개발자정보

    private static final String TAG = "SettingFragment";
    private static final int REQUEST_AUTHEN = 0;

    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

    TextView _authen_tv, _version_tv, _notice_tv, _quest_tv;

    public SettingFragment() {
    }


    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        _authen_tv = (TextView) getActivity().findViewById(R.id.authen_tv);
        _authen_tv.setOnClickListener(this);
        _version_tv = (TextView) getActivity().findViewById(R.id.version_tv);
        _version_tv.setOnClickListener(this);
        _notice_tv = (TextView) getActivity().findViewById(R.id.notice_tv);
        _notice_tv.setOnClickListener(this);
        _quest_tv = (TextView) getActivity().findViewById(R.id.quest_tv);
        _quest_tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.authen_tv:
                Intent intent_authen = new Intent(getContext(), AuthenActivity.class);
                startActivity(intent_authen);
                break;
            case R.id.version_tv:Intent intent_version = new Intent(getContext(), VersionActivity.class);
                startActivity(intent_version);
                break;
            case R.id.notice_tv:Intent intent_notice = new Intent(getContext(), NoticeActivity.class);
                startActivity(intent_notice);
                break;
            case R.id.quest_tv:Intent intent_quest = new Intent(getContext(), QuestActivity.class);
                startActivity(intent_quest);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_AUTHEN && resultCode == RESULT_OK) {

        }
    }
}
