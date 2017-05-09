package com.myles.app.aov;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.Path;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 9/5/2017.
 */

public class OpportunityAsyncTaskLoader extends AsyncTaskLoader<List<Opportunity>> {

    private URL mURL;

    public OpportunityAsyncTaskLoader(Context context, URL url) {
        super(context);
        this.mURL = url;
    }

    @Override
    public List<Opportunity> loadInBackground() {
        Log.v("MylesDebug", "Loader- loadInBackground");
        ConnectivityManager connectivityManager = (ConnectivityManager)this.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            return null;
        }

        URL url = mURL;
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        List<Opportunity> opportunities = extractFeatureFromJson(jsonResponse);
        return opportunities;
    }

    @Override
    protected void onStartLoading() {
        Log.v("MylesDebug", "Loader: onStartloading");
        this.forceLoad();
    }

    /**
     * Support method
     * @param inputStream
     * @return
     * @throws IOException
     */
    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Support method
     * @param json
     * @return
     */
    private List<Opportunity> extractFeatureFromJson(String json) {
        ArrayList<Opportunity> opportunities = new ArrayList<Opportunity>();
        try {
            JSONObject baseJsonResponse = new JSONObject(json);
            JSONArray featureArray = baseJsonResponse.getJSONArray("data");

            // If there are results in the features array
            for (int i = 0; i < featureArray.length(); i++) {
                JSONObject properties = featureArray.getJSONObject(i);
                String title = properties.getString("title");
                String publisher = properties.getString("title");
                String url = properties.getString("title");

                Opportunity opportunity = new Opportunity();
                opportunity.setTitle(title);
                opportunity.setPublisher(publisher);
                opportunity.setURL(url);
                opportunities.add(opportunity);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return opportunities;
    }
}
