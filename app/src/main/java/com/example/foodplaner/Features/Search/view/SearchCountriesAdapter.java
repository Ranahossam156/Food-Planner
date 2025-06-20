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

import com.example.foodplaner.Features.Home.view.OnCountryClickListener;
import com.example.foodplaner.R;
import com.example.foodplaner.model.MealCountry;

import java.util.ArrayList;
import java.util.List;

public class SearchCountriesAdapter extends RecyclerView.Adapter<SearchCategoriesAdapter.HomeViewHolder>{
    private List<MealCountry> countryList=new ArrayList<>();
    private Context context;
    private static final String TAG = "RecyclerView";
    OnCountryClickListener onCountryClickListener;
    private Handler handler = new Handler(Looper.getMainLooper());


    public SearchCountriesAdapter(Context context, OnCountryClickListener onCountryClickListener) {
        this.context = context;
        this.onCountryClickListener = onCountryClickListener;
    }


    @NonNull
    @Override
    public SearchCategoriesAdapter.HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.mealcard, parent,  false);
        SearchCategoriesAdapter.HomeViewHolder vh= new SearchCategoriesAdapter.HomeViewHolder(v);
        Log.i(TAG, "===== onCreateViewHolder ====");
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchCategoriesAdapter.HomeViewHolder holder, int position) {
        MealCountry mealCountry = countryList.get(position);
        holder.titleTextView.setText(mealCountry.getStrArea());
        switch(mealCountry.getStrArea())
        {
            case "American":
                holder.productImageView.setImageResource(R.drawable.american_flag_large);
                break;
            case "British":
                holder.productImageView.setImageResource(R.drawable.britishflaglarge);
                break;
            case "Canadian":
                holder.productImageView.setImageResource(R.drawable.canadianflaglarge);
                break;
            case "Chinese":
                holder.productImageView.setImageResource(R.drawable.chineseflaglarge);
                break;
            case "Croatian":
                holder.productImageView.setImageResource(R.drawable.croatianflaglarge);
                break;
            case "Dutch":
                holder.productImageView.setImageResource(R.drawable.dutchflaglarge);
                break;
            case "Egyptian":
                holder.productImageView.setImageResource(R.drawable.egyptianflaglarge);
                break;
            case "Filipino":
                holder.productImageView.setImageResource(R.drawable.filipinoflaglarge);
                break;
            case "French":
                holder.productImageView.setImageResource(R.drawable.frenchflaglarge);
                break;
            case "Greek":
                holder.productImageView.setImageResource(R.drawable.greekflaglarge);
                break;
            case "Indian":
                holder.productImageView.setImageResource(R.drawable.indianflaglarge);
                break;
            case "Irish":
                holder.productImageView.setImageResource(R.drawable.irishflaglarge);
                break;
            case "Italian":
                holder.productImageView.setImageResource(R.drawable.italianflaglarge);
                break;
            case "Jamaican":
                holder.productImageView.setImageResource(R.drawable.jamaicanflaglarge);
                break;
            case "Japanese":
                holder.productImageView.setImageResource(R.drawable.japaneseflaglarge);
                break;
            case "Kenyan":
                holder.productImageView.setImageResource(R.drawable.kenyanflaglarge);
                break;
            case "Malaysian":
                holder.productImageView.setImageResource(R.drawable.malaysianflaglarge);
                break;
            case "Mexican":
                holder.productImageView.setImageResource(R.drawable.mexicanflaglarge);
                break;
            case "Moroccan":
                holder.productImageView.setImageResource(R.drawable.moroccanflaglarge);
                break;
            case "Norwegian":
                holder.productImageView.setImageResource(R.drawable.norwegianflaglarge);
                break;
            case "Polish":
                holder.productImageView.setImageResource(R.drawable.polishflaglarge);
                break;
            case "Portuguese":
                holder.productImageView.setImageResource(R.drawable.portugueseflaglarge);
                break;
            case "Russian":
                holder.productImageView.setImageResource(R.drawable.russianflaglarge);
                break;
            case "Spanish":
                holder.productImageView.setImageResource(R.drawable.spanishflaglarge);
                break;
            case "Thai":
                holder.productImageView.setImageResource(R.drawable.thaiflaglarge);
                break;
            case "Tunisian":
                holder.productImageView.setImageResource(R.drawable.tunisianflaglarge);
                break;
            case "Turkish":
                holder.productImageView.setImageResource(R.drawable.turkishflaglarge);
                break;
            case "Ukrainian":
                holder.productImageView.setImageResource(R.drawable.ukrainianflaglarge);
                break;
            case "Uruguayan":
                holder.productImageView.setImageResource(R.drawable.uruguayanflaglarge);
                break;
            case "Vietnamese":
                holder.productImageView.setImageResource(R.drawable.vietnameseflaglarge);
                break;

        }
        holder.itemView.setOnClickListener(v->onCountryClickListener.onCountryClick(mealCountry));

    }
    public void filterList(String query) {
        List<MealCountry> filteredList = new ArrayList<>();
        for (MealCountry mealCountry : countryList) { // originalList is your backup list of all categories
            if (mealCountry.getStrArea().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(mealCountry);
            }
        }
        countryList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    public void setCountryList(List<MealCountry> mealCountryList) {
        this.countryList =mealCountryList ;
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
