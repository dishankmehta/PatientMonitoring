package com.example.dishank.patientmonitoring.RecycleViewList;

/**
 * Created by Dishank on 1/30/2016.
 */
public class PatientList {

    private String name,des,year;

    public PatientList(){

    }

    public PatientList(String name, String des, String year){
        this.name = name;
        this.des = des;
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name1) {
        this.name = name1;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des1) {
        this.des = des1;
    }
}
