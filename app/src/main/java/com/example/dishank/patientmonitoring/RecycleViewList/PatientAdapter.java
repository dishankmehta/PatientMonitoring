package com.example.dishank.patientmonitoring.RecycleViewList;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dishank.patientmonitoring.R;

import java.util.List;

/**
 * Created by Dishank on 1/30/2016.
 */
public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.MyViewHolder> {

    private List<PatientList> patientlist;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView name,des,year;

        public MyViewHolder(View view){
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            des = (TextView) view.findViewById(R.id.des);
            year = (TextView) view.findViewById(R.id.year);
        }
    }

    public PatientAdapter(List<PatientList> patientlist){
        this.patientlist = patientlist;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PatientList patient = patientlist.get(position);
        holder.name.setText(patient.getName());
        holder.des.setText(patient.getDes());
        holder.year.setText(patient.getYear());
    }

    @Override
    public int getItemCount() {
        return patientlist.size();
    }
}
