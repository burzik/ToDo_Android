package com.god.magic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

/**
 * HISTORY
 * 	Date			Author				Comments
 * 	19.04.2017		Eduard Arefjev 	    Shared preferences activity
 */

public class SharedPreferences extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(ToDoManager.getTheme());
        setContentView(R.layout.linear_shared_pref);

        RadioButton colordefault = (RadioButton) findViewById(R.id.color_def);
        RadioButton colorRed = (RadioButton) findViewById(R.id.color_red);
        RadioButton colorGreen = (RadioButton) findViewById(R.id.color_green);
        RadioButton colorBlue = (RadioButton) findViewById(R.id.color_blue);

        //EA set checked in radio group
        if (2131230886 == ToDoManager.getTheme()) {
            colorRed.setChecked(true);
        }
        if (2131230885 == ToDoManager.getTheme()) {
            colorGreen.setChecked(true);
        }
        if (2131230884 == ToDoManager.getTheme()) {
            colorBlue.setChecked(true);
        }
        if (2131230883 == ToDoManager.getTheme()) {
            colordefault.setChecked(true);
        }
    }

    //EA radio button event
    public void onRadioButtonClicked (View view){
        boolean checked = ((RadioButton)view).isChecked();

        //EA Get radio and set theme
        switch (view.getId()){
            //EA color Default
            case R.id.color_def:
                if (checked) {
                    ToDoManager.setTheme(R.style.AppTheme);
                } break;
            //EA color Red
            case R.id.color_red:
                if (checked) {
                    ToDoManager.setTheme(R.style.AppTheme_Red);
                } break;
            //EA color Green
            case R.id.color_green:
                if (checked) {
                    ToDoManager.setTheme(R.style.AppTheme_Green);
                } break;
            //EA color Blue
            case R.id.color_blue:
                if (checked) {
                    ToDoManager.setTheme(R.style.AppTheme_Blue);
                } break;
        }
        SharedPreferences.this.recreate();
    }
}


