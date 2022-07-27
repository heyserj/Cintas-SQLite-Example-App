package com.example.sqliteexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    DBmain dBmain;
    SQLiteDatabase sqLiteDatabase;
    EditText lname, fname;
    Button submit, display, edit;
    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dBmain = new DBmain(this);

        //create object
        findid();

        //disabling the edit button because the user hasn't chosen an entry
        //to edit yet
        edit.setEnabled(false);
        edit.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);

        insertData();
        cleardata();
        editdata();
    }

    private void editdata() {
        if (getIntent().getBundleExtra("studata") != null) {
            Bundle bundle = getIntent().getBundleExtra("studata");
            id = bundle.getInt("id");
            fname.setText(bundle.getString("fname"));
            lname.setText(bundle.getString("lname"));

            //edit.setVisibility(View.VISIBLE);
            edit.getBackground().setColorFilter(null);
            edit.setEnabled(true);
            //submit.setVisibility(View.GONE);
            submit.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
            submit.setEnabled(false);
        }
    }

    private void cleardata() {
        fname.setText(""); //clearing the text in the two editText boxes on the main activity
        lname.setText("");
    }

    private void insertData() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues = new ContentValues(); //creating a new row that we can insert into the database
                contentValues.put("fname", fname.getText().toString().trim());
                contentValues.put("lname", lname.getText().toString().trim());

                sqLiteDatabase = dBmain.getWritableDatabase();
                Long recid = sqLiteDatabase.insert("subject", null, contentValues);
                if (recid != null) { //the new row was successfully inserted into the database
                    Toast.makeText(MainActivity.this, "The insert was successful!", Toast.LENGTH_SHORT).show();
                    cleardata();
                } else { //there was an error inserting the new row
                    Toast.makeText(MainActivity.this, "There was an error inserting. Try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        display.setOnClickListener(new View.OnClickListener() { //want to display the entries in the database
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("fname", fname.getText().toString().trim());
                contentValues.put("lname", lname.getText().toString().trim());

                sqLiteDatabase = dBmain.getWritableDatabase();
                long recid = sqLiteDatabase.update("subject", contentValues, "id=" + id, null);
                if (recid != -1) {
                    Toast.makeText(MainActivity.this, "The update was successful!", Toast.LENGTH_SHORT).show();
                    //submit.setVisibility(View.VISIBLE);
                    submit.getBackground().setColorFilter(null);
                    submit.setEnabled(true);
                    edit.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                    edit.setEnabled(false);
                    cleardata();
                } else {
                    Toast.makeText(MainActivity.this, "There was an error updating. Try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void findid() {
        fname = (EditText) findViewById(R.id.fname);
        lname = (EditText) findViewById(R.id.lname);
        submit = (Button) findViewById(R.id.submit_btn);
        display = (Button) findViewById(R.id.display_btn);
        edit = (Button) findViewById(R.id.edit_btn);
    }
}