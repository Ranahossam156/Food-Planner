package com.example.foodplaner.Features.Favorites.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.foodplaner.Features.ShowSpecificMeals.view.OnFavoriteClickListener;
import com.example.foodplaner.R;
import com.example.foodplaner.model.MealElement;

import java.util.ArrayList;
import java.util.List;

public class FavoriteGridAdapter extends BaseAdapter {

    private Context context;
    private List<MealElement> categoryList = new ArrayList<>();
    onFavoriteMealDetailsListener onMealClickListener;
    OnFavoriteClickListener onFavoriteClickListener;
    OnRemoveClickListener onRemoveClickListener;

    public FavoriteGridAdapter(Context context, onFavoriteMealDetailsListener onMealClickListener, OnFavoriteClickListener onFavoriteClickListener,OnRemoveClickListener onRemoveClickListener) {
        this.context = context;
        this.onMealClickListener = onMealClickListener;
        this.onFavoriteClickListener = onFavoriteClickListener;
        this.onRemoveClickListener=onRemoveClickListener;
    }
    public FavoriteGridAdapter(Context context) {
        this.context = context;
    }

    public void setCategoryList(List<MealElement> categoryList) {
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.fav_cal_item, parent, false);
            holder = new ViewHolder();
            holder.categoryImage = convertView.findViewById(R.id.fav_cal_Image);
            holder.categoryName = convertView.findViewById(R.id.fav_cal_Name);
            holder.removeImage = convertView.findViewById(R.id.removeicon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MealElement mealElement = categoryList.get(position);

        holder.categoryName.setText(mealElement.getStrMeal());
        Glide.with(context)
                .load(mealElement.getStrMealThumb())
                .into(holder.categoryImage);
        convertView.setOnClickListener(view -> onMealClickListener.getClickedMealDetails(mealElement));
        holder.removeImage.setOnClickListener(view -> {
            if (onRemoveClickListener != null) {
                onRemoveClickListener.onRemoveClicked(mealElement);
            }
        });
        //holder.heartImage.setOnClickListener(view -> onFavoriteClickListener.onFavProductClick(filteredMeal));
        return convertView;
    }

    private static class ViewHolder {
        ImageView categoryImage;
        TextView categoryName;
        TextView extraText;
        ImageView removeImage;
    }
}
