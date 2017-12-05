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
 * Created by Kimjungmin on 2017. 12. 5..
 */

public class CommentAdapter extends BaseAdapter {
    private Context context;
    private List<Comment> commentList;

    public CommentAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @Override
    public int getCount() {
        return commentList.size();
    }

    @Override
    public Object getItem(int i) {
        return commentList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_comment, null);
        }

        TextView name_tv = convertView.findViewById(R.id.name_tv);
        TextView text_tv = convertView.findViewById(R.id.text_tv);

        Comment comment = commentList.get(position);

        name_tv.setText(comment.getName());
        text_tv.setText(comment.getText());
        return convertView;
    }
}
