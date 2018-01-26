package com.socialappproject.ifelse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

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

        TextView title_tv = convertView.findViewById(R.id.title_tv);
        EditText category_tv = convertView.findViewById(R.id.category_et);
        TextView description_tv = convertView.findViewById(R.id.description_tv);
        final TextView option_1_tv = convertView.findViewById(R.id.option_1_tv);
        final TextView option_2_tv = convertView.findViewById(R.id.option_2_tv);
        EditText option_1_et = convertView.findViewById(R.id.option_1_et);
        EditText option_2_et = convertView.findViewById(R.id.option_2_et);
        ImageView option_1_iv = convertView.findViewById(R.id.option_1_iv);
        ImageView option_2_iv = convertView.findViewById(R.id.option_2_iv);

        Article article = articleList.get(getCount() - position - 1); // 최신순 정렬을 위해

        if (article != null) {
            title_tv.setText(article.getTitle());
            description_tv.setText(article.getDescription());

            if (article.getOption1_flag() == 1) {
                option_1_iv.setVisibility(View.VISIBLE);
                option_1_tv.setVisibility(View.INVISIBLE);
                Glide.with(context).load(article.getOption1()).into(option_1_iv);
            } else {
                option_1_tv.setVisibility(View.VISIBLE);
                option_1_iv.setVisibility(View.INVISIBLE);
                option_1_tv.setBackground(null);
                option_1_tv.setText(article.getOption1());
            }

            if (article.getOption2_flag() == 1) {
                option_2_iv.setVisibility(View.VISIBLE);
                option_2_tv.setVisibility(View.INVISIBLE);
                Glide.with(context).load(article.getOption2()).into(option_2_iv);

            } else {
                option_2_tv.setVisibility(View.VISIBLE);
                option_2_iv.setVisibility(View.INVISIBLE);
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

            category_tv.setText(Category.get().getCategory_Name_byIndex(article.getCategory()));
        }
        return convertView;
    }
}