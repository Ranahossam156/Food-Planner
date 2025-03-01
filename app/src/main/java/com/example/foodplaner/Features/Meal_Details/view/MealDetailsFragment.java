package com.example.foodplaner.Features.Meal_Details.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foodplaner.Database.MealsLocalDataSourceImplementation;
import com.example.foodplaner.Features.Meal_Details.presenter.MealDetailsPresenter;
import com.example.foodplaner.Features.Meal_Details.presenter.MealDetailsPresenterImplementation;
import com.example.foodplaner.R;
import com.example.foodplaner.model.IngredientItem;
import com.example.foodplaner.model.MealElement;
import com.example.foodplaner.model.MealRepositoryImplementation;
import com.example.foodplaner.model.PlannedMeal;
import com.example.foodplaner.network.MealsRemoteDataSourceImplementaion;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class MealDetailsFragment extends Fragment implements MealDetailsView{
    private YouTubePlayerView youTubePlayerView;
    private String youtubeVideoId = "";
    ImageView planImage;

    private MealElement meal;
    ImageView mealImage,heart;
    TextView mealName;
    TextView mealCountry;
    TextView instructions;
    MealDetailsPresenter mealDetailsPresenter;

    public MealDetailsFragment() {
        // Required empty public constructor
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
        planImage=view.findViewById(R.id.plan_icon);
        heart=view.findViewById(R.id.heart_icon);
        mealName.setText(meal.getStrMeal());
        mealCountry.setText(meal.getStrArea());
        instructions.setText(meal.getStrInstructions());
        Glide.with(this).load(meal.getStrMealThumb()).into(mealImage);
        setupIngredientsRecyclerView(meal);
        mealDetailsPresenter=new MealDetailsPresenterImplementation(this, MealRepositoryImplementation.getInstance(MealsLocalDataSourceImplementation.getInstance(getContext()), MealsRemoteDataSourceImplementaion.getInstance()));
        youTubePlayerView = view.findViewById(R.id.meal_video);
        getLifecycle().addObserver(youTubePlayerView);
        planImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDayPickerDialog();
                Toast.makeText(getContext(), "Plan Added Successfully", Toast.LENGTH_SHORT).show();
            }
        });
        String youtubeUrl = meal.getStrYoutube();
        if(youtubeUrl != null && !youtubeUrl.isEmpty()) {
            youtubeVideoId = extractYoutubeId(youtubeUrl);
        }
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mealDetailsPresenter.OnFavoriteClicked(meal.getIdMeal());
                heart.setImageResource(R.drawable.redheartfilled);
            }
        });

        setupYouTubePlayer();

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
    private void showDayPickerDialog() {
        final String[] days = {
                "Saturday",
                "Sunday",
                "Monday",
                "Tuesday",
                "Wednesday",
                "Thursday",
                "Friday"
        };

        final int[] selectedItem = {-1};

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Choose any day you want");

        builder.setSingleChoiceItems(days, selectedItem[0], (dialog, which) -> {
            selectedItem[0] = which;
        });

        builder.setPositiveButton("OK", (dialog, which) -> {
            if (selectedItem[0] >= 0) {
                String chosenDay = days[selectedItem[0]];
                PlannedMeal plannedMeal=new PlannedMeal(chosenDay,meal.getStrIngredient10(), meal.getStrIngredient12(), meal.getStrIngredient11(),meal.getStrIngredient14(),meal.getStrCategory(),meal.getStrIngredient13(),meal.getStrIngredient16(),meal.getStrIngredient15(),meal.getStrIngredient18(),meal.getStrIngredient17(),meal.getStrArea(),meal.getStrIngredient19(),meal.getStrTags(),meal.getIdMeal(),meal.getStrInstructions(),meal.getStrIngredient1(),meal.getStrIngredient3(),meal.getStrIngredient2(),meal.getStrIngredient20(),meal.getStrIngredient5(),meal.getStrIngredient4(),meal.getStrIngredient7(),meal.getStrIngredient6(),meal.getStrIngredient9(),meal.getStrIngredient8(),meal.getStrMealThumb(),meal.getStrMeasure20(),meal.getStrYoutube(),meal.getStrMeal(),meal.getStrMeasure12(),meal.getStrMeasure13(),meal.getStrMeasure10(),meal.getStrMeasure11(),meal.getStrSource(),meal.getStrMeasure9(),meal.getStrMeasure7(),meal.getStrMeasure8(),meal.getStrMeasure5(),meal.getStrMeasure6(),meal.getStrMeasure3(),meal.getStrMeasure4(),meal.getStrMeasure1(),meal.getStrMeasure18(),meal.getStrMeasure2(),meal.getStrMeasure19(),meal.getStrMeasure16(),meal.getStrMeasure17(),meal.getStrMeasure14(),meal.getStrMeasure15());
                mealDetailsPresenter.OnPlanClicked(plannedMeal);
                Toast.makeText(this.getContext(), "Selected: " + chosenDay, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this.getContext(), "No day selected", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("CANCEL", (dialog, which) -> {
            dialog.dismiss();
        });

        builder.create().show();
    }

    @Override
    public void onFavoriteClick(String id) {
        mealDetailsPresenter.OnFavoriteClicked(id);
    }

    @Override
    public void onPlanClick() {

    }

    @Override
    public void showError(String errorMsg) {

    }

    @Override
    public void showFavoriteAddedSuccess() {

    }
}