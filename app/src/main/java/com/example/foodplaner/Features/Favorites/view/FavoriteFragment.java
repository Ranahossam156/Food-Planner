package com.example.foodplaner.Features.Favorites.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodplaner.Database.MealsLocalDataSourceImplementation;
import com.example.foodplaner.Database.SharedPrefrencesDataSourceImplementation;
import com.example.foodplaner.Features.Favorites.presenter.FavoritePresenter;
import com.example.foodplaner.Features.Favorites.presenter.FavoritePresenterImplementation;
import com.example.foodplaner.Features.ShowSpecificMeals.view.OnFavoriteClickListener;
import com.example.foodplaner.R;
import com.example.foodplaner.Utils.DialogUtils;
import com.example.foodplaner.model.MealElement;
import com.example.foodplaner.model.MealRepositoryImplementation;
import com.example.foodplaner.network.FirebaseDataSourceImpl;
import com.example.foodplaner.network.MealsRemoteDataSourceImplementaion;

import java.util.List;

public class FavoriteFragment extends Fragment implements FavoriteView , onFavoriteMealDetailsListener, OnFavoriteClickListener,OnRemoveClickListener {
    FavoritePresenter favoritePresenter;
    FavoriteGridAdapter favoriteGridAdapter;
    ImageView back;
    TextView guestMessage;
    GridView gridView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        favoritePresenter=new FavoritePresenterImplementation(this, MealRepositoryImplementation.getInstance(MealsLocalDataSourceImplementation.getInstance(getContext()), MealsRemoteDataSourceImplementaion.getInstance(),FirebaseDataSourceImpl.getInstance(getContext()), SharedPrefrencesDataSourceImplementation.getInstance(getContext())));
        super.onViewCreated(view, savedInstanceState);
        gridView = view.findViewById(R.id.favoritegridRecyclerView);
        favoriteGridAdapter = new FavoriteGridAdapter(getContext(),this,this,this);
        gridView.setAdapter(favoriteGridAdapter);
        favoritePresenter.getFavoriteMeals();
        guestMessage=view.findViewById(R.id.guestmessage);
        favoritePresenter.isGuest();

    }

    @Override
    public void getFavorites(List<MealElement> meals) {
        favoriteGridAdapter.setCategoryList(meals);
        favoriteGridAdapter.notifyDataSetChanged();
    }

    @Override
    public void getClickedMealDetails(MealElement meal) {
        NavDirections action = FavoriteFragmentDirections.actionFavoriteFragmentToMealDetailsFragment(meal);
        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void onFavProductClick(String id) {

    }

    @Override
    public void onRemoveClicked(MealElement mealElement) {
        showRemoveConfirmationDialog(mealElement);
        Toast.makeText(this.getContext(), "Removed", Toast.LENGTH_SHORT).show();
    }
    private void showRemoveConfirmationDialog(MealElement mealElement) {
        DialogUtils.showConfirmationDialog(requireContext(),
                "Are you sure you want to remove this item?",
                (dialog, which) -> favoritePresenter.onFavoriteRemoved(mealElement));
    }

    @Override
    public void showGuestMessage(boolean isGuest) {
        if (isGuest) {
            gridView.setVisibility(View.GONE);
            guestMessage.setVisibility(View.VISIBLE);
        } else {
            gridView.setVisibility(View.VISIBLE);
            guestMessage.setVisibility(View.GONE);
        }
    }

}