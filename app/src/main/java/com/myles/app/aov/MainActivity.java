package com.myles.app.aov;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Opportunity>> {

    private static final int OPPS_LOADER = 1;
    
    private static final int GEP_IN_LOAD = 1;
    private static final int US_IN_LOAD = 2;
    private static final int CA_IN_LOAD = 3;
    private static final int DEV_IN_LOAD = 4;

    private static final String GIS_API_URL_GEP = "https://gis-api.aiesec.org/v2/opportunities.json?access_token=e316ebe109dd84ed16734e5161a2d236d0a7e6daf499941f7c110078e3c75493&filters%5Bis_gep%5D=true&per_page=1000&only=data";
    private static final String GIS_API_URL_US = "https://gis-api.aiesec.org/v2/opportunities.json?access_token=e316ebe109dd84ed16734e5161a2d236d0a7e6daf499941f7c110078e3c75493&filters[home_mcs][]=1621&filters[programmes][]=2&per_page=1000&only-data";
    private static final String GIS_API_URL_CA = "https://gis-api.aiesec.org/v2/opportunities.json?access_token=e316ebe109dd84ed16734e5161a2d236d0a7e6daf499941f7c110078e3c75493&filters[home_mcs][]=1554&filters[programmes][]=2&per_page=1000&only-data";
    private static final String GIS_API_URL_DEV = "https://gis-api.aiesec.org/v2/opportunities.json?access_token=e316ebe109dd84ed16734e5161a2d236d0a7e6daf499941f7c110078e3c75493&q=developer&per_page=1000&only=data";
                        
    private int mInLoadIndicator;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((ListView) this.findViewById(R.id.list)).setEmptyView((TextView) findViewById(R.id.empty_view));

        final LoaderManager loaderManager = getLoaderManager();
        //loaderManager.initLoader(OPPS_LOADER, null, MainActivity.this);

        /* GEP Search Button Setup */
        ((Button) this.findViewById(R.id.button_gep)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                MainActivity.this.mInLoadIndicator = GEP_IN_LOAD;
                if ( loaderManager.getLoader(OPPS_LOADER) == null ){
                    loaderManager.initLoader(OPPS_LOADER, null, MainActivity.this);
                }else{
                    loaderManager.restartLoader(OPPS_LOADER, null, MainActivity.this);
                }
            }
        });
        
        /* US Search Button Setup */
        ((Button) this.findViewById(R.id.button_us)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                MainActivity.this.mInLoadIndicator = US_IN_LOAD;
                if ( loaderManager.getLoader(OPPS_LOADER) == null ){
                    loaderManager.initLoader(OPPS_LOADER, null, MainActivity.this);
                }else{
                    loaderManager.restartLoader(OPPS_LOADER, null, MainActivity.this);
                }
            }
        });
        
        /* CA Search Button Setup */
        ((Button) this.findViewById(R.id.button_ca)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                MainActivity.this.mInLoadIndicator = CA_IN_LOAD;
                if ( loaderManager.getLoader(OPPS_LOADER) == null ){
                    loaderManager.initLoader(OPPS_LOADER, null, MainActivity.this);
                }else{
                    loaderManager.restartLoader(OPPS_LOADER, null, MainActivity.this);
                }
            }
        });
        
        /* DEV Search Button Setup */
        ((Button) this.findViewById(R.id.button_developer)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                MainActivity.this.mInLoadIndicator = DEV_IN_LOAD;
                if ( loaderManager.getLoader(OPPS_LOADER) == null ){
                    loaderManager.initLoader(OPPS_LOADER, null, MainActivity.this);
                }else{
                    loaderManager.restartLoader(OPPS_LOADER, null, MainActivity.this);
                }
            }
        });
        
        ((ListView) this.findViewById(R.id.list)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, OpportunityActivity.class);
                intent.putExtra("id", ((TextView)view.findViewById(R.id.text_id)).getText().toString());
                intent.putExtra("title", ((TextView)view.findViewById(R.id.text_title)).getText().toString());
                intent.putExtra("company", ((TextView)view.findViewById(R.id.text_company)).getText().toString());
                intent.putExtra("country", ((TextView)view.findViewById(R.id.text_country)).getText().toString());
                intent.putExtra("duration", ((TextView)view.findViewById(R.id.text_duration)).getText().toString());
                intent.putExtra("application_close_date", ((TextView)view.findViewById(R.id.text_application_close_date)).getText().toString());
                startActivity(intent);
            }
        });

        ((SwipeRefreshLayout) this.findViewById(R.id.swiperefresh)).setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.v("MylesDebug", "onRefresh called from SwipeRefreshLayout");
                loaderManager.getLoader(OPPS_LOADER).forceLoad();
            }
        });

    }

    @Override
    public Loader<List<Opportunity>> onCreateLoader(int id, Bundle args) {
        Log.v("MylesDebug", "onCreateLoader");
        ((ListView) this.findViewById(R.id.list)).setEmptyView((TextView) findViewById(R.id.empty_view));
        Toast.makeText(this, "start load:" + getInLoadItemName(), Toast.LENGTH_SHORT).show();
        /* Debug: assumne id = OPPS_LOADER so no chekcing */
        URL searchUrl = null;
        try {
            searchUrl = new URL(this.getInLoadURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return new OpportunityAsyncTaskLoader(this, searchUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Opportunity>> loader, List<Opportunity> opportunities) {
        Log.v("MylesDebug", "onLoadFinished");

        if (((SwipeRefreshLayout) findViewById(R.id.swiperefresh)).isRefreshing()) {
            ((SwipeRefreshLayout) findViewById(R.id.swiperefresh)).setRefreshing(false);
        }

        if ( opportunities == null || opportunities.size() == 0 ) {
            Toast.makeText(this, "fail to load, use last result", Toast.LENGTH_SHORT).show();
           
            /* Load Failed: Use file data instead */
            try{
                FileInputStream fis = openFileInput(getInLoadItemName());
                ObjectInputStream ois = new ObjectInputStream(fis);
                opportunities = (List<Opportunity>)ois.readObject();
            } 
            catch (Exception e) {
                e.printStackTrace();
            }

        }else {
            /* Load Success: update result to file */
            Toast.makeText(this, "Load lastest info success, save to file", Toast.LENGTH_SHORT).show();
            try {
                FileOutputStream fos = openFileOutput(getInLoadItemName(), Context.MODE_PRIVATE);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(opportunities);
                oos.close();
                fos.close();
            }catch (IOException ioe){
                ioe.printStackTrace();
            }
        }

        /* No matter success or fail: Present the result */
        OpportunityAdapter adapter = new OpportunityAdapter(MainActivity.this, opportunities);
        ((ListView) findViewById(R.id.list)).setAdapter(adapter);

    }

    @Override
    public void onLoaderReset(Loader<List<Opportunity>> loader) {
        Log.v("MylesDebug", "onResetLoader");
        /* Debug: assumne id = OPPS_LOADER so no chekcing */
        /* This method is followed by a new call to onCreateLoader so the URL will
         * be selected again by getInLoadURL() method 
         */
    }
    
    private String getInLoadURL(){
        String urlString = "";
        if ( this.mInLoadIndicator == GEP_IN_LOAD ){
            urlString = GIS_API_URL_GEP; 
        }else if (this.mInLoadIndicator == US_IN_LOAD){
            urlString = GIS_API_URL_US; 
        }else if (this.mInLoadIndicator == CA_IN_LOAD){
            urlString = GIS_API_URL_CA; 
        }else if (this.mInLoadIndicator == DEV_IN_LOAD){
            urlString = GIS_API_URL_DEV; 
        } 
        return urlString;
    }

    private String getInLoadItemName(){
        String itemName = "";
        if ( this.mInLoadIndicator == GEP_IN_LOAD ){
            itemName = "GEP";
        }else if (this.mInLoadIndicator == US_IN_LOAD){
            itemName = "US";
        }else if (this.mInLoadIndicator == CA_IN_LOAD){
            itemName = "CA";
        }else if (this.mInLoadIndicator == DEV_IN_LOAD){
            itemName = "DEV";
        }
        return itemName;
    }

}
