package com.example.foodplaner.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodplaner.model.Category;
import com.example.foodplaner.R;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    HomeAdapter homeAdapter;
    RecyclerView recyclerView;


    public HomeFragment() {
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.CategoriesRecyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        homeAdapter = new HomeAdapter(getContext());
        recyclerView.setAdapter(homeAdapter);
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Beef", "Beef Desc", "1","https://www.themealdb.com/images/category/beef.png"));
        categories.add(new Category("Chicken", "Chicken desc", "2","https://www.themealdb.com/images/category/chicken.png"));
        // Add more categories...

        homeAdapter.setProductList(categories);
        //presenter.subscribeFavorites();
    }
}