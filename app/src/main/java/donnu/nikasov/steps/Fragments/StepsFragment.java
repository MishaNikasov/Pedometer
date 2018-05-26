package donnu.nikasov.steps.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import donnu.nikasov.steps.Data.DayStepsIdentity;
import donnu.nikasov.steps.R;

import static donnu.nikasov.steps.MainActivity.APP_CURRENT_DATE;
import static donnu.nikasov.steps.MainActivity.APP_PROFILE_GOAL;
import static donnu.nikasov.steps.MainActivity.APP_PROFILE_GROWTH;
import static donnu.nikasov.steps.MainActivity.APP_PROFILE_SETTINGS;
import static donnu.nikasov.steps.MainActivity.APP_PROFILE_WEIGHT;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StepsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StepsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepsFragment extends Fragment implements SensorEventListener{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private SensorManager sensorManager;
    private TextView calCount;
    private TextView stepsCount;
    private TextView timeCount;
    private TextView meterCount;
    private TextView stepGoal;
    private boolean running = false;

    private double calCountValue;
    private double stepsCountValue;
    private double timeCountValue;
    private double meterCountValue;

    private int mWeight;
    private int mGrowth;
    private int mGoal;
    private double stepLenth;
    private boolean stepGoalBool = false;

    private String stepsCountValueFormat;
    private String meterCountValueFormat;
    private String timeCountValueFormat;
    private String calCountValueFormat;
    private String dateString;

    private SharedPreferences mSettings;

    public StepsFragment() {
        // Required empty public constructor
    }

    public static StepsFragment newInstance(String param1, String param2) {
        StepsFragment fragment = new StepsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSettings = getContext().getSharedPreferences(APP_PROFILE_SETTINGS, Context.MODE_PRIVATE);

        if (!mSettings.contains(APP_PROFILE_GOAL)){
            mGoal = 4000;
        }
        else
            mGoal = mSettings.getInt(APP_PROFILE_GOAL, 0);

        mWeight = mSettings.getInt(APP_PROFILE_WEIGHT, 0);
        mGrowth = mSettings.getInt(APP_PROFILE_GROWTH, 0);

        stepLenth = (mGrowth/100/4)+0.37;

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_steps, container, false);

        stepsCount = (TextView) view.findViewById(R.id.stepCount);
        meterCount = (TextView) view.findViewById(R.id.meterCount);
        calCount = (TextView) view.findViewById(R.id.calCount);
        timeCount = (TextView) view.findViewById(R.id.timeCount);
        stepGoal = (TextView) view.findViewById(R.id.stepGoal);

        sensorManager = (SensorManager) getContext().getSystemService(getContext().SENSOR_SERVICE);

        stepGoal.setText("Цель: " + mGoal);
//
//        Date date = new Date();
//        SimpleDateFormat sdf1 = new SimpleDateFormat("dd");
//        String dateString1 = sdf1.format(date);
//
//        String dataa = "";
//
//        if (mSettings.contains(APP_CURRENT_DATE)) {
//            dataa = mSettings.getString(APP_CURRENT_DATE, "");
//        }
//
//        if (Integer.getInteger(dateString1)>Integer.getInteger(dataa)){
//            SharedPreferences.Editor editor = mSettings.edit();
//            editor.putString(APP_CURRENT_DATE, dateString1);
//            editor.apply();
//
//
//            stepsCountValue = 0;
//        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        mWeight = mSettings.getInt(APP_PROFILE_WEIGHT, 0);
        mGrowth = mSettings.getInt(APP_PROFILE_GROWTH, 0);
        stepLenth = (mGrowth/100.0/4.0)+0.37;
        mGoal = mSettings.getInt(APP_PROFILE_GOAL, 0);
        stepGoal.setText("Цель: " + mGoal);
        running = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (countSensor!=null){
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        }
        else {
            Toast.makeText(getContext(),"Opa", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        running = false;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
//            Toast.makeText(context, "Not", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onSensorChanged(SensorEvent event) {

        if (running){
            stepsCountValue = event.values[0];
            meterCountValue = (event.values[0])*stepLenth/1000;
            timeCountValue = meterCountValue/5*60*60;
            calCountValue = meterCountValue*0.5*mWeight;

            Double d = new Double(timeCountValue);
            int i = d.intValue();

            stepsCountValueFormat = String.format("%.0f", stepsCountValue);
            meterCountValueFormat = String.format("%.2f", meterCountValue);
            timeCountValueFormat = timeConversion(i);
            calCountValueFormat = String.format("%.1f", calCountValue);

            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM");

            dateString = sdf.format(date);
            stepsCount.setText(stepsCountValueFormat);
            meterCount.setText(meterCountValueFormat);
            timeCount.setText(timeCountValueFormat);
            calCount.setText(calCountValueFormat);

            if (stepsCountValue>=mGoal){
                stepGoalBool = true;
                Toast.makeText(getContext(), "NIICCEEE", Toast.LENGTH_SHORT).show();
            }

            DayStepsIdentity dayStepsIdentity = new DayStepsIdentity(calCountValue,
                    stepsCountValue, timeCountValue ,meterCountValue , dateString, stepGoalBool);

            dayStepsIdentity.save();
        }
    }

    public static String timeConversion(int totalSeconds) {

        final int MINUTES_IN_AN_HOUR = 60;
        final int SECONDS_IN_A_MINUTE = 60;

        int seconds = totalSeconds % SECONDS_IN_A_MINUTE;
        int totalMinutes = totalSeconds / SECONDS_IN_A_MINUTE;
        int minutes = totalMinutes % MINUTES_IN_AN_HOUR;
        int hours = totalMinutes / MINUTES_IN_AN_HOUR;

        return hours + "ч. " + minutes + "м. ";
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        running = false;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
