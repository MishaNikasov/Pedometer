package donnu.nikasov.steps.Data;

import com.orm.SugarRecord;

/**
 * Created by Миша on 25.05.2018.
 */

public class DayStepsIdentity extends SugarRecord {

    private double cal;
    private double steps;
    private double time;
    private double distance;
    private String  date;
    boolean goalComplete;

    public DayStepsIdentity() {
    }

    public DayStepsIdentity(double cal, double steps, double time, double distance, String date, boolean goalComplete) {
        this.cal = cal;
        this.steps = steps;
        this.time = time;
        this.distance = distance;
        this.date = date;
        this.goalComplete = goalComplete;
    }

    @Override
    public String toString() {
        return "DayStepsIdentity{" +
                "cal=" + cal +
                ", steps=" + steps +
                ", time=" + time +
                ", distance=" + distance +
                '}';
    }

    public double getCal() {
        return cal;
    }

    public void setCal(double cal) {
        this.cal = cal;
    }

    public double getSteps() {
        return steps;
    }

    public void setSteps(double steps) {
        this.steps = steps;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isGoalComplete() {
        return goalComplete;
    }

    public void setGoalComplete(boolean goalComplete) {
        this.goalComplete = goalComplete;
    }
}
