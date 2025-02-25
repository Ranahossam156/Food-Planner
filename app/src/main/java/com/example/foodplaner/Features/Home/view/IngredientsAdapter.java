//package com.example.foodplaner.Features.Home.view;
//
//
//import android.content.Context;
//import android.os.Handler;
//import android.os.Looper;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;
//import com.example.foodplaner.model.Category;
//import com.example.foodplaner.R;
//import com.example.foodplaner.model.Ingredient;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.HomeViewHolder> {
//
//    private List<Ingredient> ingredientList=new ArrayList<>();
//    private Context context;
//    private static final String TAG = "RecyclerView";
//    private OnCategoryClickListener listener;
//
//    private Handler handler = new Handler(Looper.getMainLooper());
//
//    public IngredientsAdapter(Context context, OnCategoryClickListener listener) {
//        this.context = context;
//        this.listener = listener;
//    }
//
//    public IngredientsAdapter(Context context) {
//        this.context = context;
//    }
//
//
//    @NonNull
//    @Override
//    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//        View v = inflater.inflate(R.layout.mealcard, parent,  false);
//        HomeViewHolder vh= new HomeViewHolder(v);
//        Log.i(TAG, "===== onCreateViewHolder ====");
//        return vh;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
//        Ingredient ingredient = ingredientList.get(position);
//        holder.titleTextView.setText(ingredient.getStrIngredient());
//        Glide.with(context).load(ingredient.t()).apply(new RequestOptions().override(200,200)).into(holder.productImageView);
//        holder.itemView.setOnClickListener(v->listener.onCategoryClick(category));
//    }
//
//    @Override
//    public int getItemCount() {
//        return ingredientList.size();
//    }
//
//    public void setCategoryList(List<Ingredient> ingredientList) {
//        this.ingredientList = ingredientList;
//        notifyDataSetChanged();
//    }
//
//    public static class HomeViewHolder extends RecyclerView.ViewHolder {
//        TextView titleTextView;
//        ImageView productImageView;
//
//        public HomeViewHolder(@NonNull View itemView) {
//            super(itemView);
//            titleTextView = itemView.findViewById(R.id.CategoryName);
//            productImageView = itemView.findViewById(R.id.CategoryImage);
//        }
//    }
//
//}