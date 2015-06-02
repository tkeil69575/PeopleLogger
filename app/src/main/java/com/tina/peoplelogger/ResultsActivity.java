package com.tina.peoplelogger;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

        //Log.d("Reading: ", "Reading all entries..");
        //List<PLog> plogs = db.getAllPLogs();

        //for (PLog cn : plogs) {
            //String log = "Total " + total +" ,Id: "+cn.getID()+" ,Group: " + cn.getGroup() + " ,Date: " + cn.getDatetime();
            // Write entries to log only for testing
            //Log.d("Results: ", log);
        //}

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
