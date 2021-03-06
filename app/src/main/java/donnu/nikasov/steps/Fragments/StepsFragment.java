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
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import donnu.nikasov.steps.Data.DayStepsIdentity;
import donnu.nikasov.steps.R;

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
    Sensor countSensor;
    private SensorManager sensorManager;
    private TextView calCount;
    private TextView stepsCount;
    private TextView timeCount;
    private TextView meterCount;
    private TextView stepGoal;
    private boolean running = false;

    private double calCountValue;
    private float stepsCountValue ;
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
    private SharedPreferences mSettings1;

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

        mSettings = PreferenceManager.getDefaultSharedPreferences(getContext());
        mSettings1 = getContext().getSharedPreferences("appData", Context.MODE_PRIVATE);

//        if ( mSettings1.getFloat("stepCounter", 0)){
//
//        }
        stepsCountValue = mSettings1.getFloat("stepCounter", 0);

        mGoal = Integer.valueOf(mSettings.getString("editGoal", ""));
        mWeight = Integer.valueOf(mSettings.getString("editWeight", ""));
        mGrowth = Integer.valueOf(mSettings.getString("editGrowth", ""));

        stepLenth = (mGrowth/100/4)+0.37;

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

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

        stepsCount.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector =
                    new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onDoubleTap(MotionEvent e) {
                            Log.d("TEST", "onDoubleTap");

                            DayStepsIdentity dayStepsIdentity = new DayStepsIdentity(calCountValue,
                                    stepsCountValue, timeCountValue ,meterCountValue , dateString, stepGoalBool);
                            dayStepsIdentity.save();
                            return super.onDoubleTap(e);
                        }
                    });
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        timeCount.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector =
                    new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onDoubleTap(MotionEvent e) {
                            Log.d("TEST", "onDoubleTap");
                            stepsCountValue = 1;
                            return super.onDoubleTap(e);
                        }
                    });
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        running = true;

        stepsCountValue = mSettings1.getFloat("stepCounter", 0);

        mGoal = Integer.valueOf(mSettings.getString("editGoal", ""));
        mWeight = Integer.valueOf(mSettings.getString("editWeight", ""));
        mGrowth = Integer.valueOf(mSettings.getString("editGrowth", ""));

        stepLenth = (mGrowth/100.0/4.0)+0.37;

        stepGoal.setText("Цель: " + mGoal);

        countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

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
        mSettings1.edit().putFloat("stepCounter", stepsCountValue).apply();
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
//            float value = event.values[0];
        stepsCountValue += 1;

        stepsCountValueFormat = String.format("%.0f", stepsCountValue);

        meterCountValue = (stepsCountValue)*stepLenth/1000;
        timeCountValue = meterCountValue/5*60*60;
        calCountValue = meterCountValue*0.45*mWeight;

        Double d = new Double(timeCountValue);
        int i = d.intValue();

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

        if (stepsCountValue==mGoal){
            stepGoalBool = true;
            Toast.makeText(getContext(), "NIICCEEE", Toast.LENGTH_SHORT).show();
        }

        mSettings1.edit().putFloat("stepCounter", stepsCountValue).apply();
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
