package com.example.jjbeck.todoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import static com.example.jjbeck.todoapp.R.id.etEditText;

public class EditItemActivity extends AppCompatActivity {

    private final String DATA_EXTRA_STRING_TO_EDIT   = "stringToEdit";
    private final String DATA_EXTRA_POSITION_TO_EDIT = "positionToEdit";
    private final int    DEFAULT_POSITION            = -1;

    EditText etEditText;
    int      positionGettingEdited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        // Get the string value to edit passed into via the Bundle.
        String strToEdit      = getIntent().getStringExtra(DATA_EXTRA_STRING_TO_EDIT);
        positionGettingEdited = getIntent().getIntExtra(DATA_EXTRA_POSITION_TO_EDIT, DEFAULT_POSITION);

        // Find the EditText
        etEditText = (EditText) findViewById(R.id.teEditText);

        // Set the string value to edit into the EditText.
        etEditText.setText(strToEdit);

    }

    // EditItemActivity.java -- launched for a result - the edited value!
    // User has clicked the Save Button.
    // Collect value in the view and pass back to the main activity.
    // Finally close and return to the main.
    public void onSaveItem(View v) {

        // Prepare data intent
        Intent data = new Intent();

        // Pass relevant data back as a result
        // TODO: any reason to not just use the same "Extra Name" to return the edited value!
        // TODO: Put these data extra names into a constants file or config file.
        data.putExtra(DATA_EXTRA_STRING_TO_EDIT,   etEditText.getText().toString());
        data.putExtra(DATA_EXTRA_POSITION_TO_EDIT, positionGettingEdited);

        // Activity finished ok, return the data
        setResult(RESULT_OK, data); // set result code and bundle data for response

        // closes the activity and returns data to parent
        this.finish();
    }
}
