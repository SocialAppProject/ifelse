package com.socialappproject.ifelse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

        TextView title_tv = (TextView) convertView.findViewById(R.id.title_tv);
        TextView description_tv = (TextView) convertView.findViewById(R.id.description_tv);
        TextView option_1_tv = (TextView) convertView.findViewById(R.id.option_1_tv);
        TextView option_2_tv = (TextView) convertView.findViewById(R.id.option_2_tv);
        ImageView option_1_iv = (ImageView) convertView.findViewById(R.id.option_1_iv);
        ImageView option_2_iv = (ImageView) convertView.findViewById(R.id.option_2_iv);

        Article article = articleList.get(getCount() - position - 1); // 최신순 정렬을 위해

        title_tv.setText(article.getTitle());
        description_tv.setText(article.getDescription());
        if(article.getOption1_flag() == 1)
            option_1_iv.setColorFilter(33);
        else
            option_1_tv.setText(article.getOption1());

        if(article.getOption2_flag() == 1)
            option_2_iv.setColorFilter(33);
        else
            option_2_tv.setText(article.getOption2());

        return convertView;
    }
}