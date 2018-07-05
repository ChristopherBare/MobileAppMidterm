package com.christopherbare.midterm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class AppDetails extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getExtras()!=null){
            TextView name = findViewById(R.id.name);
            TextView release_date = findViewById(R.id.releaseDate);
            TextView artist = findViewById(R.id.artistName);
            TextView genres = findViewById(R.id.genres);
            TextView copyright = findViewById(R.id.copyright);
            ImageView image = findViewById(R.id.artworkUrl100);

            App app = (App) getIntent().getExtras().getSerializable("App");
            name.setText(app.getName());
            release_date.setText(app.getReleaseDate());
            artist.setText(app.getArtistName());
            genres.setText(app.getGenres());
            copyright.setText(app.getCopyright());
            Picasso.get().load(app.getArtworkUrl100()).into(image);
        }
    }
}
