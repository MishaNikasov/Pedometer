package donnu.nikasov.steps.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

import donnu.nikasov.steps.Data.DayStepsIdentity;
import donnu.nikasov.steps.Data.MyAdapter;
import donnu.nikasov.steps.R;


public class StatisticsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ListView staticticsList;
    private MyAdapter adapter;

    private String calStatistic;
    private String distanceStatistic;
    private String timeStatistic;
    private String stepsStatistics;

    private TextView calStatisticView;
    private TextView distanceStatisticView;
    private TextView timeStatisticView;
    private TextView stepsStatisticsView;

    private Button button;

    private OnFragmentInteractionListener mListener;

    public StatisticsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static StatisticsFragment newInstance(String param1, String param2) {
        StatisticsFragment fragment = new StatisticsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        calStatisticView = (TextView) view.findViewById(R.id.calStatistic);
        distanceStatisticView = (TextView) view.findViewById(R.id.distanceStatistic);
        timeStatisticView = (TextView) view.findViewById(R.id.timeStatistic);
        stepsStatisticsView = (TextView) view.findViewById(R.id.stepsStatistics);

        staticticsList = (ListView) view.findViewById(R.id.statisticList);

        button = (Button) view.findViewById(R.id.button1);

        List<DayStepsIdentity> list = DayStepsIdentity.listAll(DayStepsIdentity.class);
        adapter = new MyAdapter(getContext(), list);
        staticticsList.setAdapter(adapter);

        staticticsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), view.getTag().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        double cal = 0;
        double steps = 0;
        double distance = 0;
        double time = 0;

        for (DayStepsIdentity day : list) {
            cal += day.getCal();
            distance += day.getDistance();
            steps += day.getSteps();
            time += day.getTime();
        }

        Double d = new Double(time);
        int i = d.intValue();

        stepsStatistics = String.format("%.0f", steps);
        distanceStatistic = String.format("%.2f", distance);
        timeStatistic = StepsFragment.timeConversion(i);
        calStatistic = String.format("%.1f", cal);

        calStatisticView.setText(calStatistic);
        distanceStatisticView.setText(distanceStatistic);
        timeStatisticView.setText(timeStatistic);
        stepsStatisticsView.setText(stepsStatistics);

        stepsStatisticsView.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector =
                new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        Log.d("TEST", "onDoubleTap");
                        if (button.getVisibility()==View.VISIBLE){
                            button.setVisibility(View.GONE);
                        }
                        else
                            button.setVisibility(View.VISIBLE);
                        return super.onDoubleTap(e);
                    }
                });
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DayStepsIdentity.deleteAll(DayStepsIdentity.class);
                adapter.notifyDataSetChanged();
                List<DayStepsIdentity> list = DayStepsIdentity.listAll(DayStepsIdentity.class);
                adapter = new MyAdapter(getContext(), list);
                staticticsList.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                calStatisticView.setText("0");
                distanceStatisticView.setText("0");
                timeStatisticView.setText("0ч. 0м.");
                stepsStatisticsView.setText("0");
            }
        });

        return view;
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
    public void onResume() {
        super.onResume();
        button.setVisibility(View.GONE);
        List<DayStepsIdentity> list = DayStepsIdentity.listAll(DayStepsIdentity.class);
        adapter = new MyAdapter(getContext(), list);
        staticticsList.setAdapter(adapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
