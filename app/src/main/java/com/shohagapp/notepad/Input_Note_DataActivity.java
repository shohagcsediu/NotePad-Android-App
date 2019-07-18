package com.shohagapp.notepad;

import android.support.v7.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Input_Note_DataActivity extends AppCompatActivity {
    ExampleDBHelper db;
    EditText n_title;
    EditText n_text;
    String title,text;
    public static SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_note_data);

        db=new ExampleDBHelper(getApplicationContext());
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        n_title= findViewById(R.id.title);
        n_text= findViewById(R.id.text);

        Button clickButton = findViewById(R.id.clickButton);
        clickButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                title = n_title.getText().toString();
                text = n_text.getText().toString();

                if(title.length() == 0){
                    SharedPreferences.Editor editor = pref.edit();

                    int idName = pref.getInt("name", 0);
                    idName++;
                    title="new document "+idName ;
                    editor.putInt("name",idName);
                    editor.apply();

                }

                if( text.length() == 0){
                    Toast.makeText(getApplicationContext(), "Title or Description is Empty ?",
                            Toast.LENGTH_SHORT).show();
                }
                else
                {
                    db.insertPerson(title,text);
                    Toast.makeText(getApplicationContext(), "Note Saved Successfully", Toast.LENGTH_LONG).show();
                    finish();}
            }
        });
    }

}

