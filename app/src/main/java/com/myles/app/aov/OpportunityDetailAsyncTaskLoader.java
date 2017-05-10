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

import com.myles.app.aov.Opportunity.*;

public class OpportunityDetailAsyncTaskLoader extends AsyncTaskLoader<Opportunity> {

    final static public int DEFAULT_READ_TIMEOUT = 20000;
    final static public int DEFAULT_CONNECTION_TIMEOUT = 30000;

    private URL mURL;

    public OpportunityDetailAsyncTaskLoader(Context context, URL url) {
        super(context);
        this.mURL = url;
    }

    @Override
    public Opportunity loadInBackground() {
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

        Opportunity opportunity = extractFeatureFromJson(jsonResponse);
        return opportunity;
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

    private Opportunity extractFeatureFromJson(String json) {
        Opportunity opportunity = new Opportunity();
        try {
            JSONObject properties = new JSONObject(json);

            /* Brief member */
            opportunity.setId(properties.getInt("id"));
            opportunity.setTitle(properties.getString("title"));
            opportunity.setCompany(properties.getJSONObject("branch").getString("name"));
            opportunity.setDuration(properties.getInt("duration"));
            opportunity.setCountry(properties.getJSONObject("home_lc").getString("country"));

            /* Detail member */
            /* Debug Suppose all type are well-set and should compate wo converting type */
            opportunity.setViews(properties.getInt("views"));
            opportunity.setApplicationCloseDate(properties.getString("applications_close_date"));
            opportunity.setHomeLC(properties.getJSONObject("home_lc").getString("full_name"));
            opportunity.setVisaLink(properties.getJSONObject("legal_info").getString("visa_link"));
            opportunity.setVisaType(properties.getJSONObject("legal_info").getString("visa_type"));
            opportunity.setVisaDuration(properties.getJSONObject("legal_info").getString("visa_duration"));
            opportunity.setCity(properties.getJSONObject("role_info").getString("city"));
            opportunity.setSelectProcess(properties.getJSONObject("role_info").getString("selection_process"));
            opportunity.setSalary(properties.getJSONObject("specifics_info").getInt("salary"));
            opportunity.setSalaryCcy(properties.getJSONObject("specifics_info").getJSONObject("salary_currency").getString("name"));
            opportunity.setSalaryCcyCode(properties.getJSONObject("specifics_info").getJSONObject("salary_currency").getInt("numeric_code"));
            opportunity.setCreateTime(properties.getString("created_at"));
            opportunity.setUpdateTime(properties.getString("updated_at"));
			
			/* Read managers */
			JSONArray managersJson = properties.getJSONArray("managers");
			for (int i = 0 ; i < managersJson.length(); i++ ){
				Manager m = new Manager();
				m.setFullName(managersJson.getJSONObject(i).getString("full_name"));
				m.setEmail(managersJson.getJSONObject(i).getString("email"));
			    opportunity.getManagers().add(m);
			}
			
			/* Read Skills */
			JSONArray skillsJson = properties.getJSONArray("skills");
			for (int i = 0 ; i < skillsJson.length(); i++ ){
				Skill s = new Skill();
				s.setName(skillsJson.getJSONObject(i).getString("name"));
				s.setOption(skillsJson.getJSONObject(i).getString("option"));
				//s._level= skillsJson.getJSONObject(i).getInt("level");
			    opportunity.getSkills().add(s);
			}
			
			/* Read Background */
			JSONArray backgroundsJson = properties.getJSONArray("backgrounds");
			for (int i = 0 ; i < backgroundsJson.length(); i++ ){
				Background b = new Background();
				b.setName(backgroundsJson.getJSONObject(i).getString("name"));
				b.setOption(backgroundsJson.getJSONObject(i).getString("option"));
			    opportunity.getBackgrounds().add(b);
			}

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return opportunity;
    }
}
