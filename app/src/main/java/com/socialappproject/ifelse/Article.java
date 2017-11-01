package com.socialappproject.ifelse;

import java.util.Date;

/**
 * Created by junseon on 2017. 10. 16..
 */

public class Article {
    private String title;
    private String description;
    private String option1;
    private String option2;
    private Boolean option1_flag;
    private Boolean option2_flag;
    private Date startTime;
    private Date endTime;
    private int target_old;
    private int target_gender;
    private int category;
    private String articleID;
    private String UserID;


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

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public int getTarget_old() {
        return target_old;
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

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setTarget_old(int target_old) {
        this.target_old = target_old;
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

    public Boolean getOption1_flag() {
        return option1_flag;
    }

    public void setOption1_flag(Boolean option1_flag) {
        this.option1_flag = option1_flag;
    }

    public Boolean getOption2_flag() {
        return option2_flag;
    }

    public void setOption2_flag(Boolean option2_flag) {
        this.option2_flag = option2_flag;
    }
}
