package com.example.foodplaner.Features.Search.view;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.foodplaner.Features.Search.view.OnIngredientClickListener;
import com.example.foodplaner.model.Category;
import com.example.foodplaner.R;
import com.example.foodplaner.model.Ingredient;
import com.example.foodplaner.model.IngredientItem;

import java.util.ArrayList;
import java.util.List;

public class SearchIngrediantAdapter extends RecyclerView.Adapter<SearchIngrediantAdapter.SearchIngredientHolder> {

    private List<Ingredient> ingredients=new ArrayList<>();
    private Context context;
    OnIngredientClickListener onIngredientClickListener;

    private Handler handler = new Handler(Looper.getMainLooper());


    public SearchIngrediantAdapter(Context context,OnIngredientClickListener onIngredientClickListener) {
        this.onIngredientClickListener = onIngredientClickListener;
        this.context = context;
    }



    @NonNull
    @Override
    public SearchIngredientHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.mealcard, parent,  false);
        SearchIngredientHolder vh= new SearchIngredientHolder(v);
        Log.i("TAG", "===== onCreateViewHolder ====");
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchIngredientHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.titleTextView.setText(ingredient.getStrIngredient());
        String ingredientName=ingredient.getStrIngredient();
        String imageUrl = "https://www.themealdb.com/images/ingredients/" + ingredientName + "-Small.png";
        Glide.with(context).load(imageUrl).apply(new RequestOptions().override(200,200)).into(holder.productImageView);
        holder.itemView.setOnClickListener(v->onIngredientClickListener.onIngredientClick(ingredient));
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public void setCategoryList(List<Ingredient> ingredientList) {
        this.ingredients = ingredientList;
        notifyDataSetChanged();
    }
    public void filterList(String query) {
        List<Ingredient> filteredList = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getStrIngredient().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(ingredient);
            }
        }
        ingredients = filteredList;
        notifyDataSetChanged();
    }

    public static class SearchIngredientHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        ImageView productImageView;

        public SearchIngredientHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.CategoryName);
            productImageView = itemView.findViewById(R.id.CategoryImage);
        }
    }

}