package com.mrapocalypse.purplesprite3.fragments;


import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mrapocalypse.purplesprite3.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Recents extends PreferenceFragment implements Preference.OnPreferenceChangeListener {


    public Recents() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_recents);

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }
}
