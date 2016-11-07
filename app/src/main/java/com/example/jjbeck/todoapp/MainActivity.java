package com.example.jjbeck.todoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.R.attr.name;

public class MainActivity extends AppCompatActivity {

    ArrayList<String>    todoItems;
    ArrayAdapter<String> aToDoAdapter;
    ListView             lvItems;
    EditText             etEditText;

    // REQUEST_CODE can be any value we like, used to determine the result type later
    private final int    REQUEST_CODE_EDIT_ITEM      = 20;
    private final String DATA_EXTRA_STRING_TO_EDIT   = "stringToEdit";
    private final String DATA_EXTRA_POSITION_TO_EDIT = "positionToEdit";
    private static final String TODO_FILENAME        = "new-todo.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateArrayItems();
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(aToDoAdapter);
        etEditText = (EditText) findViewById(R.id.etEditText);
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                todoItems.remove(position);
                processListUpdate();
                return true;
            }
        });
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String strToEdit = todoItems.get(position);
                launchEditView(strToEdit, position);
            }
        });
    }

    private void processListUpdate() {
        aToDoAdapter.notifyDataSetChanged();
        writeItems();
    }


    public void populateArrayItems() {
        readItems();
        aToDoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems);
    }

    // ---------------------------------------------------------
    // Management of the Add button actions
    // ---------------------------------------------------------
    public void onAddItem(View view) {
        aToDoAdapter.add(etEditText.getText().toString());
        etEditText.setText("");
        writeItems();
    }

    // ---------------------------------------------------------
    // These methods are for the data persistence to file.
    // ---------------------------------------------------------
    private void readItems() {
        File filesDir = getFilesDir();
        File file = new File(filesDir, TODO_FILENAME);
        try {
            FileUtils.touch(file); // needed to create file first time around.
            todoItems = new ArrayList<String>(FileUtils.readLines(file));
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }
    private void writeItems() {
        File filesDir = getFilesDir();
        File file = new File(filesDir, TODO_FILENAME);
        try {
            FileUtils.writeLines(file, todoItems);
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    // ---------------------------------------------------------
    // These methods are for the edit text activity
    // ---------------------------------------------------------

    // Launch the edit view/activity when user clicks on a list item.
    private void launchEditView(String strToEdit, int positionToEdit) {
        // first parameter is the context, second is the class of the activity to launch
        Intent editIntent = new Intent(this, EditItemActivity.class);

        // Pass the data to the edit activity.
        // Best to cache the position getting edited on the activity and get back later.
        editIntent.putExtra(DATA_EXTRA_STRING_TO_EDIT,   strToEdit);
        editIntent.putExtra(DATA_EXTRA_POSITION_TO_EDIT, positionToEdit);
        startActivityForResult(editIntent, REQUEST_CODE_EDIT_ITEM);
    }

    // MainActivity.java, time to handle the result of the sub-activity EditTextActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE_EDIT_ITEM is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_EDIT_ITEM) {
            // Extract name value from result extras
            String editedString   = data.getExtras().getString(DATA_EXTRA_STRING_TO_EDIT);
            int    positionEdited = data.getExtras().getInt(DATA_EXTRA_POSITION_TO_EDIT);

            // Overwrite the value at this position.
            todoItems.set(positionEdited, editedString);
            processListUpdate();

            // Toast the name to display temporarily on screen
            // I like this for debugging.  Pretty neat.
            String toastMsg = "Updating Item #" + positionEdited + " to new value: " + editedString;
            Toast.makeText(this, toastMsg, Toast.LENGTH_SHORT).show();
        }
    }
}
