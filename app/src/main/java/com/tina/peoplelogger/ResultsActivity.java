package com.tina.peoplelogger;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class ResultsActivity extends Activity {

    private ListView top5list;
    public static String PACKAGE_NAME;
    public static ApplicationInfo PACKAGE_APPINFO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        PACKAGE_NAME = getPackageName();
        PACKAGE_APPINFO = getApplicationInfo();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        dbOpenHelper db = new dbOpenHelper(this);

        int total = db.getPLogCount();
        final TextView Entries = (TextView) findViewById(R.id.entries);
        Entries.setText(String.valueOf(total));

        top5list = (ListView) findViewById(R.id.top5list);
        ArrayList<String> top5listarr = db.getTop5PLogs();

        //connects list to adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.listview_layout, top5listarr);
        //set the listview to use the array adapter
        top5list.setAdapter(adapter);
    }

    public void onClickImportBtn(View view) throws IOException {
        dbOpenHelper db = new dbOpenHelper(this);
        int status = db.importDatabase();
        if (status == 1) {
            Toast.makeText(this,"Import successful", Toast.LENGTH_LONG).show();
            //refresh the activity display
            finish();
            startActivity(getIntent());
        } else {
            Toast.makeText(this,"Import FAILED!!", Toast.LENGTH_LONG).show();
        }
    }

    public void onClickBackupBtn(View view) throws IOException {
        dbOpenHelper db = new dbOpenHelper(this);
        int status = db.backupDatabase();
        if (status == 1) {
            Toast.makeText(this,"Backup successful", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this,"Backup FAILED!!", Toast.LENGTH_LONG).show();
        }
    }

}
