package com.socialappproject.ifelse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

        TextView tv_title = (TextView) convertView.findViewById(R.id.tv_title) ;
        TextView tv_description = (TextView) convertView.findViewById(R.id.tv_description) ;

        Article article = articleList.get(getCount() - position - 1); // 최신순 정렬을 위해

        tv_title.setText(article.getTitle());
        tv_description.setText(article.getDescription());

        return convertView;
    }
}
