package com.udacity.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.OnClick;

public class RecipeCardAdapter extends RecyclerView.Adapter<RecipeCardAdapter.CardViewHolder> {

    private final String TAG = "Recipe RecyclerView";

    final private RecipeCardClickListener onClickListener;

    private String[] recipes;  // = {"Nutella Pie", "Brownies"};

    private int numberOfRecipes;

    private String json;

    public interface RecipeCardClickListener
    {
        void onListItemClicked(int clickedItemIndex);
    }

    public RecipeCardAdapter(int numberOfCards, RecipeCardClickListener listener)
    {
        numberOfRecipes = numberOfCards;
        onClickListener = listener;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.card_collection_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        CardViewHolder viewHolder = new CardViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder cardViewHolder, int position) {
        Log.d(TAG, "# " + position);
        cardViewHolder.setRecipeName(recipes[position]);
    }

    @Override
    public int getItemCount() {
        return numberOfRecipes;
    }

    public void setJson(String jsonString) {
        this.json = jsonString;
    }

    public void setRecipes() {
        recipes = NetworkingUtils.getRecipeNames(json);
    }


    class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView recipeCardTextView;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeCardTextView = (TextView) itemView.findViewById(R.id.recipe_name);
            recipeCardTextView.setOnClickListener(this);
        }

        void setRecipeName(String recipeName)
        {
            recipeCardTextView.setText(recipeName);
        }

        @Override
        public void onClick(View v)
        {
            int clickedPosition = getAdapterPosition();
            onClickListener.onListItemClicked(clickedPosition);
        }

    }

}
