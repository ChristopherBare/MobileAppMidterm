package com.christopherbare.midterm;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.commons.io.IOUtils;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Activity activity;
    ArrayList<App> apps = new ArrayList<>();
    ArrayList<String> genreArray = new ArrayList<>();
    ArrayList<App> filteredAppsList = new ArrayList<>();
    AppAdapter adapter;
    ListView appList;
    App app;
    Spinner spinner;
    Context context;
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = findViewById(R.id.spinner);
        context = getApplicationContext();
        activity = MainActivity.this;
        button = findViewById(R.id.button);

        if(isConnected()) {
            new GetDataAsync().execute("https://rss.itunes.apple.com/api/v1/us/ios-apps/top-free/all/50/explicit.json");
        } else
            Toast.makeText(getApplicationContext(), "No Network Connection.", Toast.LENGTH_LONG).show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filter = spinner.getSelectedItem().toString();
                new GetDataAsync(filter).execute("https://rss.itunes.apple.com/api/v1/us/ios-apps/top-free/all/50/explicit.json");
            }
        });

        if (appList != null) {
            appList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(MainActivity.this, AppDetails.class);
                    //put extras here
                    startActivity(intent);
                }
            });
        }
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected() &&
                (networkInfo.getType() == ConnectivityManager.TYPE_WIFI
                        || networkInfo.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    private class GetDataAsync extends AsyncTask<String, Void, ArrayList<App>> {
        ProgressDialog dialog;
        String filterParam;
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected ArrayList<App> doInBackground(String... params) {
            HttpURLConnection connection = null;
            ArrayList<App> result = new ArrayList<>();
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    //The JSON file in string format
                    String json = IOUtils.toString(connection.getInputStream(), "UTF-8");

                    //The entire JSON object
                    JSONObject root = new JSONObject(json);

                    //The array within the JSON object
                    JSONObject feed = root.getJSONObject("feed");

                    JSONArray resultApps = feed.getJSONArray("results");

                    for (int i = 0; i < feed.length(); i++) {

                        //Initialize objects
                        app = new App();

                        //Get the JSON "App"
                        JSONObject appJSON = resultApps.getJSONObject(i);

                        //get the JSON "genres"
                        JSONArray genresJSON = appJSON.getJSONArray("genres");

                        //Set the fields for object from the JSON one
                        app.setName(appJSON.getString("name"));
                        app.setReleaseDate(appJSON.getString("releaseDate"));
                        app.setArtistName(appJSON.getString("artistName"));
                        app.setArtworkUrl100(appJSON.getString("artworkUrl100"));

                        String genreName;
                        //get genres
                        if (genresJSON != null) {
                            for (int j = 0; j < genresJSON.length(); j++) {
                                JSONObject genre = genresJSON.getJSONObject(j);
                                genreName = genre.getString("name");
                                app.addGenre(genreName);
                                if (!genreArray.contains(genresJSON.getString(j))) {
                                genreArray.add(genreName);
                                }
                            }
                        }

                        //Add this person to our results
                        result.add(app);
                        Log.d("demo", "doInBackground: " + app.toString());
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Getting Apps");
            dialog.show();
        }


        public GetDataAsync() {
            dialog = new ProgressDialog(activity);
        }

        public GetDataAsync(String filter) {
            dialog = new ProgressDialog(activity);
            filterParam = filter;

        }
        @Override
        protected void onPostExecute(ArrayList<App> result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if (apps.size() == 0){
                apps.addAll(result);
            } else {
                //need to fix this, doesn't work right.
                filteredAppsList.clear();
                for (int i = 0; i < apps.size(); i++) {
                    if (result.get(i).getGenres().contains(filterParam)) {
                        if (!filteredAppsList.contains(result.get(i))) {
                            filteredAppsList.add(apps.get(i));
                        }

                    }
                }
                apps.clear();
                apps.addAll(filteredAppsList);
            }
                adapter = new AppAdapter(getApplicationContext(), R.layout.activity_app_item, apps);
                appList = findViewById(R.id.list_view_apps);
                appList.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                if (genreArray.isEmpty()){
                    Toast.makeText(context, "Error retrieving spinner items", Toast.LENGTH_SHORT).show();
                } else {
                //populate spinner
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, genreArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                button.setClickable(true); }
        }
    }
}



