package com.example.foodplaner.Features.Plan_Meals.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplaner.R;
import com.example.foodplaner.model.PlannedMeal;

import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {
    private final List<PlannedMeal> meals;
    private OnPlannedMealClicked listener;
    private OnPlannedMealRemoved onPlannedMealRemoved;



    public MealAdapter(List<PlannedMeal> meals, OnPlannedMealClicked listener, OnPlannedMealRemoved plannedMealRemoved) {
        this.meals = meals;
        this.listener = listener;
        this.onPlannedMealRemoved=plannedMealRemoved;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fav_cal_item, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        PlannedMeal meal = meals.get(position);
        holder.bind(meal);

        holder.itemView.setOnClickListener(v -> {
                listener.onMealClicked(meal);
        });
        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPlannedMealRemoved.onMealRemoved(meal);
            }
        });
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    static class MealViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mealImage;
        private final TextView mealName;
        private final ImageView deleteIcon;

        MealViewHolder(View itemView) {
            super(itemView);
            mealImage = itemView.findViewById(R.id.fav_cal_Image);
            mealName = itemView.findViewById(R.id.fav_cal_Name);
            deleteIcon=itemView.findViewById(R.id.removeicon);
        }

        void bind(PlannedMeal meal) {
            mealName.setText(meal.getStrMeal());
            Glide.with(itemView)
                    .load(meal.getStrMealThumb())
                    .into(mealImage);
        }
    }

}