package com.mrapocalypse.purplesprite3.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mrapocalypse.purplesprite3.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UI extends Fragment {


    public UI() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_ui, container, false);
        return rootView;
    }


}
