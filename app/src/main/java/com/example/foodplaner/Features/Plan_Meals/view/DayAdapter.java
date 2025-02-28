package com.example.foodplaner.Features.Plan_Meals.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplaner.R;
import com.example.foodplaner.model.PlannedMeal;

import java.util.List;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder> {
    private List<DayItem> days;
    private static OnPlannedMealClicked listener;
    OnPlannedMealRemoved onPlannedMealRemoved;
    public DayAdapter(List<DayItem> days,OnPlannedMealClicked listener,OnPlannedMealRemoved onPlannedMealRemoved) {
        this.days = days;
        this.listener=listener;
        this.onPlannedMealRemoved=onPlannedMealRemoved;
    }

    public void updateData(List<DayItem> newDays) {
        this.days = newDays;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.plan_item, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        DayItem dayItem = days.get(position);
        holder.bind(dayItem);
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    class DayViewHolder extends RecyclerView.ViewHolder {
        private final TextView dayTextView;
        private final RecyclerView mealsRecyclerView;
        private final TextView emptyView;

        DayViewHolder(View itemView) {
            super(itemView);
            dayTextView = itemView.findViewById(R.id.dayTextView);
            mealsRecyclerView = itemView.findViewById(R.id.mealsRecycleView);
            emptyView = itemView.findViewById(R.id.empty_view);
        }

        void bind(DayItem dayItem) {
            String header = dayItem.getDayName() + "\n(" + dayItem.getFormattedDate() + ")";
            dayTextView.setText(header);

            if (dayItem.getMeals().isEmpty()) {
                emptyView.setVisibility(View.VISIBLE);
                mealsRecyclerView.setVisibility(View.GONE);
            } else {
                emptyView.setVisibility(View.GONE);
                mealsRecyclerView.setVisibility(View.VISIBLE);
                setupMealsRecyclerView(dayItem.getMeals());
            }
        }

        private void setupMealsRecyclerView(List<PlannedMeal> meals) {
            MealAdapter mealAdapter = new MealAdapter(meals,listener,onPlannedMealRemoved);
            mealsRecyclerView.setLayoutManager(
                    new LinearLayoutManager(itemView.getContext(),
                            LinearLayoutManager.HORIZONTAL,
                            false)
            );
            mealsRecyclerView.setAdapter(mealAdapter);
        }
    }

    public static class DayItem {
        private final String dayName;
        private final String formattedDate;
        private final List<PlannedMeal> meals;

        public DayItem(String dayName, String formattedDate, List<PlannedMeal> meals) {
            this.dayName = dayName;
            this.formattedDate = formattedDate;
            this.meals = meals;
        }

        public String getDayName() { return dayName; }
        public String getFormattedDate() { return formattedDate; }
        public List<PlannedMeal> getMeals() { return meals; }
    }
}
