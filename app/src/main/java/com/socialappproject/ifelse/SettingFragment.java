package com.socialappproject.ifelse;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class SettingFragment extends PreferenceFragmentCompat { // 계정, 앱정보, 개발자정보

    private static final String TAG = "SettingFragment";
    private static final int REQUEST_AUTHEN = 0;

    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

    TextView _authen_tv, _version_tv, _notice_tv, _quest_tv;

    private Preference authentication_pref;
    private Preference version_pref;
    private Preference quest_pref;

    public SettingFragment() {
    }


    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preference_setting);
        authentication_pref = findPreference("authentication_pref");
        authentication_pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Fragment fragment = AuthenticationFragment.newInstance();
                FragmentManager fragmentManager = MainActivity.fm;
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                return false;
            }
        });
        version_pref = findPreference("version_pref");
        version_pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent_version = new Intent(getContext(), VersionActivity.class);
                startActivity(intent_version);
                return false;
            }
        });
        quest_pref = findPreference("quest_pref");
        quest_pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent_quest = new Intent(getContext(), QuestActivity.class);
                startActivity(intent_quest);
                return false;
            }
        });
    }
}
