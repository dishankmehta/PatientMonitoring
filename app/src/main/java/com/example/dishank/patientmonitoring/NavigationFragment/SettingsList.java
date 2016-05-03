package com.example.dishank.patientmonitoring.NavigationFragment;

/**
 * Created by Dishank on 4/6/2016.
 */
public class SettingsList {

    private String title;
    private int image;

    public SettingsList(){

    }

    public SettingsList(int image, String title){
        this.image = image;
        this.title = title;
    }



    public int getImage(){
        return image;
    }

    public void setImage(int image1){
        this.image = image1;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title1){
        this.title = title1;
    }
    
}
