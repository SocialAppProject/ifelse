package com.socialappproject.ifelse;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {

    private String[] category_hash;

    public CategoryFragment() {
        for(int i = 0; i < 8; i++)
            category_hash[i] = "#" + Category.get().getCategory_Name_byIndex(i);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_category, container, false);
    }

}
