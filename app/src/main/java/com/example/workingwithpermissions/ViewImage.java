package com.example.workingwithpermissions;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

public class ViewImage extends AppCompatActivity {

    Button btn;
    ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        btn = findViewById(R.id.btnBack);
        imgView = findViewById(R.id.imgView);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        String path = bundle.getString("path");

        // set image
        File imgFile = new File(path);
        if (imgFile.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imgView.setImageBitmap(bitmap);
        }

        btn.setOnClickListener(view -> {
            finish();
        });
    }
}