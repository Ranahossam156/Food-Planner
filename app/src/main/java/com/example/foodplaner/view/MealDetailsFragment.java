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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.foodplaner.R;
import com.example.foodplaner.model.IngredientItem;
import com.example.foodplaner.model.MealElement;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MealDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MealDetailsFragment extends Fragment {
    private YouTubePlayerView youTubePlayerView;
    private String youtubeVideoId = "";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MealElement meal;
    ImageView mealImage;
    TextView mealName;
    TextView mealCountry;
    TextView instructions;

    public MealDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MealDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MealDetailsFragment newInstance(String param1, String param2) {
        MealDetailsFragment fragment = new MealDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            MealDetailsFragmentArgs args = MealDetailsFragmentArgs.fromBundle(getArguments());
            meal = args.getShowMealDetails();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meal_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         mealImage = view.findViewById(R.id.meal_image);
         mealName = view.findViewById(R.id.meal_name);
         mealCountry = view.findViewById(R.id.meal_country);
        instructions=view.findViewById(R.id.meal_instructions);
        mealName.setText(meal.getStrMeal());
        mealCountry.setText(meal.getStrArea());
        instructions.setText(meal.getStrInstructions());
        Glide.with(this).load(meal.getStrMealThumb()).into(mealImage);
        setupIngredientsRecyclerView(meal);
        youTubePlayerView = view.findViewById(R.id.meal_video);
        getLifecycle().addObserver(youTubePlayerView);

        String youtubeUrl = meal.getStrYoutube();
        if(youtubeUrl != null && !youtubeUrl.isEmpty()) {
            youtubeVideoId = extractYoutubeId(youtubeUrl);
        }

        setupYouTubePlayer();
                View bottomNav = getActivity().findViewById(R.id.bottomNavigationView);
            bottomNav.setVisibility(View.GONE);

    }
    private void setupYouTubePlayer() {
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                if(!youtubeVideoId.isEmpty()) {
                    youTubePlayer.loadVideo(youtubeVideoId, 0);
                }
            }
        });
    }

    private String extractYoutubeId(String url) {
        String videoId = "";
        if (url != null && url.trim().length() > 0) {
            String pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";
            Pattern compiledPattern = Pattern.compile(pattern);
            Matcher matcher = compiledPattern.matcher(url);
            if(matcher.find()){
                videoId = matcher.group();
            }
        }
        return videoId;
    }
    private List<IngredientItem> parseIngredients(MealElement meal) {
        List<IngredientItem> ingredients = new ArrayList<>();
        String baseImageUrl = "https://www.themealdb.com/images/ingredients/";

        String[] ingredientValues = {
                meal.getStrIngredient1(), meal.getStrIngredient2(), meal.getStrIngredient3(),
                meal.getStrIngredient4(), meal.getStrIngredient5(), meal.getStrIngredient6(),
                meal.getStrIngredient7(), meal.getStrIngredient8(), meal.getStrIngredient9(),
                meal.getStrIngredient10(), meal.getStrIngredient11(), meal.getStrIngredient12(),
                meal.getStrIngredient13(), meal.getStrIngredient14(), meal.getStrIngredient15(),
                meal.getStrIngredient16(), meal.getStrIngredient17(), meal.getStrIngredient18(),
                meal.getStrIngredient19(), meal.getStrIngredient20()
        };

        String[] measureValues = {
                meal.getStrMeasure1(), meal.getStrMeasure2(), meal.getStrMeasure3(),
                meal.getStrMeasure4(), meal.getStrMeasure5(), meal.getStrMeasure6(),
                meal.getStrMeasure7(), meal.getStrMeasure8(), meal.getStrMeasure9(),
                meal.getStrMeasure10(), meal.getStrMeasure11(), meal.getStrMeasure12(),
                meal.getStrMeasure13(), meal.getStrMeasure14(), meal.getStrMeasure15(),
                meal.getStrMeasure16(), meal.getStrMeasure17(), meal.getStrMeasure18(),
                meal.getStrMeasure19(), meal.getStrMeasure20()
        };

        for (int i = 0; i < ingredientValues.length; i++) {
            String ingredient = ingredientValues[i];
            String measure = measureValues[i];

            if (ingredient != null && !ingredient.trim().isEmpty()) {
                String imageUrl = baseImageUrl + ingredient.replace(" ", "_") + "-Small.png";
                ingredients.add(new IngredientItem(ingredient, measure != null ? measure : "", imageUrl));
            }
        }

        return ingredients;
    }

    private void setupIngredientsRecyclerView(MealElement meal) {
        List<IngredientItem> ingredients = parseIngredients(meal);

        RecyclerView ingredientsRecycler = getView().findViewById(R.id.ingredients_recycler);
        IngredientsAdapter adapter = new IngredientsAdapter(getContext(), ingredients);

        ingredientsRecycler.setLayoutManager(new LinearLayoutManager(
                getContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        ));

        ingredientsRecycler.setAdapter(adapter);
        ingredientsRecycler.setHasFixedSize(true);
    }
}