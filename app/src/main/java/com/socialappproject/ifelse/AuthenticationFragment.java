package com.socialappproject.ifelse;

import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 */
public class AuthenticationFragment extends PreferenceFragmentCompat {

    private Preference email_pf;
    private Preference name_pf;
    private Preference gender_pf;
    private Preference age_pf;
    private Preference star_pf;
    private Preference logout_pf;
    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

    private String star = "";
    private String old = "";
    private String gender = "";

    public AuthenticationFragment() {
    }

    public static AuthenticationFragment newInstance() {
        AuthenticationFragment fragment = new AuthenticationFragment();
        if (MainActivity.currentUser != null) {
            fragment.setStar(MainActivity.currentUser.getStar() + "개");
            fragment.setOld(MainActivity.currentUser.getOld() + "세");
            fragment.setGender((MainActivity.currentUser.getGender() == 0) ? "여성" : "남성");
        }
        return fragment;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preference_authentication);

        email_pf = findPreference("email");
        name_pf = findPreference("name");
        gender_pf = findPreference("gender");
        age_pf = findPreference("age");
        star_pf = findPreference("star");
        logout_pf = findPreference("logout");

        if(MainActivity.currentUser == null)
            Toast.makeText(getContext(), "네트워크 오류", Toast.LENGTH_SHORT).show();
        else {
            email_pf.setSummary(MainActivity.currentUser.getEmail());
            name_pf.setSummary(MainActivity.currentUser.getName());
            gender_pf.setSummary(gender);
            age_pf.setSummary(old);
            star_pf.setSummary(star);
            star_pf.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Toast.makeText(getActivity(), "회원가입시 별 50개가 지급됩니다.\n투표를 하면 별 1개를 획득할 수 있습니다.\n게시글 작성을 위해선 별 5개가 필요합니다.", Toast.LENGTH_LONG).show();
                    return false;
                }
            });
            logout_pf.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("로그아웃")
                            .setMessage("로그아웃을 하면 앱을 재 시동해야 합니다.\n로그아웃 하시겠습니까?")
                            .setCancelable(true)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    mFirebaseAuth.signOut();
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.cancel();
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return false;
                }
            });
        }
    }

    public void setStar(String star) {
        this.star = star;
    }

    public void setOld(String old) {
        this.old = old;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
