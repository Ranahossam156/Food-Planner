package com.example.foodplaner.Features.Search.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.foodplaner.Database.MealsLocalDataSourceImplementation;
import com.example.foodplaner.Features.Search.presenter.SearchAfterFilterPresenter;
import com.example.foodplaner.Features.Search.presenter.SearchAfterFilterPresenterImplementation;
import com.example.foodplaner.Features.ShowSpecificMeals.presenter.SpecificMealsPresenter;
import com.example.foodplaner.Features.ShowSpecificMeals.presenter.SpecificMealsPresenterImplementation;
import com.example.foodplaner.Features.ShowSpecificMeals.view.MealsGridAdapter;
import com.example.foodplaner.Features.ShowSpecificMeals.view.OnFavoriteClickListener;
import com.example.foodplaner.Features.ShowSpecificMeals.view.OnMealClickListener;
import com.example.foodplaner.Features.ShowSpecificMeals.view.SpecificMealsFragmentArgs;
import com.example.foodplaner.Features.ShowSpecificMeals.view.SpecificMealsFragmentDirections;
import com.example.foodplaner.Features.ShowSpecificMeals.view.SpecificMealsView;
import com.example.foodplaner.R;
import com.example.foodplaner.model.FilteredMeal;
import com.example.foodplaner.model.MealElement;
import com.example.foodplaner.model.MealRepositoryImplementation;
import com.example.foodplaner.network.MealsRemoteDataSourceImplementaion;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class SearchAfterFilterFragment extends Fragment implements SearchAfterFilterView, OnMealClickListener, OnFavoriteClickListener {
    SearchAfterFilterPresenter searchAfterFilterPresenter;
    String filterName;
    MealsGridAdapter categoriesGridAdapter;
    GridView gridView;
    private EditText searchEditText;


    public SearchAfterFilterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_after_filter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            SearchAfterFilterFragmentArgs args = SearchAfterFilterFragmentArgs.fromBundle(getArguments());
            filterName = args.getFilter();
        }
        searchEditText = view.findViewById(R.id.searchFilterEditText);
        super.onViewCreated(view, savedInstanceState);
        gridView = view.findViewById(R.id.filtercategoriesgridRecyclerView);
        categoriesGridAdapter = new MealsGridAdapter(getContext(),this,this);
        gridView.setAdapter(categoriesGridAdapter);
        searchAfterFilterPresenter =new SearchAfterFilterPresenterImplementation(this, MealRepositoryImplementation.getInstance(MealsLocalDataSourceImplementation.getInstance(getContext()),MealsRemoteDataSourceImplementaion.getInstance()));
        searchAfterFilterPresenter.getSpecificMealsByCategories(filterName);
        searchAfterFilterPresenter.getSpecificMealsByCountries(filterName);
        searchAfterFilterPresenter.getSpecificMealsByIngredients(filterName);
        Observable.create(emitter -> {
            TextWatcher watcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    emitter.onNext(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            };

            searchEditText.addTextChangedListener(watcher);
        }).subscribe(query -> {
            filterAdapter(query.toString());
        });

    }

    private void filterAdapter(String query) {
        ((MealsGridAdapter)gridView.getAdapter()).filterList(query);
    }
    @Override
    public void getSpecificMealsByCategories(List<FilteredMeal> meal) {
        categoriesGridAdapter.setCategoryList(meal);
        categoriesGridAdapter.notifyDataSetChanged();
    }

    @Override
    public void getSpecificMealsyCountries(List<FilteredMeal> meal) {
        categoriesGridAdapter.setCategoryList(meal);
        categoriesGridAdapter.notifyDataSetChanged();
    }

    @Override
    public void getSpecificMealsyIngredients(List<FilteredMeal> meal) {
        categoriesGridAdapter.setCategoryList(meal);
        categoriesGridAdapter.notifyDataSetChanged();
    }


    @Override
    public void showError(String errorMsg) {

    }

    @Override
    public void showFavoriteAddedSuccess() {
    }


    @Override
    public void onMealClick(String id) {
        searchAfterFilterPresenter.getMealDetailsById(id);
    }

    @Override
    public void getMealDetailsByID(List<MealElement> meal) {
        if (meal != null && !meal.isEmpty()) {
            MealElement mealElement = meal.get(0);
            NavDirections action = SearchAfterFilterFragmentDirections
                    .actionSearchAfterFilterFragmentToMealDetailsFragment(mealElement);
            Navigation.findNavController(requireView()).navigate(action);
        }
    }

    @Override
    public void onFavProductClick(String id) {
        searchAfterFilterPresenter.onFavoriteClick(id);
        Toast.makeText(getContext(), "AddedSuccessfully", Toast.LENGTH_SHORT).show();

    }
}