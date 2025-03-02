package com.example.foodplaner.Features.Search.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplaner.Database.MealsLocalDataSourceImplementation;
import com.example.foodplaner.Features.Home.view.CategoriesAdapter;
import com.example.foodplaner.Features.Home.view.CountriesAdapter;
import com.example.foodplaner.Features.Home.view.OnCategoryClickListener;
import com.example.foodplaner.Features.Home.view.OnCountryClickListener;
import com.example.foodplaner.Features.Search.presenter.SearchPresenter;
import com.example.foodplaner.Features.Search.presenter.SearchPresenterImplementation;
import com.example.foodplaner.R;
import com.example.foodplaner.model.Category;
import com.example.foodplaner.model.Ingredient;
import com.example.foodplaner.model.MealCountry;
import com.example.foodplaner.model.MealRepositoryImplementation;
import com.example.foodplaner.network.MealsRemoteDataSourceImplementaion;
import com.google.android.material.chip.Chip;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class SearchFragment extends Fragment implements SearchView, OnCategoryClickListener, OnCountryClickListener ,OnIngredientClickListener{

    private Chip countryChip, categoryChip, ingredientsChip;
    private RecyclerView searchRecyclerView;
    private SearchPresenter searchPresenter;
    private SearchCategoriesAdapter categoriesAdapter;
    private SearchCountriesAdapter countriesAdapter;
    SearchIngrediantAdapter searchIngrediantAdapter;
    private EditText searchEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchEditText = view.findViewById(R.id.searchEditText);
        initializeViews(view);
        setupRecyclerView();
        setupPresenters();
        setupChipListeners();
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
        if (searchRecyclerView.getAdapter() instanceof SearchCategoriesAdapter) {
            ((SearchCategoriesAdapter) searchRecyclerView.getAdapter()).filterList(query);
        } else if (searchRecyclerView.getAdapter() instanceof SearchCountriesAdapter) {
            ((SearchCountriesAdapter) searchRecyclerView.getAdapter()).filterList(query);
        } else if (searchRecyclerView.getAdapter() instanceof SearchIngrediantAdapter) {
            ((SearchIngrediantAdapter) searchRecyclerView.getAdapter()).filterList(query);
        }
    }


    private void initializeViews(View view) {
        categoryChip = view.findViewById(R.id.categoryChip);
        countryChip = view.findViewById(R.id.countryChip);
        ingredientsChip = view.findViewById(R.id.ingredientChip);
        searchRecyclerView = view.findViewById(R.id.searchRecyclerView);
    }

    private void setupRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        searchRecyclerView.setLayoutManager(layoutManager);

        categoriesAdapter = new SearchCategoriesAdapter(requireContext(), this);
        countriesAdapter = new SearchCountriesAdapter(requireContext(), this);
        searchIngrediantAdapter=new SearchIngrediantAdapter(requireContext(),this);
    }

    private void setupPresenters() {
        searchPresenter = new SearchPresenterImplementation(
                this,
                MealRepositoryImplementation.getInstance(MealsLocalDataSourceImplementation.getInstance(getContext()),MealsRemoteDataSourceImplementaion.getInstance())
        );
    }

    private void setupChipListeners() {
        categoryChip.setOnClickListener(v -> {
            searchPresenter.getCategories();
            searchRecyclerView.setAdapter(categoriesAdapter);
        });

        countryChip.setOnClickListener(v -> {
            searchPresenter.getCountries();
            searchRecyclerView.setAdapter(countriesAdapter);
        });
        ingredientsChip.setOnClickListener(
                v -> {
                    searchPresenter.getIngredients();
                    searchRecyclerView.setAdapter(searchIngrediantAdapter);
                }
        );

    }


    @Override
    public void getCategories(List<Category> categoryList) {
        categoriesAdapter.setCategoryList(categoryList);
        categoriesAdapter.notifyDataSetChanged();
    }

    @Override
    public void getCountries(List<MealCountry> countryModel) {
        countriesAdapter.setCountryList(countryModel);
        countriesAdapter.notifyDataSetChanged();
    }

    @Override
    public void getIngredients(List<Ingredient> countryModel) {
        searchIngrediantAdapter.setCategoryList(countryModel);
        searchIngrediantAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String message) {
        Log.e("SearchFragment", "Error: " + message);
    }

    @Override
    public void onCategoryClick(Category category) {
        navigateToCategory(category.getStrCategory());
    }

    @Override
    public void onCountryClick(MealCountry country) {
        navigateToCategory(country.getStrArea());
    }

    private void navigateToCategory(String filter) {
        NavDirections action = SearchFragmentDirections.actionSearchFragmentToSearchAfterFilterFragment(filter);
        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void onIngredientClick(Ingredient ingredient) {
        navigateToCategory(ingredient.getStrIngredient());
    }
}