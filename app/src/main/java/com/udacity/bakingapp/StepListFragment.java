package com.udacity.bakingapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class StepListFragment extends ListFragment {

    public static final String STEPS_KEY = "steps";
    public static final String TITLE = "title";
    public static final String INDEX = "index";
    public static final String JSON = "json";

    String[] steps;
    private String title;
    private String json;
    private int index;

    protected Context context;

    static interface Listener {
        void itemClicked(int position);
    }

    private Listener listener;

    public StepListFragment() {}

    Bundle savedState;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.step_list_fragment, container, false);
        if (savedInstanceState != null)
        {
            steps = savedInstanceState.getStringArray(STEPS_KEY);
            title = savedInstanceState.getString(TITLE);
            index = savedInstanceState.getInt(INDEX);
            json = savedInstanceState.getString(JSON);
            savedState = savedInstanceState;
        }

        return rootView;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        this.listener = (Listener) context;
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
        if (savedState != null) setUpListFragment(json, index, title, steps);
    }

    protected void setUpListFragment(String json, int recipeNumber, String title, String[] steps)
    {
        this.json = json;
        index = recipeNumber;
        this.title = title;
        this.steps = steps;


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
        setListAdapter(listAdapter);

    }

    @Override
    public void onListItemClick(ListView listView, View itemView, int position, long id) {

        if (listener != null) {
            listener.itemClicked(position);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        savedInstanceState.putString(TITLE, title);
        savedInstanceState.putString(JSON, json);
        savedInstanceState.putInt(INDEX, index);
        savedInstanceState.putStringArray(STEPS_KEY, steps);
        super.onSaveInstanceState(savedInstanceState);
    }

    // for using LeakCanary with a fragment
    /*
    @Override
    public void onDestroy() {
        super.onDestroy();
        TheApplication.getRefWatcher(getActivity()).watch(this);
    }
    */
}
