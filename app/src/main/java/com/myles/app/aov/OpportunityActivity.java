package com.myles.app.aov;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.Loader;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class OpportunityActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Opportunity> {

    private static final int OPPS_LOADER = 1;

    private static final String GIS_API_URL_PREFIX = "https://gis-api.aiesec.org/v2/opportunities/";

    private static final String GIS_API_URL_SUFFIX = ".json?access_token=e316ebe109dd84ed16734e5161a2d236d0a7e6daf499941f7c110078e3c75493";

    /* the id of opportunity*/
    private String mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opportunity);

        Intent intent = getIntent();
        String id = intent.getExtras().getString("id");
        String title = intent.getExtras().getString("title");
        String company = intent.getExtras().getString("company");
        String country = intent.getExtras().getString("country");
        String duration = intent.getExtras().getString("duration");
        String applicationCloseDate = intent.getExtras().getString("application_close_date");

        this.mId = id;

        ((TextView) findViewById(R.id.text_id)).setText(id);
        ((TextView) findViewById(R.id.text_title)).setText(title);
        ((TextView) findViewById(R.id.text_company)).setText(company);
        ((TextView) findViewById(R.id.text_country)).setText(country);
        ((TextView) findViewById(R.id.text_duration)).setText(duration);
        ((TextView) findViewById(R.id.text_application_close_date)).setText(applicationCloseDate);

        final LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(OPPS_LOADER, null, OpportunityActivity.this);

        ((SwipeRefreshLayout) this.findViewById(R.id.swiperefresh)).setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.v("MylesDebug", "OpportunityDeatil: onRefresh called from SwipeRefreshLayout");
                loaderManager.getLoader(OPPS_LOADER).forceLoad();
            }
        });

    }

    private void setAllDetailViewsText(String str) {
        ((TextView) findViewById(R.id.text_views)).setText(str);
        ((TextView) findViewById(R.id.text_home_lc)).setText(str);
        ((TextView) findViewById(R.id.text_managers)).setText(str);
        ((TextView) findViewById(R.id.text_skills)).setText(str);
        ((TextView) findViewById(R.id.text_backgrounds)).setText(str);
        ((TextView) findViewById(R.id.text_visa_link)).setText(str);
        ((TextView) findViewById(R.id.text_visa_type)).setText(str);
        ((TextView) findViewById(R.id.text_visa_duration)).setText(str);
        ((TextView) findViewById(R.id.text_city)).setText(str);
        ((TextView) findViewById(R.id.text_select_process)).setText(str);
        ((TextView) findViewById(R.id.text_salary)).setText(str);
        ((TextView) findViewById(R.id.text_salary_ccy)).setText(str);
        ((TextView) findViewById(R.id.text_salary_ccy_code)).setText(str);
        ((TextView) findViewById(R.id.text_create_time)).setText(str);
        ((TextView) findViewById(R.id.text_update_time)).setText(str);
    }

    @Override
    public Loader<Opportunity> onCreateLoader(int id, Bundle args) {
        Log.v("MylesDebug", "Opportunity: onCreateLoader");

        /* Set result empty */
        setAllDetailViewsText("Loading...");

        URL searchUrl = null;
        try {
            searchUrl = new URL(GIS_API_URL_PREFIX + this.mId + GIS_API_URL_SUFFIX);
        } catch (MalformedURLException e) {
            setAllDetailViewsText("URL Not Parsed");
        }

        // 为给定 URL 创建新 loader
        return new OpportunityDetailAsyncTaskLoader(this, searchUrl);
    }

    @Override
    public void onLoadFinished(Loader<Opportunity> loader, Opportunity opportunity) {
        Log.v("MylesDebug", "Opportunity: onLoadFinished");

        if (((SwipeRefreshLayout) findViewById(R.id.swiperefresh)).isRefreshing()) {
            ((SwipeRefreshLayout) findViewById(R.id.swiperefresh)).setRefreshing(false);
        }

        if (opportunity == null || opportunity.isEmpty()) {
            Toast.makeText(this, "fail to load, use last result", Toast.LENGTH_SHORT).show();
            /* Load Failed: Use file data instead */
            try {
                FileInputStream fis = openFileInput("OPP" + String.valueOf(this.mId));
                ObjectInputStream ois = new ObjectInputStream(fis);
                opportunity = (Opportunity) ois.readObject();
            } catch (Exception e) {
                Toast.makeText(this, "Unfortunately, no cached result", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            System.out.println("Here we go");
            System.out.println(opportunity);

        } else {
            /* Load Success: update result to file */
            Toast.makeText(this, "Load lastest info success, save to file", Toast.LENGTH_SHORT).show();
            try {
                FileOutputStream fos = openFileOutput("OPP" + String.valueOf(this.mId), Context.MODE_PRIVATE);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(opportunity);
                oos.close();
                fos.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }


        if (opportunity != null && !opportunity.isEmpty()) { //since opp can still be empty if no cached file
            /* Debug temp set all as string to avoid type problem */
            ((TextView) findViewById(R.id.text_views)).setText(String.valueOf(opportunity.getViews()));
            ((TextView) findViewById(R.id.text_home_lc)).setText(opportunity.getHomeLC());
            ((TextView) findViewById(R.id.text_visa_link)).setText(opportunity.getVisaLink());
            ((TextView) findViewById(R.id.text_visa_type)).setText(opportunity.getVisaType());
            ((TextView) findViewById(R.id.text_visa_duration)).setText(opportunity.getVisaDuration());
            ((TextView) findViewById(R.id.text_city)).setText(opportunity.getCity());
            ((TextView) findViewById(R.id.text_select_process)).setText(opportunity.getSelectProcess());
            ((TextView) findViewById(R.id.text_salary)).setText(String.valueOf(opportunity.getSalary()));
            ((TextView) findViewById(R.id.text_salary_ccy)).setText(opportunity.getSalaryCcy());
            ((TextView) findViewById(R.id.text_salary_ccy_code)).setText(String.valueOf(opportunity.getSalaryCcyCode()));
            ((TextView) findViewById(R.id.text_create_time)).setText(opportunity.getCreateTime());
            ((TextView) findViewById(R.id.text_update_time)).setText(opportunity.getUpdateTime());
            //new description, preparation & addtional
            ((TextView) findViewById(R.id.text_description)).setText(opportunity.getDescription());
            ((TextView) findViewById(R.id.text_preparation)).setText(opportunity.getPreparation());
            ((TextView) findViewById(R.id.text_additional)).setText(opportunity.getAdditional());

            /* Parse n managers to string */
            StringBuilder managersStringBuilder = new StringBuilder();
            for (int i = 0; i < opportunity.getManagers().size(); i++) {
                Opportunity.Manager m = opportunity.getManagers().get(i);
                managersStringBuilder.append(m.getFullName());
                managersStringBuilder.append("/");
                managersStringBuilder.append(m.getEmail());
                managersStringBuilder.append("\n");
            }
            ((TextView) findViewById(R.id.text_managers)).setText(managersStringBuilder.toString());

            /* Parse n skills to string */
            StringBuilder skillsStringBuilder = new StringBuilder();
            for (int i = 0; i < opportunity.getSkills().size(); i++) {
                Opportunity.Skill s = opportunity.getSkills().get(i);
                skillsStringBuilder.append(s.getName());
                skillsStringBuilder.append("/");
                skillsStringBuilder.append(s.getOption());
                skillsStringBuilder.append("/");
                skillsStringBuilder.append(s.getLevel());
                skillsStringBuilder.append("\n");
            }
            ((TextView) findViewById(R.id.text_skills)).setText(skillsStringBuilder.toString());

            /* Parse n backgrounds to string */
            StringBuilder backgroundsStringBuilder = new StringBuilder();
            for (int i = 0; i < opportunity.getBackgrounds().size(); i++) {
                Opportunity.Background b = opportunity.getBackgrounds().get(i);
                backgroundsStringBuilder.append(b.getName());
                backgroundsStringBuilder.append("/");
                backgroundsStringBuilder.append(b.getOption());
                backgroundsStringBuilder.append("\n");
            }
            ((TextView) findViewById(R.id.text_backgrounds)).setText(backgroundsStringBuilder.toString());

        }

    }

    @Override
    public void onLoaderReset(Loader<Opportunity> loader) {
        Log.v("MylesDebug", "Opportunity: onResetLoader");
        // empty the list in activity
    }

}
