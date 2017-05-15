package com.myles.app.aov;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 * Created by asus on 9/5/2017.
 */

public class Opportunity implements Serializable {

    private final static int INT_EMPTY = 0;
    private final static String STRING_EMPTY = null;
    private final static List LIST_EMPTY = null;

    /* Brief */
    private int id;
    private String title;
    private String company;
    private int duration;
    private String country;
    private String applicationCloseDate;

    /* Details */
    private int views;
    private String homeLC;
    private List<Manager> managers = new ArrayList<Manager>();
    private List<Skill> skills = new ArrayList<Skill>();
    private List<Background> backgrounds = new ArrayList<Background>();
    private String visaLink;
    private String visaType;
    private String visaDuration;
    private String city;
    private String selectProcess;
    private int salary;
    private String salaryCcy;
    private int salaryCcyCode;
    private String createTime;
    private String updateTime;

    /* Constructor */
    public Opportunity(){

        this.id = INT_EMPTY;
        this.title = STRING_EMPTY;
        this.company  = STRING_EMPTY;
        this.duration= INT_EMPTY;
        this.country = STRING_EMPTY;
        this.applicationCloseDate = STRING_EMPTY;

        this.views= INT_EMPTY;
        this.homeLC = STRING_EMPTY;
        this.managers = LIST_EMPTY;
        this.skills = LIST_EMPTY;
        this.backgrounds = LIST_EMPTY;
        this.visaLink = STRING_EMPTY;
        this.visaType = STRING_EMPTY;
        this.visaDuration = STRING_EMPTY;
        this.city = STRING_EMPTY;
        this.selectProcess = STRING_EMPTY;
        this.salary= INT_EMPTY;
        this.salaryCcy = STRING_EMPTY;
        this.salaryCcyCode= INT_EMPTY;
        this.createTime = STRING_EMPTY;
        this.updateTime = STRING_EMPTY;
        
    }

    /* isEmpty method */
    public boolean isEmpty() {
        // Only check Brief infos
        return id == INT_EMPTY 
            && title == STRING_EMPTY 
            && company == STRING_EMPTY 
            && duration == INT_EMPTY 
            && country == STRING_EMPTY 
            && applicationCloseDate == STRING_EMPTY;
    }

    /* Setters and Getters */
    public String getApplicationCloseDate() {
        return applicationCloseDate;
    }

    public void setApplicationCloseDate(String applicationCloseDate) {
        this.applicationCloseDate = applicationCloseDate;
    }

    public List<Background> getBackgrounds() {
        return backgrounds;
    }

    public void setBackgrounds(List<Background> backgrounds) {
        this.backgrounds = backgrounds;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getHomeLC() {
        return homeLC;
    }

    public void setHomeLC(String homeLC) {
        this.homeLC = homeLC;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Manager> getManagers() {
        return managers;
    }

    public void setManagers(List<Manager> managers) {
        this.managers = managers;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getSalaryCcy() {
        return salaryCcy;
    }

    public void setSalaryCcy(String salaryCcy) {
        this.salaryCcy = salaryCcy;
    }

    public int getSalaryCcyCode() {
        return salaryCcyCode;
    }

    public void setSalaryCcyCode(int salaryCcyCode) {
        this.salaryCcyCode = salaryCcyCode;
    }

    public String getSelectProcess() {
        return selectProcess;
    }

    public void setSelectProcess(String selectProcess) {
        this.selectProcess = selectProcess;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getVisaDuration() {
        return visaDuration;
    }

    public void setVisaDuration(String visaDuration) {
        this.visaDuration = visaDuration;
    }

    public String getVisaLink() {
        return visaLink;
    }

    public void setVisaLink(String visaLink) {
        this.visaLink = visaLink;
    }

    public String getVisaType() {
        return visaType;
    }

    public void setVisaType(String visaType) {
        this.visaType = visaType;
    }

    public static class Manager {

        private String fullName;
        private String email;

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

    }

    public static class Skill {

        private String name;
        private String option;
        private int level;

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOption() {
            return option;
        }

        public void setOption(String option) {
            this.option = option;
        }

    }

    public static class Background {

        private String name;
        private String option;

        public String getOption() {
            return option;
        }

        public void setOption(String option) {
            this.option = option;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

}
