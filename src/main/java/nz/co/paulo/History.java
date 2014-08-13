package nz.co.paulo;

import java.util.ArrayList;

/**
 * Created by martin paulo on 13/08/2014.
 */
public class History {

    String alarmName = "";
    ArrayList<AlarmDetails> history = new ArrayList();

    public History(String alarmName) {
        this.alarmName = alarmName;
    }

    public ArrayList<AlarmDetails> getHistory() {
        return history;
    }




}
