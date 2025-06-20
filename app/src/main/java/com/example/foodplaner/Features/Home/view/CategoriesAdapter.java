package com.example.foodplaner.Features.Home.view;

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
import com.example.foodplaner.model.Category;
import com.example.foodplaner.R;

import java.util.ArrayList;
import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.HomeViewHolder> {

    private List<Category> categoryList=new ArrayList<>();
    private Context context;
    private static final String TAG = "RecyclerView";
    private OnCategoryClickListener listener;

    private Handler handler = new Handler(Looper.getMainLooper());

    public CategoriesAdapter(Context context, OnCategoryClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public CategoriesAdapter(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.mealcard, parent,  false);
        HomeViewHolder vh= new HomeViewHolder(v);
        Log.i(TAG, "===== onCreateViewHolder ====");
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.titleTextView.setText(category.getStrCategory());
        Glide.with(context).load(category.getStrCategoryThumb()).apply(new RequestOptions().override(200,200)).into(holder.productImageView);
        holder.itemView.setOnClickListener(v->listener.onCategoryClick(category));
    }
    public void filterList(String query) {
        List<Category> filteredList = new ArrayList<>();
        for (Category category : categoryList) {
            if (category.getStrCategory().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(category);
            }
        }
        categoryList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    public static class HomeViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        ImageView productImageView;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.CategoryName);
            productImageView = itemView.findViewById(R.id.CategoryImage);
        }
    }

}