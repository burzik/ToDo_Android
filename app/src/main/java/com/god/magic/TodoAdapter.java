package com.god.magic;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import static com.god.magic.R.layout.relative_list_view;

/**
 * HISTORY
 * 	Date			Author				Comments
 * 	23.02.2017		Eduard Arefjev 		Basic functions for adapter
 */

class TodoAdapter extends ArrayAdapter<ToDoList> {

    private Activity activity;

    //EA init view holder
    private class ViewHolder {
        TextView title;
        TextView date;
        CheckBox check;
        Button button;
    }

    TodoAdapter(Activity activity) {
        super(activity, R.layout.relative_list_view, ToDoManager.values());
        this.activity = activity;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull final ViewGroup parent) {
        final ViewHolder viewHolder;

        //EA first run
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(relative_list_view, null);

            viewHolder.title = (TextView) convertView.findViewById(R.id.RelativeMainText);
            viewHolder.check = (CheckBox) convertView.findViewById(R.id.RelativeCheckbox);
            viewHolder.date = (TextView) convertView.findViewById(R.id.RelativeDate);
            viewHolder.button = (Button) convertView.findViewById(R.id.RelativeButton);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //EA set elements
        viewHolder.title.setText(ToDoManager.get(position).name);
        viewHolder.date.setText(DateFormat.format("dd.MM.yyyy", ToDoManager.get(position).date));
        viewHolder.check.setChecked(ToDoManager.get(position).getSelected());
        //EA checked element
        viewHolder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToDoManager.get(position).check ^= true;
                ToDoManager.notify(position);
            }
        });
        //EA delete element with alert message
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                builder.setTitle(R.string.DeleteMsg);
                builder.setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //EA Do nothing
                    }
                });
                builder.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToDoManager.remove(position);
                        ((TodoAdapter) ((ListView) view.getParent().getParent()).getAdapter()).notifyDataSetChanged();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        //EA drilldown to next activity
        convertView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ToDoMoreInfo.class);
                intent.putExtra("position", position);
                activity.startActivity(intent);
            }
        });
        return convertView;
    }
}
