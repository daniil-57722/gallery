package ru.example.mygallery;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class ImageActivity extends AppCompatActivity {
    ImageView imageView;
    int index = 0;
    String paths;
    String[] pathsArr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        imageView = findViewById(R.id.ImageOnClick);
        Bundle arguments = getIntent().getExtras();
        index = arguments.getInt("Index");
        paths = arguments.getString("Paths");
        pathsArr = paths.split("#");
        setImageFromPath(arguments.getString("Image"), imageView);


    }
    private void setImageFromPath(String path, ImageView image) {
        File imgFile = new File(path);
        if (imgFile.exists()) {
            Bitmap myBitmap = ImageHelper.decodeSampleBitmapFromPath(imgFile.getAbsolutePath(), 1000, 1000);
            image.setImageBitmap(myBitmap);
        }
    }
    public void RightButtonClick(View v) {
        if (index<pathsArr.length-1){
        index++;
        setImageFromPath(pathsArr[index], imageView);}
        else{
            Toast.makeText(this, "Это последнее фото", Toast.LENGTH_LONG).show();
        }
    }

    public void LeftButtonClick(View v) {
        if (index>0){
        index--;
        setImageFromPath(pathsArr[index], imageView);}
        else{
            Toast.makeText(this, "Это первое фото", Toast.LENGTH_LONG).show();
        }

    }
}