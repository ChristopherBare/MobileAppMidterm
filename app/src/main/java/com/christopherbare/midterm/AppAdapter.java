package com.christopherbare.midterm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AppAdapter extends ArrayAdapter<App> {

    public AppAdapter(Context context, int resource, List<App> objects) {
        super(context, resource, objects);
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        App app = getItem(position);
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_app_item, parent, false);

        TextView name = convertView.findViewById(R.id.appName);
        TextView artistName = convertView.findViewById(R.id.artistName);
        ImageView appImage = convertView.findViewById(R.id.appImage);
        TextView releaseDate = convertView.findViewById(R.id.releaseDate);
        TextView genres = convertView.findViewById(R.id.genres);



        //set the data from the contact object
        name.setText(app.getName());
        artistName.setText(app.getArtistName());
        releaseDate.setText(app.getReleaseDate());
        genres.setText(app.getGenres().toString());

        //use picasso
        if (app.getArtworkUrl100()!=null) Picasso.get().load(app.getArtworkUrl100()).into(appImage);

        return convertView;
    }
}
