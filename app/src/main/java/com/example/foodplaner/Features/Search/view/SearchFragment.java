package com.example.foodplaner.Features.Search.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class SearchFragment extends Fragment implements SearchView, OnCategoryClickListener, OnCountryClickListener {

    private Chip countryChip, categoryChip, ingredientsChip;
    private RecyclerView searchRecyclerView;
    private SearchPresenter searchPresenter;
    private CategoriesAdapter categoriesAdapter;
    private CountriesAdapter countriesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeViews(view);
        setupRecyclerView();
        setupPresenters();
        setupChipListeners();
    }

    private void initializeViews(View view) {
        categoryChip = view.findViewById(R.id.categoryChip);
        countryChip = view.findViewById(R.id.countryChip);
        ingredientsChip = view.findViewById(R.id.ingredientChip);
        searchRecyclerView = view.findViewById(R.id.searchRecyclerView);
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        searchRecyclerView.setLayoutManager(layoutManager);

        categoriesAdapter = new CategoriesAdapter(requireContext(), this);
        countriesAdapter = new CountriesAdapter(requireContext(), this);
    }

    private void setupPresenters() {
        searchPresenter = new SearchPresenterImplementation(
                this,
                MealRepositoryImplementation.getInstance(MealsRemoteDataSourceImplementaion.getInstance())
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
//        categoriesAdapter.setCategoryList(countryModel);
//        categoriesAdapter.notifyDataSetChanged();
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
        NavDirections action = SearchFragmentDirections.actionSearchFragmentToSpecificMealsFragment(filter);
        Navigation.findNavController(requireView()).navigate(action);
    }

}