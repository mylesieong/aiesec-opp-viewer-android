package com.myles.app.aov;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by asus on 9/5/2017.
 */

public class OpportunityAdapter extends ArrayAdapter<Opportunity> {
    public OpportunityAdapter(Context context, List<Opportunity> opportunities) {
        super(context, 0, opportunities);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        }

        Opportunity currentOpportunity = this.getItem(position);

        ((TextView) listItemView.findViewById(R.id.text_id)).setText(String.valueOf(currentOpportunity.getId()));
        ((TextView) listItemView.findViewById(R.id.text_title)).setText(currentOpportunity.getTitle());
        ((TextView) listItemView.findViewById(R.id.text_company)).setText(currentOpportunity.getCompany());
        ((TextView) listItemView.findViewById(R.id.text_country)).setText(currentOpportunity.getCountry());
        ((TextView) listItemView.findViewById(R.id.text_duration)).setText(String.valueOf(currentOpportunity.getDuration()) + "wks");
        ((TextView) listItemView.findViewById(R.id.text_application_close_date)).setText(currentOpportunity.getApplicationCloseDate());

        return listItemView;
    }

    private String parseDate(String timeStamp){
        StringBuilder sb = new StringBuilder();
        sb.append(timeStamp.substring(8,10));
        sb.append("/");
        sb.append(timeStamp.substring(5,7));
        sb.append("/");
        sb.append(timeStamp.substring(2,4));
        return sb.toString();
    }
}
