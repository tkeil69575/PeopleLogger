package com.tina.peoplelogger;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

public class ResultsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        dbOpenHelper db = new dbOpenHelper(this);

        int total = db.getPLogCount();
        final TextView Entries = (TextView) findViewById(R.id.entries);
        Entries.setText(String.valueOf(total));

        Log.d("Reading: ", "Reading all entries..");
        List<PLog> plogs = db.getAllPLogs();

        for (PLog cn : plogs) {
            String log = "Total " + total +" ,Id: "+cn.getID()+" ,Group: " + cn.getGroup() + " ,Date: " + cn.getDatetime();
            // Write entries to log only for testing
            Log.d("Results: ", log);
        }

    }
}
