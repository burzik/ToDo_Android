package com.god.magic;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * HISTORY
 * 	Date			Author				Comments
 * 	10.03.2017		Eduard Arefjev 		Detailed ToDoActivity
 */

public class ToDoMoreInfo extends AppCompatActivity {

    //EA init variables
    public static final int REQUEST_CAPTURE = 0;
    private static final int GALLERY_REQUEST = 1;
    ImageView resultPhoto;
    private int currentTheme = 0;

    @Override
    public void setTheme(@StyleRes int theme) {
        currentTheme = theme;
        super.setTheme(theme);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(ToDoManager.getTheme());
        setContentView(R.layout.linear_more_info);

        Intent intent = getIntent();
        final int position = intent.getIntExtra("position", -1);
        ToDoList todoList = ToDoManager.get(position);

        final String sLabel = todoList.name;
        String sInfo = todoList.info;

        //EA Label
        final EditText editText = (EditText) findViewById(R.id.label);
        editText.setText(sLabel);

        //EA Info
        final EditText infoText = (EditText) findViewById(R.id.moreinfo);
        infoText.setText(sInfo);

        //EA buttons
        Button shareButton = (Button) findViewById(R.id.share);
        Button deleteButton = (Button) findViewById(R.id.delete);
        Button click = (Button) findViewById(R.id.takephoto);
        Button choose = (Button) findViewById(R.id.choosephoto);

        //EA twitter share
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra(Intent.EXTRA_TEXT, sLabel);
                i.setAction(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://twitter.com/intent/tweet?text="+(sLabel)));
                startActivity(i);
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                infoText.setText("");
            }
        });

        //EA open camera
        click.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                launchCamera(v);
            }
        });

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });

        resultPhoto = (ImageView) findViewById(R.id.photo);
        resultPhoto.setImageBitmap(todoList.photo);

        if(!hasCamera()){
            click.setEnabled(false);
        }
    }

    //EA Exist camera
    public boolean hasCamera() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1 || getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    //EA open camera
    public void launchCamera(View v){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
         if( requestCode == REQUEST_CAPTURE && resultCode == RESULT_OK) {
             Bundle extras = data.getExtras();
             Bitmap photo = (Bitmap) extras.get("data");
             resultPhoto.setImageBitmap(photo);
         }
         if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
             Uri imageUri = data.getData();
             resultPhoto.setImageURI(imageUri);
         }
    }


    //EA save when exit
    public void onBackPressed() {
        final EditText infoText = (EditText) findViewById(R.id.moreinfo);
        final EditText labelText = (EditText) findViewById(R.id.label);
        final ImageView photo = (ImageView) findViewById(R.id.photo);
        BitmapDrawable drawable = (BitmapDrawable) photo.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        Intent intent = getIntent();
        final int position = intent.getIntExtra("position", -1);
        ToDoList todoList = ToDoManager.get(position);

        todoList.name = labelText.getText().toString();
        todoList.info = infoText.getText().toString();
        todoList.photo = bitmap;
        ToDoManager.notify(position);
        ToDoMoreInfo.this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentTheme != ToDoManager.getTheme()) {
            recreate();
        }
    }
}

