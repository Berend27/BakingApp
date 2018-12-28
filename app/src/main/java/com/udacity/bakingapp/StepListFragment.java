package com.udacity.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class StepListFragment extends Fragment {

    public static final String STEPS_KEY = "steps";
    public static final String TITLE = "title";
    public static final String INDEX = "index";
    public static final String JSON = "json";

    String[] steps;
    private String title;
    private String json;
    private int index;

    protected Context context;

    public StepListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (savedInstanceState != null)
        {
            // get stuff
        }

        View rootView = inflater.inflate(R.layout.step_list_fragment, container, false);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
    }

    @Override
    public void onStart()
    {
        super.onStart();

    }

    protected void setUpListFragment(String json, int recipeNumber, String title, String[] steps)
    {
        this.json = json;
        index = recipeNumber;
        this.title = title;
        this.steps = steps;
        final String FINAL_JSON = json;

        View rootView = getView();

        TextView titleTextView = (TextView) rootView.findViewById(R.id.recipe_name);
        if (title.contains("Pie"))
            titleTextView.setBackgroundColor(getResources().getColor(R.color.pie));
        if (title.contains("Brownies")) {
            titleTextView.setTextColor(getResources().getColor(R.color.icing));
            titleTextView.setBackgroundColor(getResources().getColor(R.color.brownie));
        } else if (title.contains("Yellow"))
            titleTextView.setBackgroundColor(getResources().getColor(R.color.yellow));
        else if (title.contains("Cheesecake")) {
            titleTextView.setBackgroundColor(getResources().getColor(R.color.cheeseCake));
            titleTextView.setTextColor(getResources().getColor(R.color.crust));
        } else if (title.contains("Nutella")) {
            titleTextView.setTextColor(getResources().getColor(R.color.brownie));
        }
        titleTextView.setText(title);


        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(context, R.layout.step_list_item,
                R.id.list_item, steps);
        ListView stepList = (ListView) rootView.findViewById(R.id.steps_list);
        stepList.setAdapter(listAdapter);

        // Creating the Click Listener
        AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemView, int position, long id) {
                Intent detailedIntent;
                if (position == 0)    // if "Ingredients" is selected
                    detailedIntent = new Intent(context, IngredientsActivity.class);
                else {
                    detailedIntent = new Intent(context, DetailsActivity.class);
                    position = position - 1;    // since "Ingredients" takes up position 0
                }
                Bundle selected = new Bundle();
                selected.putString(JSON, FINAL_JSON);
                selected.putInt(INDEX, index);
                selected.putInt(StepDetails.STEP, position);
                detailedIntent.putExtras(selected);
                startActivity(detailedIntent);
            }
        };
        stepList.setOnItemClickListener(clickListener);
    }
}
