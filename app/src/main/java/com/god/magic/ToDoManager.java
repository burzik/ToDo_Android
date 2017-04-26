package com.god.magic;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

/**
 * HISTORY
 * 	Date			Author				Comments
 * 	18.04.2017		Eduard Arefjev 		Data manager
 */

class ToDoManager {
    //EA init static variables
    private static ArrayList<ToDoList> values;
    private static ToDoDB db = null;

    private static int theme_id = R.style.AppTheme;
    private static SharedPreferences.Editor preferencesEditor = null;
    private static Firebase firebase;

    private ToDoManager() {
        values = new ArrayList<>();
    }

    static void createContext(Context context) {

        firebase = new Firebase("https://todo-b43fa.firebaseio.com/Elem");
        db = new ToDoDB(context);
        String PREF_NAME = "Preferences";
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        preferencesEditor = preferences.edit();
        preferencesEditor.apply(); //Check it pls
        theme_id = preferences.getInt("theme_id", R.style.AppTheme);
    }
    //EA load from db
    static void LoadFromDB() {
        try {
            values = db.getAllElems();
            firebase = new Firebase("https://todo-b43fa.firebaseio.com/");

            firebase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    /*for (DataSnapshot child: dataSnapshot.getChildren()) {
                        ToDoList todoList = child.getValue(ToDoList.class);
                        values.add(todoList);
                    }*/
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }

            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //EA add elem
    static void add(String name, String info, boolean check, Date date, Bitmap photo) {
        try {
            ToDoList elem = new ToDoList(name, info, check, date, photo);
            db.addElem(elem);
            values.add(elem);


            firebase = new Firebase("https://todo-b43fa.firebaseio.com/Elem" +  elem.getId());
            firebase.setValue(elem);

        /*  ToDoList elemz = new ToDoList(name, info, date, "");
            DatabaseReference firebasez;
            firebasez = FirebaseDatabase.getInstance().getReference().child("Elem");
            DatabaseReference fChild = firebasez.push();
            fChild.setValue(elemz);
*/


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static ToDoList get(int index) {
        return values.get(index);
    }

    static ArrayList<ToDoList> values() {
        return values;
    }

    //EA delete elem
    static void remove(int index) {
        try {
            ToDoList elem = values.get(index);
            db.deleteElem(elem);
            values.remove(index);

            firebase = new Firebase("https://todo-b43fa.firebaseio.com/Elem" +  elem.getId());
            firebase.removeValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //EA update elem in db
    static void notify(final int index)  {
        new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... params) {
                try {
                    db.updateElem(values.get(index));
                    ToDoList todoList = values.get(index);

                    firebase = new Firebase("https://todo-b43fa.firebaseio.com/Elem" +  todoList.getId());
                    firebase.setValue(todoList);

                    Bitmap photo = todoList.getPhoto();

                    //DatabaseReference firebasez;
                    //firebasez = FirebaseDatabase.getInstance().getReference().child("Elem");
                    //firebasez.setValue(todoList);
                    //DatabaseReference fChild = firebasez.push();
                    //fChild.setValue(todoList);

                    StorageReference mStorage = FirebaseStorage.getInstance().getReference();
                    StorageReference filepath = mStorage.child("Images/IMG" + todoList.getId());
                    filepath.putBytes(todoList.bitmapToByte(photo)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            assert downloadUrl != null;
                            firebase.child("photo").setValue(downloadUrl.toString());
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    //EA get theme id
    static int getTheme() {
        return theme_id;
    }

    //EA set theme
    static void setTheme(int theme_id) {
        ToDoManager.theme_id = theme_id;
        preferencesEditor.putInt("theme_id", theme_id);
        preferencesEditor.commit();
    }
}
