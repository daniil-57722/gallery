package ru.example.mygallery;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Cell> allFiles;
    String allFilesPaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
        } else {
            showImages();
        }
    }

    private void showImages() {

        Uri allImagesuri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.BUCKET_ID};
        Cursor cursor = this.getContentResolver().query(allImagesuri, projection, null, null, null);
        try {
            int count = 0;
            allFiles = new ArrayList<>();
            if (cursor != null) {
                cursor.moveToLast();
            }
            do {
                Cell path = new Cell();
                path.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)));
                path.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));
                count++;
                allFiles.add(path);

            }
            while (cursor.moveToPrevious() && count < 102);
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < allFiles.size(); i++) {
            allFilesPaths = allFilesPaths + allFiles.get(i).getPath() + "#";

            Log.d("picture folders", allFiles.get(i).getTitle() + " and path = " + allFiles.get(i).getPath());
        }
        Log.d("picture folders", allFilesPaths );
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.gallery);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        // список в три колонки
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(layoutManager);

        //оптимизация
        recyclerView.setItemViewCacheSize(20000);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        MyAdapter adapter = new MyAdapter(getApplicationContext(), allFiles, allFilesPaths);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showImages();
            } else {
                Toast.makeText(this, "Разрешения на чтение нет", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}

