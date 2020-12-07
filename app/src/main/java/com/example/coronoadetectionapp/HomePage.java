package com.example.coronoadetectionapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

public class HomePage extends AppCompatActivity {

    FloatingActionButton picture;
    BottomNavigationView navigationView;
    ImageView imageView;
    Button proceed;
    Python py;
    PyObject obj;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        if(!Python.isStarted()){
            Python.start(new AndroidPlatform(this));
        }

        py = Python.getInstance();
        obj = py.getModule("main");
        proceed = (Button)findViewById(R.id.proceedToNext);
        picture = (FloatingActionButton)findViewById(R.id.fab);
        textView = (TextView)findViewById(R.id.textView2);
        imageView = findViewById(R.id.image);
        navigationView = (BottomNavigationView)findViewById(R.id.bottomNavigationView);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFrag()).commit();

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selected = new HomeFrag();
                switch (item.getItemId()){
                    case R.id.home:
                        selected = new HomeFrag();
                        break;
                    case R.id.about:
                        selected = new AboutFrag();
                        break;
                }
          getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selected).commit();
                return true;
            }
        });

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                giveOptionToSelect();
            }
        });

    }

    private void giveOptionToSelect() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] list = {"Local Storage","Open Camera"};
        builder.setTitle("Select An Option");
        builder.setItems(list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(list[i].equals("Local Storage")){
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                    startActivityForResult(intent, 1);
                }else{
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePictureIntent,2);
                }
            }
        });
        builder.create();
        builder.show();

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PyObject obj = null;
                obj.callAttr("main",imageView);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                Uri uri = data.getData();
                Picasso.get().load(uri).resize(800,800).into(imageView);
                break;
            case 2:
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imageView.setImageBitmap(imageBitmap);
                break;
        }
    }


}