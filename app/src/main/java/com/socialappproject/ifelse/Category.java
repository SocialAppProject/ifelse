package com.socialappproject.ifelse;


/**
 * Created by Kimjungmin on 2017. 11. 7..
 */

public class Category {
    private static Category category;
    private boolean[] category_isOn;
    private String[] category_Name;
    private int num = 8;

    private Category() {
        category_isOn = new boolean[8];
        for (int i = 0; i < num; i++)
            category_isOn[i] = false;

        category_Name = new String[8];
        category_Name[0] = "음식";
        category_Name[1] = "패션";
        category_Name[2] = "연애";
        category_Name[3] = "진로 및 학업";
        category_Name[4] = "엔터테인먼트";
        category_Name[5] = "장소";
        category_Name[6] = "뷰티";
        category_Name[7] = "기타";
    }

    public static Category get() {
        if (category == null)
            category = new Category();

        return category;
    }

    public String getCategory_Name_byIndex(int i) {
        return category_Name[i];
    }
}
