package com.example.foodplaner.Features.PlanMeals.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.foodplaner.Database.MealsLocalDataSourceImplementation;
import com.example.foodplaner.Database.SharedPrefrencesDataSourceImplementation;
import com.example.foodplaner.Features.PlanMeals.presenter.PlanMealsPresenter;
import com.example.foodplaner.Features.PlanMeals.presenter.PlannedMealPresenterImplementation;
import com.example.foodplaner.R;
import com.example.foodplaner.Utils.DialogUtils;
import com.example.foodplaner.model.MealElement;
import com.example.foodplaner.model.MealRepository;
import com.example.foodplaner.model.MealRepositoryImplementation;
import com.example.foodplaner.model.PlannedMeal;
import com.example.foodplaner.network.FirebaseDataSourceImpl;
import com.example.foodplaner.network.MealsRemoteDataSourceImplementaion;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PlanFragment extends Fragment implements OnPlannedMealClicked, OnPlannedMealRemoved,PlannedView{
    private RecyclerView daysRecyclerView;
    private MealRepository mealRepository;
    private DayAdapter adapter;
    PlanMealsPresenter planMealsPresenter;
    TextView guestMessage2;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout directly
        return inflater.inflate(R.layout.fragment_plan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        daysRecyclerView = view.findViewById(R.id.daysRecycleView);
        planMealsPresenter=new PlannedMealPresenterImplementation(this, MealRepositoryImplementation.getInstance(MealsLocalDataSourceImplementation.getInstance(getContext()), MealsRemoteDataSourceImplementaion.getInstance(),FirebaseDataSourceImpl.getInstance(getContext()),SharedPrefrencesDataSourceImplementation.getInstance(getContext())));
        mealRepository = MealRepositoryImplementation.getInstance(
                MealsLocalDataSourceImplementation.getInstance(requireContext()),
                MealsRemoteDataSourceImplementaion.getInstance(),FirebaseDataSourceImpl.getInstance(getContext()), SharedPrefrencesDataSourceImplementation.getInstance(getContext())
        );
        guestMessage2=view.findViewById(R.id.guestmessage2);

        if (planMealsPresenter.isGuest()) {
            daysRecyclerView.setVisibility(View.GONE);
            guestMessage2.setVisibility(View.VISIBLE);
        } else {
            daysRecyclerView.setVisibility(View.VISIBLE);
            guestMessage2.setVisibility(View.GONE);
        }

        setupRecyclerView();
        planMealsPresenter.getPlannedMeals();}

    private void setupRecyclerView() {
        adapter = new DayAdapter(Collections.emptyList(),this,this);
        daysRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        daysRecyclerView.setAdapter(adapter);
    }

    private List<DayAdapter.DayItem> createWeeklyPlan(List<PlannedMeal> meals) {
        List<DayAdapter.DayItem> weekPlan = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d", Locale.getDefault());

        for (int i = 0; i < 7; i++) {
            Date currentDate = calendar.getTime();
            String dayName = dayFormat.format(currentDate);
            String dateString = dateFormat.format(currentDate);

            List<PlannedMeal> dailyMeals = new ArrayList<>();
            for (PlannedMeal meal : meals) {
                if (meal.getDayOfWeek().equalsIgnoreCase(dayName)) {
                    dailyMeals.add(meal);
                }
            }

            weekPlan.add(new DayAdapter.DayItem(dayName, dateString, dailyMeals));
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        return weekPlan;
    }

    private void showEmptyState() {
        daysRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        daysRecyclerView = null;
    }

    @Override
    public void onMealClicked(PlannedMeal meal) {
        MealElement mealElement=new MealElement(meal.getStrIngredient10(),meal.getStrIngredient12(),meal.getStrIngredient11(),meal.getStrIngredient14(),meal.getStrCategory(),meal.getStrIngredient13(),meal.getStrIngredient16(),meal.getStrIngredient15(),meal.getStrIngredient18(),meal.getStrIngredient17(),meal.getStrArea(),meal.getStrIngredient19(),meal.getStrTags(),meal.getIdMeal(),meal.getStrInstructions(),meal.getStrIngredient1(),meal.getStrIngredient3(),meal.getStrIngredient2(),meal.getStrIngredient20(),meal.getStrIngredient5(),meal.getStrIngredient4(),meal.getStrIngredient7(),meal.getStrIngredient6(),meal.getStrIngredient9(),meal.getStrIngredient8(),meal.getStrMealThumb(),meal.getStrMeasure20(),meal.getStrYoutube(),meal.getStrMeal(),meal.getStrMeasure12(),meal.getStrMeasure13(),meal.getStrMeasure10(),meal.getStrMeasure11(),meal.getStrSource(),meal.getStrMeasure9(),meal.getStrMeasure7(),meal.getStrMeasure8(),meal.getStrMeasure5(),meal.getStrMeasure6(),meal.getStrMeasure3(),meal.getStrMeasure4(),meal.getStrMeasure1(), meal.getStrMeasure18(),meal.getStrMeasure2(),meal.getStrMeasure19(),meal.getStrMeasure16(),meal.getStrMeasure17(),meal.getStrMeasure14(),meal.getStrMeasure15());

        NavDirections action = PlanFragmentDirections.actionPlanFragmentToMealDetailsFragment(mealElement);
        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void onMealRemoved(PlannedMeal meal) {
        showRemoveConfirmationDialog(meal);
    }
    private void showRemoveConfirmationDialog(PlannedMeal plannedMeal) {
        DialogUtils.showConfirmationDialog(requireContext(),
                "Are you sure you want to remove this item?",
                (dialog, which) -> planMealsPresenter.onPlannedeRemoved(plannedMeal));
    }
    @Override
    public void getPlanned(List<PlannedMeal> meals) {
        if (meals.isEmpty()) {
            showEmptyState();
        } else {
            daysRecyclerView.setVisibility(View.VISIBLE);

            List<DayAdapter.DayItem> weekPlan = createWeeklyPlan(meals);
            adapter.updateData(weekPlan);
        }
    }

}