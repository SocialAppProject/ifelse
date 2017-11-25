package com.socialappproject.ifelse;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;

import javax.microedition.khronos.opengles.GL;

/**
 * Created by Kimjungmin on 2017. 11. 7..
 */

public class CustomAdapter extends BaseAdapter {
    private Context context;
    private List<Article> articleList;

    public CustomAdapter(Context context, List<Article> articleList) {
        this.context = context;
        this.articleList = articleList;
    }

    @Override
    public int getCount() {
        return articleList.size();
    }

    @Override
    public Object getItem(int position) {
        return articleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_article, null);
        }

        TextView title_tv = (TextView) convertView.findViewById(R.id.title_tv);
        EditText category_tv = (EditText) convertView.findViewById(R.id.category_et);
        TextView description_tv = (TextView) convertView.findViewById(R.id.description_tv);
        final TextView option_1_tv = (TextView) convertView.findViewById(R.id.option_1_tv);
        final TextView option_2_tv = (TextView) convertView.findViewById(R.id.option_2_tv);
        EditText option_1_et = (EditText) convertView.findViewById(R.id.option_1_et);
        EditText option_2_et = (EditText) convertView.findViewById(R.id.option_2_et);

        Article article = articleList.get(getCount() - position - 1); // 최신순 정렬을 위해

        title_tv.setText(article.getTitle());
        description_tv.setText(article.getDescription());

        if (article.getOption1_flag() == 1) {
            option_1_tv.setText("");

            Bitmap image = decodeBase64(article.getOption1());
            option_1_tv.setBackground(new BitmapDrawable(image));
        } else {
            option_1_tv.setBackground(null);
            option_1_tv.setText(article.getOption1());
        }

        if (article.getOption2_flag() == 1) {
            option_2_tv.setText("");

            Bitmap image = decodeBase64(article.getOption2());
            option_2_tv.setBackground(new BitmapDrawable(image));
        } else {
            option_2_tv.setBackground(null);
            option_2_tv.setText(article.getOption2());
        }


        LinearLayout.LayoutParams option1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        option1.weight = Math.max(0.1f, article.getOption1_num());

        LinearLayout.LayoutParams option2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        option2.weight = Math.max(0.1f, article.getOption2_num());

        option_1_et.setLayoutParams(option1);
        option_2_et.setLayoutParams(option2);
        option_1_et.setText("" + article.getOption1_num());
        option_2_et.setText("" + article.getOption2_num());

        category_tv.setText(Category.get(context).getCategory_Name_byIndex(article.getCategory()));

        return convertView;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}