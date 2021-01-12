package com.example.answered;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Implements a fragment used for the default view in the app.
 */
public class MainFragment extends Fragment {

    /**
     * Required empty public constructor
     */
    public MainFragment() {
    }

    /**
     * Inflates the fragment layout in the NavHost container.
     * @param inflater used to inflate the layout
     * @param container container to inflate layout in
     * @param savedInstanceState unused
     * @return View that is created from inflating the layout
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }
}