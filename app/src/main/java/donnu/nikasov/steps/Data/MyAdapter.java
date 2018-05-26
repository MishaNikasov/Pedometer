package donnu.nikasov.steps.Data;

import android.content.Context;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import donnu.nikasov.steps.Fragments.StepsFragment;
import donnu.nikasov.steps.R;

/**
 * Created by Миша on 25.05.2018.
 */

public class MyAdapter extends BaseAdapter {

    private Context context;
    private List<DayStepsIdentity> dayStepsIdentityList;

    public MyAdapter(Context context, List<DayStepsIdentity> dayStepsIdentityList) {
        this.context = context;
        this.dayStepsIdentityList = dayStepsIdentityList;
    }

    @Override
    public int getCount() {
        return dayStepsIdentityList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView.inflate(context, R.layout.steps_statisctics_layout, null);

        TextView stepStatistic = (TextView) view.findViewById(R.id.stepStatistic);
        TextView stepDistance = (TextView) view.findViewById(R.id.stepDistance);
        TextView stepTime = (TextView) view.findViewById(R.id.stepTime);
        TextView stepCal = (TextView) view.findViewById(R.id.stepCal);
        TextView stepDay = (TextView) view.findViewById(R.id.stepDay);

        LinearLayout contentLayout = (LinearLayout) view.findViewById(R.id.contentLayout);

        double time = dayStepsIdentityList.get(position).getTime();

        Double d = new Double(time);
        int i = d.intValue();

        view.setTag(dayStepsIdentityList.get(position).getId());

        if (dayStepsIdentityList.get(position).isGoalComplete()){
            contentLayout.setBackgroundColor(0xff00ffff);
        }

        stepStatistic.setText(String.format("%.0f", (dayStepsIdentityList.get(position).getSteps())));
        stepDistance.setText(String.format("%.2f", dayStepsIdentityList.get(position).getDistance()));
        stepTime.setText(StepsFragment.timeConversion(i));
        stepCal.setText(String.format("%.1f",dayStepsIdentityList.get(position).getCal()));
        stepDay.setText(String.valueOf(dayStepsIdentityList.get(position).getDate()));

        return view;
    }
}
