package com.myles.app.aov;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.Path;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OpportunityAsyncTaskLoader extends AsyncTaskLoader<List<Opportunity>> {

    final static public int DEFAULT_READ_TIMEOUT = 20000;
    final static public int DEFAULT_CONNECTION_TIMEOUT = 30000;

    private URL mURL;

    public OpportunityAsyncTaskLoader(Context context, URL url) {
        super(context);
        this.mURL = url;
    }

    @Override
    public List<Opportunity> loadInBackground() {
        Log.v("MylesDebug", "Loader- loadInBackground");
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
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
            urlConnection.setReadTimeout(DEFAULT_READ_TIMEOUT);
            urlConnection.setConnectTimeout(DEFAULT_CONNECTION_TIMEOUT);
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

    private List<Opportunity> extractFeatureFromJson(String json) {
        ArrayList<Opportunity> opportunities = new ArrayList<Opportunity>();
        try {
            JSONObject baseJsonResponse = new JSONObject(json);
            JSONArray featureArray = baseJsonResponse.getJSONArray("data");

            // If there are results in the features array
            for (int i = 0; i < featureArray.length(); i++) {
                JSONObject properties = featureArray.getJSONObject(i);
                Opportunity opportunity = new Opportunity();
                opportunity.setId(properties.getInt("id"));
                opportunity.setTitle(properties.getString("title"));
                opportunity.setCompany(properties.getJSONObject("branch").getString("name"));
                opportunity.setCountry(properties.getJSONObject("office").getString("country"));
                opportunity.setDuration(properties.getInt("duration"));
                opportunity.setApplicationCloseDate(properties.getString("applications_close_date"));
                opportunities.add(opportunity);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return opportunities;
    }
}
