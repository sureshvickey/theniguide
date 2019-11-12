package com.thenikaran.guide.Adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thenikaran.guide.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.thenikaran.guide.MVP.CategoryListResponse;


/**
 * Created by ubantu on 3/3/17.
 */
public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesViewHolder> {
    Context context;
    List<CategoryListResponse> categoryListResponses;
    List<CategoryListResponse> categoryListResponses1;

    public CategoriesAdapter(Context context, List<CategoryListResponse> categoryListResponses) {
        this.context = context;
        this.categoryListResponses = categoryListResponses;

        this.categoryListResponses1 = new ArrayList<CategoryListResponse>();
        this.categoryListResponses1.addAll(categoryListResponses);
    }

    @Override
    public CategoriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_list_items, null);
        CategoriesViewHolder categoriesViewHolder = new CategoriesViewHolder(context,view);
        return categoriesViewHolder;
    }

    @Override
    public void onBindViewHolder(CategoriesViewHolder holder, int position) {
        holder.catName.setText(categoryListResponses.get(position).getCategoryName());
        String temp=categoryListResponses.get(position).getCategoryImage().replaceAll(" ", "%20");
        Picasso.with(context)
                .load(temp)
                .placeholder(R.drawable.defaultimage)
                .error(R.drawable.defaulterrorcategoryimage)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return categoryListResponses.size();
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        categoryListResponses.clear();
        if (charText.length() == 0) {
            categoryListResponses.addAll(categoryListResponses1);
        } else {
            for (CategoryListResponse wp : categoryListResponses1) {
                if (wp.getCategoryName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    categoryListResponses.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
