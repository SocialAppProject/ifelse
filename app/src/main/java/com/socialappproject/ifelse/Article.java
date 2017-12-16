package com.socialappproject.ifelse;


/**
 * Created by junseon on 2017. 10. 16..
 */

public class Article {
    private String key;
    private String articleID;
    private String UserID;
    private String title;
    private String description;
    private String option1;
    private String option2;
    private int option1_num;
    private int option2_num;
    private int option1_flag; // 0: 없음, 1: 이미지, 2: 텍스트
    private int option2_flag;
    private String time;
    private int target_min_old;
    private int target_max_old;
    private int target_gender;
    private int category;

    public String getKey() {
        return key;
    }

    public int getOption1_num() {
        return option1_num;
    }

    public int getOption2_num() {
        return option2_num;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getOption1() {
        return option1;
    }

    public String getOption2() {
        return option2;
    }

    public String getTime() {
        return time;
    }

    public int getTarget_min_old() {
        return target_min_old;
    }

    public int getTarget_max_old() {
        return target_max_old;
    }

    public int getTarget_gender() {
        return target_gender;
    }

    public int getCategory() {
        return category;
    }

    public String getArticleID() {
        return articleID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setOption1_num(int option1_num) {
        this.option1_num = option1_num;
    }

    public void setOption2_num(int option2_num) {
        this.option2_num = option2_num;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTarget_min_old(int target_min_old) {
        this.target_min_old = target_min_old;
    }

    public void setTarget_max_old(int target_max_old) {
        this.target_max_old = target_max_old;
    }

    public void setTarget_gender(int target_gender) {
        this.target_gender = target_gender;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public void setArticleID(String articleID) {
        this.articleID = articleID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public int getOption1_flag() {
        return option1_flag;
    }

    public void setOption1_flag(int option1_flag) {
        this.option1_flag = option1_flag;
    }

    public int getOption2_flag() {
        return option2_flag;
    }

    public void setOption2_flag(int option2_flag) {
        this.option2_flag = option2_flag;
    }
}

class Comment {
    private String name;
    private String text;
    public Comment() {}

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setText(String text) {
        this.text = text;
    }
}
