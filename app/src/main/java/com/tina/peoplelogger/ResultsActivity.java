package com.tina.peoplelogger;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResultsActivity extends Activity {


    private ArrayList results = new ArrayList();
    private ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        dbOpenHelper db = new dbOpenHelper(this);

        int total = db.getPLogCount();
        final TextView Entries = (TextView) findViewById(R.id.entries);
        Entries.setText(String.valueOf(total));

        Log.d("Reading: ", "Reading all entries..");
        List<PLog> plogs = db.getTop5PLogs();

        //for (PLog cn : plogs) {
            //String log = "Total " + total +" ,Id: "+cn.getID()+" ,Group: " + cn.getGroup() + " ,Date: " + cn.getDatetime();
            // Write entries to log only for testing
            //Log.d("Results: ", log);
        //}

        final ListView top5list = (ListView) findViewById(R.id.top5list);
        //List<PLog> test = db.getTop5PLogs();


        //final String[] COUNTRIES = new String[] {
               // "Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra"
        //};

        //connects list to adapter
       // ArrayAdapter<PLog> adapter = new ArrayAdapter<PLog>(this, android.R.layout.simple_list_item_1, COUNTRIES);
        //set the listview to use the array adapter
        //top5ListView.setAdapter(adapter);
        int i = 0;
        String[] mylist = new String[2];
        for (PLog cn : plogs) {
            i++;
            mylist[0] = cn.getGroup(); //this adds an element to the list.
            mylist[1] = "" + cn.getID() + "";
        }
        ArrayList<String> testlist = new ArrayList<String>();
        testlist.addAll(Arrays.asList(mylist));

        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, testlist);
        top5list.setAdapter(listAdapter);

    }

    public void backupDatabase(View view) throws IOException {
        if (Environment.getExternalStorageState() != null) {
            File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PLogBackup");
            if (!dir.exists()) {
                dir.mkdir();
            }

            String fromPath = "";
            if (android.os.Build.VERSION.SDK_INT >= 4.2) {
                fromPath = getApplicationInfo().dataDir + "/databases/" + "peopleLoggerDb";
            } else {
                fromPath = "/data/data/" + getPackageName() + "/databases/" + "peopleLoggerDb";
            }

            String toPath = dir.getAbsolutePath() + "/peopleLoggerDb";
            fileCopy(new File(fromPath), new File(toPath));
            //if (fileExists(toPath)) {
            //Toast.makeText(this, "Backup successful", Toast.LENGTH_LONG).show();
            //}
        }
    }

    public boolean fileExists(String fname){
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }

    public void fileCopy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

}
