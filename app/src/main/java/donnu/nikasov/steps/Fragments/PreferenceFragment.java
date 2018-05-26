package donnu.nikasov.steps.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import donnu.nikasov.steps.R;

import static donnu.nikasov.steps.MainActivity.APP_PROFILE_SETTINGS;


/**
 * A simple {@link Fragment} subclass.
 */
public class PreferenceFragment extends PreferenceFragmentCompat {

    private int mWeight;
    private int mGrowth;
    private int mGoal;

    private SharedPreferences mSettings;

    public PreferenceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSettings = getContext().getSharedPreferences(APP_PROFILE_SETTINGS, Context.MODE_PRIVATE);
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.fragment_preference);
    }
}