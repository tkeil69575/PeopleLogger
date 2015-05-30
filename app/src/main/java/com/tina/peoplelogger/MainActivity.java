package com.tina.peoplelogger;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] groups_list = getResources().getStringArray(R.array.groups_list);
        // Create Instance of ArrayAdapter and bind it with groups_list array.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, groups_list);

        // Link the Auto Complete Text View
        AutoCompleteTextView autoText = (AutoCompleteTextView) findViewById(R.id.group);
        autoText.setThreshold(1); // Start From 1st Character
        autoText.setAdapter(adapter);

        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this, R.array.age_ranges, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter2);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView myText = (TextView) view;
        Toast.makeText(this,"You selected "+myText.getText(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_results) {
            Toast.makeText(this,"You selected the results", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
