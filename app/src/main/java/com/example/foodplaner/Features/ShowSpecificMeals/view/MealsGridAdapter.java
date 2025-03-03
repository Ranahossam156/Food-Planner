package com.example.foodplaner.Features.ShowSpecificMeals.view;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.foodplaner.Features.Meal_Details.view.MealDetailsFragmentDirections;
import com.example.foodplaner.R;
import com.example.foodplaner.Utils.DialogUtils;
import com.example.foodplaner.model.Category;
import com.example.foodplaner.model.FilteredMeal;

import java.util.ArrayList;
import java.util.List;

public class MealsGridAdapter extends BaseAdapter {

    private Context context;
    private List<FilteredMeal> categoryList = new ArrayList<>();
    OnMealClickListener onMealClickListener;
    OnFavoriteClickListener onFavoriteClickListener;
    private boolean isGuest = false;

    public void setGuestMode(boolean isGuest) {
        this.isGuest = isGuest;
        notifyDataSetChanged();
    }


    public MealsGridAdapter(Context context, OnMealClickListener onMealClickListener, OnFavoriteClickListener onFavoriteClickListener) {
        this.context = context;
        this.onMealClickListener = onMealClickListener;
        this.onFavoriteClickListener = onFavoriteClickListener;
    }
    public MealsGridAdapter(Context context) {
        this.context = context;
    }

    public void setCategoryList(List<FilteredMeal> categoryList) {
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
    public void filterList(String query) {
        List<FilteredMeal> filteredList = new ArrayList<>();
        for (FilteredMeal category : categoryList) {
            if (category.getStrMeal().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(category);
            }
        }
        categoryList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.favoritecard, parent, false);
            holder = new ViewHolder();
            holder.categoryImage = convertView.findViewById(R.id.CategoryImage);
            holder.categoryName = convertView.findViewById(R.id.CategoryName);
            holder.heartImage = convertView.findViewById(R.id.imageView2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        FilteredMeal filteredMeal = categoryList.get(position);

        holder.categoryName.setText(filteredMeal.getStrMeal());
        Glide.with(context)
                .load(filteredMeal.getStrMealThumb())
                .into(holder.categoryImage);
        convertView.setOnClickListener(view -> onMealClickListener.onMealClick(filteredMeal.getidMeal()));
        if (isGuest) {
            holder.heartImage.setAlpha(0.5f);
            holder.heartImage.setOnClickListener(v -> {
                DialogUtils.showConfirmationDialog(context,
                        "To Add to favorites you must sign in",
                        (dialog, which) -> Navigation.findNavController(v)
                                .navigate(MealDetailsFragmentDirections.actionMealDetailsFragmentToSigninFragment()));
            });
        } else {
            holder.heartImage.setOnClickListener(view -> {
                if (onFavoriteClickListener != null) {
                    onFavoriteClickListener.onFavProductClick(filteredMeal.getidMeal());
                    holder.heartImage.setImageResource(R.drawable.redheartfilled);
                }
            });
        }
        return convertView;
    }

    private static class ViewHolder {
        ImageView categoryImage;
        TextView categoryName;
        TextView extraText;
        ImageView heartImage;
    }
}
