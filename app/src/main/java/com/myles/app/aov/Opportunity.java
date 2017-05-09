package com.myles.app.aov;

/**
 * Created by asus on 9/5/2017.
 */

public class Opportunity {
    private String mTitle;
    private String mPublisher;
    private String mURL;

    public String getPublisher() {
        return mPublisher;
    }

    public void setPublisher(String mPublisher) {
        this.mPublisher = mPublisher;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getURL() {
        return mURL;
    }

    public void setURL(String mURL) {
        this.mURL = mURL;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().toString());
        sb.append(this.mTitle);
        sb.append("/");
        sb.append(this.mPublisher);
        sb.append("/");
        sb.append(this.mURL);
        sb.append("#");
        return sb.toString();
    }
}
