package com.example.sqliteexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity {
    DBmain dBmain;
    SQLiteDatabase sqLiteDatabase;
    String[] fname, lname; //arrays that hold all of the first and last name entries
    int[] id; //an array that holds all of the id's of the entries (which corresponds to its index in the table)
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        dBmain = new DBmain(this);
        findid();
        displaydata();

    }

    private void displaydata() {
        sqLiteDatabase = dBmain.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select *from subject", null);
        if (cursor.getCount() > 0) { //if there's at least one entry in the database
            id = new int[cursor.getCount()];
            fname = new String[cursor.getCount()];
            lname = new String[cursor.getCount()];
            int i = 0;

            while (cursor.moveToNext()) {
                id[i] = cursor.getInt(0); //getting the first entry that the Cursor is pointing to
                fname[i] = cursor.getString(1); //getting the second entry that the Cursor is pointing to
                lname[i] = cursor.getString(2); //getting the third entry that the Cursor is pointing to
                i++;
            }
            CustAdapter custAdapter = new CustAdapter();
            lv.setAdapter(custAdapter);
        }
    }

    private void findid() {
        lv = (ListView) findViewById(R.id.lv);
    }

    private class CustAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return fname.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            TextView txtfname, txtlname;
            ImageButton txt_edit, txt_delete;
            CardView cardview;
            convertView = LayoutInflater.from(MainActivity2.this).inflate(R.layout.singledata, parent, false);
            txtfname = convertView.findViewById(R.id.txt_fname);
            txtlname = convertView.findViewById(R.id.txt_lname);
            txt_edit = convertView.findViewById(R.id.txt_edti);
            txt_delete = convertView.findViewById(R.id.txt_delete);
            cardview = convertView.findViewById(R.id.cardview);
            cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //background random color
                    //Random random = new Random();
                    cardview.setCardBackgroundColor(Color.argb(255, 171, 217, 250));
                    txt_delete.setVisibility(View.VISIBLE);
                    txt_edit.setVisibility(View.VISIBLE);
                    txtfname.setVisibility(View.GONE);
                    txtlname.setVisibility(View.GONE);
                }
            });
            txtfname.setText(fname[position]);
            txtlname.setText(lname[position]);
            txt_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", id[position]);
                    bundle.putString("fname", fname[position]);
                    bundle.putString("lname", lname[position]);
                    Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                    intent.putExtra("studata", bundle); //sending the bundle to MainActivity
                    startActivity(intent);
                }
            });
            txt_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sqLiteDatabase = dBmain.getReadableDatabase();
                    long recremove = sqLiteDatabase.delete("subject", "id=" + id[position], null);
                    if (recremove != -1) {
                        Cursor cursor = sqLiteDatabase.rawQuery("select *from subject", null);
                        if (cursor.getCount() > 0) {
                            Toast.makeText(MainActivity2.this, "The entry was successfully deleted!", Toast.LENGTH_SHORT).show();
                            //startActivity(new Intent(MainActivity2.this, MainActivity.class));
                            displaydata();
                        } else {
                            Toast.makeText(MainActivity2.this, "The entry was successfully deleted!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity2.this, MainActivity.class));
                        }

                    }
                }
            });
            return convertView;
        }
    }
}