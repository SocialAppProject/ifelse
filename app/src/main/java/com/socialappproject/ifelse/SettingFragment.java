package com.socialappproject.ifelse;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SettingFragment extends Fragment implements View.OnClickListener { // 계정, 앱정보, 개발자정보

    private static final String TAG = "SettingFragment";

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
                Toast.makeText(getContext(), "계정 관리", Toast.LENGTH_SHORT).show();
                break;
            case R.id.version_tv:
                Toast.makeText(getContext(), "버전 정보", Toast.LENGTH_SHORT).show();
                break;
            case R.id.notice_tv:
                Toast.makeText(getContext(), "공지 사항", Toast.LENGTH_SHORT).show();
                break;
            case R.id.quest_tv:
                Toast.makeText(getContext(), "문의", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
