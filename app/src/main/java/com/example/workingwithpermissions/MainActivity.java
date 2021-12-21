package com.example.workingwithpermissions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int READ_GALLERY_REQUEST_CODE = 1;
    private static boolean READ_GALLERY_GRANTED = false;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);

        // is permission granted
        int hasReadContractPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        if (hasReadContractPermission == PackageManager.PERMISSION_GRANTED)
            READ_GALLERY_GRANTED = true;
        else
            // ask permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_GALLERY_REQUEST_CODE);

        // read contacts
        if (READ_GALLERY_GRANTED)
            getImagesFromGallery();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == READ_GALLERY_REQUEST_CODE && grantResults.length > 0 &&
            grantResults[0] == PackageManager.PERMISSION_DENIED)
            READ_GALLERY_GRANTED = true;

        if (READ_GALLERY_GRANTED)
            getImagesFromGallery();
        else
            Toast.makeText(this, "Give me a permission to read contacts",
                    Toast.LENGTH_SHORT).show();
    }

    private void getImagesFromGallery() {
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.MediaColumns.DATA}, null, null, null);
        ArrayList<String> images = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext()){
                @SuppressLint("Range")
                String path = cursor.getString(cursor.getColumnIndex(
                        MediaStore.MediaColumns.DATA));
                images.add(path);
            }
            cursor.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item, images);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            String path = images.get(i);
            Intent intent = new Intent(this, ViewImage.class);
            intent.putExtra("path", path);
            startActivity(intent);
        });
    }

}