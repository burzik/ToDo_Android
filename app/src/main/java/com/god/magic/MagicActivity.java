package com.god.magic;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Date;

/**
 * HISTORY
 * 	Date			Author				Comments
 * 	23.02.2017		Eduard Arefjev 	    Main activity
 */

public class MagicActivity extends AppCompatActivity {

    TodoAdapter mainAdapter;
    private int currentTheme = 0;
    Firebase firebase;
    //Firebase firebase = new Firebase("https://todo-b43fa.firebaseio.com/");


    //EA hide keyboard
    public static void hideKeyboard (Activity activity) {
        if (activity != null && activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    //EA set theme
    @Override
    public void setTheme(@StyleRes int theme) {
        currentTheme = theme;
        super.setTheme(theme);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.magic_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean itemSelected = super.onOptionsItemSelected(item);

        switch(item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this, SharedPreferences.class);
                this.startActivity(intent);
                break;
            default:
                return itemSelected;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(ToDoManager.getTheme());
        setContentView(R.layout.todo_layout);

        //EA Load DB
        ToDoManager.createContext(getApplicationContext());
        ToDoManager.LoadFromDB();

        //EA init adapter
        final TodoAdapter adapter = new TodoAdapter(this);
        mainAdapter = adapter;

        ListView listView = (ListView) findViewById(R.id.ListViewTodo);
        listView.setAdapter(adapter);

        final EditText editText = (EditText) findViewById(R.id.EditTextToDo);
        Button button = (Button) findViewById(R.id.AddButtonTodo);
        //EA add new record
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ToDoManager.add(editText.getText().toString(), "", false, new Date (), null);
                adapter.notifyDataSetChanged();

                //adapter.getPosition();
                firebase = new Firebase("https://todo-b43fa.firebaseio.com/Elem");
            /*    firebase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String text = dataSnapshot.getValue(String.class);

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });*/
                //firebase.setValue(editText.getText().toString(),"cust");
                //firebase.setValue(ToDoManager.get(position));
                //firebase.
                editText.setText("");
                hideKeyboard(MagicActivity.this);


            }
        });
    }

    private void saveFB(){
        final EditText editText = (EditText) findViewById(R.id.EditTextToDo);
        String label = editText.getText().toString().trim();

        //Firebase user = firebase.get
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentTheme != ToDoManager.getTheme()) {
           recreate();
        }
        mainAdapter.notifyDataSetChanged();
    }
}


