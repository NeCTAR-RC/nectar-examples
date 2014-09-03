package nz.co.paulo;

import java.util.ArrayList;

/**
 * Created by Martin Paulo
 * An implementation of the Presentation Model pattern (http://martinfowler.com/eaaDev/PresentationModel.html).
 * It's largely used to transfer values into the Mustache template.
 */
public class History {

    String alarmName = "";

    final ArrayList<AlarmDetails> history = new ArrayList<>();

    public History(String alarmName) {
        this.alarmName = alarmName;
    }

    public ArrayList<AlarmDetails> getHistory() {
        return history;
    }

    public String getAlarmName() {
        return alarmName;
    }

    @Override
    public String toString() {
        return "Name: " + alarmName + " records: " + history.size();
    }
}
