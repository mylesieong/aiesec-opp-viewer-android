package com.myles.app.aov;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by asus on 9/5/2017.
 */

public class OpportunityActivity extends AppCompatActivity {
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

        ((TextView)findViewById(R.id.text_id)).setText(id);
        ((TextView)findViewById(R.id.text_title)).setText(title);
        ((TextView)findViewById(R.id.text_company)).setText(company);
        ((TextView)findViewById(R.id.text_country)).setText(country);
        ((TextView)findViewById(R.id.text_duration)).setText(duration);
        ((TextView)findViewById(R.id.text_application_close_date)).setText(applicationCloseDate);



    }
}
