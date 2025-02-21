package com.example.foodplaner.view;

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
import com.example.foodplaner.Category;
import com.example.foodplaner.R;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    private List<Category> categoryList=new ArrayList<>();
    private Context context;
    private static final String TAG = "RecyclerView";
    private Handler handler = new Handler(Looper.getMainLooper());

    public HomeAdapter(Context context) {
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
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public void setProductList(List<Category> productList) {
        this.categoryList = productList;
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