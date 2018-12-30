package com.udacity.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class IngredientsFragment extends Fragment {

    private String json;
    private int recipeNumber;

    public IngredientsFragment() {}

    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        context = getActivity().getApplicationContext();
        if (savedInstanceState != null) {
            json = savedInstanceState.getString(StepListActivity.JSON);
            recipeNumber = savedInstanceState.getInt(StepListActivity.INDEX);
        }

        View rootView = inflater.inflate(R.layout.ingredients_fragment, container, false);
        return rootView;
    }

    @Override
    public void onStart()
    {
        View rootView = getView();
        super.onStart();

        // TODO stopping point 12/30 fix this button to work with a fragment on a tablet
        Button viewIntro = (Button) rootView.findViewById(R.id.view_intro);
        viewIntro.setVisibility(View.VISIBLE);
        viewIntro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(StepDetails.INDEX, recipeNumber);
                bundle.putString(StepDetails.JSON, json);
                bundle.putInt(StepDetails.STEP, 0);

                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        String[] ingredients = NetworkingUtils.getIngredients(json, recipeNumber);

        ArrayAdapter<String> ingredientsAdapter
                = new ArrayAdapter<>(context, R.layout.ingredients_item, R.id.list_item, ingredients);
        ListView listIngredients = (ListView) rootView.findViewById(R.id.ingredients_ListView);
        listIngredients.setAdapter(ingredientsAdapter);
    }

    public void setVariables(String jsonString, int number)
    {
        json = jsonString;
        recipeNumber = number;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        savedInstanceState.putString(StepListActivity.JSON, json);
        savedInstanceState.putInt(StepListActivity.INDEX, recipeNumber);
    }
}
