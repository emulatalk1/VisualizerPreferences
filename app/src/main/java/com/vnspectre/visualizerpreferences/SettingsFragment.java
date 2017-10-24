package com.vnspectre.visualizerpreferences;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

/**
 * Created by Spectre on 10/24/17.
 */

public class SettingsFragment extends PreferenceFragmentCompat{
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_visualizer);
    }
}
