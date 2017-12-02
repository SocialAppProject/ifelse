package com.socialappproject.ifelse;

/**
 * Created by Kimjungmin on 2017. 10. 16..
 */

public class User {
    private String email; // 이메일
    private String name; // 이름
    private int gender; // 성별 0-여자 1-남자 2-양성
    private int old; // 나이
    private int star; //별 점수

    public User(String email, String name, int gender, int old, int star) {
        this.email = email;
        this.name = name;
        this.gender = gender;
        this.old = old;
        this.star = star;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public int getGender() {
        return gender;
    }

    public int getOld() {
        return old;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }
}
