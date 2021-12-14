package ru.example.mygallery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<Cell> galleryList;
    private Context context;
    String paths;
    public ImageView img;
    Animation animation;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(Context context, ArrayList<Cell> galleryList, String allFilesPaths) {
        this.paths = allFilesPaths;
        this.context = context;
        this.galleryList = galleryList;

    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell, parent, false);
        return new ViewHolder(view);
    }
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        viewHolder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        setImageFromPath(galleryList.get(position).getPath(), viewHolder.img);
        viewHolder.img.setOnClickListener( new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                img = v.findViewById(R.id.img);
                animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.rotate);
                img.startAnimation(animation);
                Intent intent = new Intent(v.getContext(), ImageActivity.class);
                intent.putExtra("Index", position);
                intent.putExtra("Image", galleryList.get(position).getPath());
                intent.putExtra("FirstImage", galleryList.get(0).getPath());
                intent.putExtra("Paths", paths);
                v.getContext().startActivity(intent);
            }


        });
    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView img;

        public ViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.img);
        }
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return galleryList.size();
    }

    private void setImageFromPath(String path, ImageView image) {
        File imgFile = new File(path);
        if (imgFile.exists()) {
            Bitmap myBitmap = ImageHelper.decodeSampleBitmapFromPath(imgFile.getAbsolutePath(), 150, 150);
            image.setImageBitmap(myBitmap);
        }
    }
}