package com.tina.peoplelogger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {

    private AutoCompleteTextView groupSuggest;
    private Spinner ageRangeSelect;
    private EditText noteText;
    private String Sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] groups_list = getResources().getStringArray(R.array.groups_list);
        // Create Instance of ArrayAdapter and bind it with groups_list array.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, groups_list);

        // Link the Auto Complete Text View
        groupSuggest = (AutoCompleteTextView) findViewById(R.id.group);
        groupSuggest.setThreshold(1); // Start From 1st Character
        groupSuggest.setAdapter(adapter);

        //gender
        RadioGroup genderGroup = (RadioGroup) findViewById(R.id.radioGroup);
        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton) findViewById(checkedId);
                Sex = checkedRadioButton.getText().toString();
            }
        });

        //age range
        ageRangeSelect = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this, R.array.age_ranges, android.R.layout.simple_spinner_item);
        ageRangeSelect.setAdapter(adapter2);

        //notes
        noteText = (EditText)findViewById(R.id.notes);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Goto new activity listed in menu
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_results) {
            Intent launchNewIntent = new Intent(MainActivity.this,ResultsActivity.class);
            startActivityForResult(launchNewIntent, 0);
            //Toast.makeText(this,"You selected the results", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void onButtonClick(View view) {
        //Assign variables
        String DateTime = getDateTime();
        String GroupName = groupSuggest.getText().toString();
        String AgeRange = ageRangeSelect.getSelectedItem().toString();
        String Notes = noteText.getText().toString();

        //Clean up some text before we enter it into the db
        Sex = Sex.replaceAll("Male","M");
        Sex = Sex.replaceAll("Female","F");
        AgeRange = AgeRange.replaceAll("between","");
        AgeRange = AgeRange.replaceAll("over ",">");
        AgeRange = AgeRange.replaceAll("under ","<");

        //Toast.makeText(this,"You selected "+GroupName+" "+Sex+" "+AgeRange+" "+DateTime+" "+Notes, Toast.LENGTH_LONG).show();

        if (GroupName.isEmpty())  {
            Toast.makeText(this,"Please enter a group", Toast.LENGTH_LONG).show();
        } else {
            //insert into database
            //add to database
            dbOpenHelper db = new dbOpenHelper(this);
            Log.d("Insert: ", "Inserting ..");
            db.addPLog(new PLog(DateTime, GroupName, Sex, AgeRange, Notes));
            Toast.makeText(this,"Log entry added to database", Toast.LENGTH_LONG).show();

            //clear text and selection for next entry
            groupSuggest.setText(null);
            ageRangeSelect.setSelection(0);
            noteText.setText(null);
        }
    }

    // current Datetime
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

}
