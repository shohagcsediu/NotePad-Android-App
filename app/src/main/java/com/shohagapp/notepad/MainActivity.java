package com.shohagapp.notepad;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    ListView mobile_list;
    ExampleDBHelper mydb;
    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    public static final String INPUT_COLUMN_ID = "_id";
    public static final String INPUT_COLUMN_Title = "title";
    public static final String INPUT_COLUMN_Text = "text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mydb = new ExampleDBHelper(getApplicationContext());
        mobile_list = findViewById(R.id.mobile_list);
        loadData();
    }

    @Override
    public void onResume(){
        super.onResume();
        loadData();

    }


    public void addNew(View view) {
        Intent intent = new Intent(this, Input_Note_DataActivity.class);
        startActivity(intent);
    }


    public void loadData() {
        dataList.clear();
        Cursor cursor = mydb.getAllPersons();
        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {

                HashMap<String, String> map = new HashMap<String, String>();
                map.put(INPUT_COLUMN_ID, cursor.getString(cursor.getColumnIndex(INPUT_COLUMN_ID)));
                map.put(INPUT_COLUMN_Title, cursor.getString(cursor.getColumnIndex(INPUT_COLUMN_Title)));
                map.put(INPUT_COLUMN_Text, cursor.getString(cursor.getColumnIndex(INPUT_COLUMN_Text)));


                dataList.add(map);

                cursor.moveToNext();
            }
        }


        NoticeAdapter adapter = new NoticeAdapter(MainActivity.this, dataList);
        mobile_list.setAdapter(adapter);

        mobile_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent i = new Intent(MainActivity.this, NoteViewActivity.class);
                i.putExtra("id", dataList.get(+position).get(INPUT_COLUMN_ID));
                i.putExtra("title", dataList.get(+position).get(INPUT_COLUMN_Title));
                i.putExtra("text", dataList.get(+position).get(INPUT_COLUMN_Text));
                startActivity(i);

            }
        });


        //  list item long press to delete -------------start-----------

        mobile_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) {
                return onLongListItemClick(v,pos,id);
            }
            protected boolean onLongListItemClick(View v, final int pos, long id) {

                /////Display Dialog Here.......................

                AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();
                alertDialog.setTitle("Delete this note...");
                alertDialog.setMessage("Are you sure?");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String a=dataList.get(+pos).get(INPUT_COLUMN_ID);
                        mydb.deleteSingleContact(a);
                        loadData();
                    }
                });
                alertDialog.show();
                return true;
            }});

        //--------finish------------
    }
}

class NoticeAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;

    public NoticeAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data = d;
    }

    public int toInt(String s)
    {
        return Integer.parseInt(s);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        NoticeViewHolder holder;
        if (convertView == null) {
            holder = new NoticeViewHolder();
            convertView = LayoutInflater.from(activity).inflate(
                    R.layout.activity_listview, null);

            holder.title = convertView.findViewById(R.id.list_title);
            holder.text = convertView.findViewById(R.id.list_text);

            convertView.setTag(holder);
        } else {
            holder = (NoticeViewHolder) convertView.getTag();
        }
        holder.title.setId(position);
        holder.text.setId(position);

        HashMap<String, String> song;
        song = data.get(position);

        holder.title.setText(Html.fromHtml(song.get(MainActivity.INPUT_COLUMN_Title)));
        holder.text.setText(Html.fromHtml(song.get(MainActivity.INPUT_COLUMN_Text)));

        return convertView;

    }
}

class NoticeViewHolder {
    TextView title, text;
}