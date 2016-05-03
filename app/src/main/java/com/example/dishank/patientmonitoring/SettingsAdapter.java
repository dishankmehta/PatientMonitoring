package com.example.dishank.patientmonitoring;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dishank.patientmonitoring.NavigationFragment.SettingsList;


import java.util.List;

/**
 * Created by Dishank on 4/6/2016.
 */
public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.MyViewHolder> {

    private List<SettingsList> settingsLists;

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView Title;
        public ImageView Image;

        public MyViewHolder(View itemView) {
            super(itemView);
            Title = (TextView) itemView.findViewById(R.id.stext);
            Image = (ImageView) itemView.findViewById(R.id.simage);
        }
    }

    public SettingsAdapter(List<SettingsList> settingsLists){
        this.settingsLists = settingsLists;
    }

    @Override
    public SettingsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_row, parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SettingsAdapter.MyViewHolder holder, int position) {
        SettingsList slist = settingsLists.get(position);
        holder.Image.setImageResource(slist.getImage());
        holder.Title.setText(slist.getTitle());
    }

    @Override
    public int getItemCount() {
        return settingsLists.size();
    }
}
