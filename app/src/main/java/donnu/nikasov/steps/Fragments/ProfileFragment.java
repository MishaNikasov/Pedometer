package donnu.nikasov.steps.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import donnu.nikasov.steps.R;

import static donnu.nikasov.steps.MainActivity.APP_PROFILE_GOAL;
import static donnu.nikasov.steps.MainActivity.APP_PROFILE_GROWTH;
import static donnu.nikasov.steps.MainActivity.APP_PROFILE_SETTINGS;
import static donnu.nikasov.steps.MainActivity.APP_PROFILE_WEIGHT;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private int mWeight;
    private int mGrowth;
    private int mGoal;

    private SharedPreferences mSettings;

    private EditText profileWeight;
    private EditText profileGrowth;
    private EditText profileGoal;

    private Button button;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSettings = getContext().getSharedPreferences(APP_PROFILE_SETTINGS, Context.MODE_PRIVATE);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileWeight = (EditText) view.findViewById(R.id.profileWeight);
        profileGrowth = (EditText) view.findViewById(R.id.profileGrowth);
        profileGoal = (EditText) view.findViewById(R.id.profileGoal);
        button = (Button) view.findViewById(R.id.button);

        loadPreferences();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        mWeight = Integer.parseInt(profileWeight.getText().toString());
        mGrowth = Integer.parseInt(profileGrowth.getText().toString());
        savePreferences();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWeight = Integer.parseInt(profileWeight.getText().toString());
        mGrowth = Integer.parseInt(profileGrowth.getText().toString());
        mGoal = Integer.parseInt(profileGoal.getText().toString());
        savePreferences();
    }

    //Сохраняем настройки профиля
    protected  void savePreferences(){
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(APP_PROFILE_WEIGHT, mWeight);
        editor.putInt(APP_PROFILE_GROWTH, mGrowth);
        editor.putInt(APP_PROFILE_GOAL, mGoal);
        editor.apply();
    }

    //Загружаем настройки профиля
    protected void loadPreferences(){
        if (mSettings.contains(APP_PROFILE_WEIGHT)) {
            mWeight = mSettings.getInt(APP_PROFILE_WEIGHT, 0);
            profileWeight.setText(String.valueOf(mWeight));
        }
        if (mSettings.contains(APP_PROFILE_GROWTH)) {
            mGrowth = mSettings.getInt(APP_PROFILE_GROWTH, 0);
            profileGrowth.setText(String.valueOf(mGrowth));
        }
        if (mSettings.contains(APP_PROFILE_GOAL)){
            mGoal = mSettings.getInt(APP_PROFILE_GOAL, 0);
            profileGoal.setText(String.valueOf(mGoal));
        }
        else {
            profileGoal.setText(String.valueOf(4000));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button :
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
