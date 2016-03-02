package com.example.dishank.patientmonitoring.NavigationFragment;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dishank.patientmonitoring.NavigationDrawer.FragmentDrawer;
import com.example.dishank.patientmonitoring.R;
import com.example.dishank.patientmonitoring.RecycleViewList.DividerItemDecoration;
import com.example.dishank.patientmonitoring.RecycleViewList.PatientAdapter;
import com.example.dishank.patientmonitoring.RecycleViewList.PatientList;
import com.example.dishank.patientmonitoring.TabMain;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PatientListFragment extends Fragment {


    private List<PatientList> patientlist = new ArrayList<>();
    private RecyclerView recyclerView;
    private PatientAdapter padapter;
    private Dialog dialog;

    public PatientListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_mainpatientlist, container, false);



        final FragmentActivity activity = getActivity();

        recyclerView = (RecyclerView) rootview.findViewById(R.id.recycleview);

        padapter = new PatientAdapter(patientlist);
        RecyclerView.LayoutManager pLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(pLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(padapter);


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                PatientList patient = patientlist.get(position);
                Toast.makeText(getContext(), patient.getName() + " is selected!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getContext(), TabMain.class);
                startActivity(i);

            }


            @Override
            public void onLongClick(View view, int position) {
                PatientList patient = patientlist.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Want to delete "+ patient.getName() + " from the list?");
                builder.setCancelable(true);

                builder.setPositiveButton("Yes, Delete.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.setNegativeButton("No.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alert1 = builder.create();
                alert1.show();

            }
        }));
        patientdata();






        return rootview;
    }


    private void patientdata(){
        PatientList patient = new PatientList("Rakesh","Eye patient","2014");
        patientlist.add(patient);

        patient = new PatientList("Harsh","Ortho patient","2016");
        patientlist.add(patient);

        patient = new PatientList("Sulay","Ortho patient","2015");
        patientlist.add(patient);

        patient = new PatientList("Parth","Eye patient","2016");
        patientlist.add(patient);

        patient = new PatientList("Akash","Abnormal","2014");
        patientlist.add(patient);

        patient = new PatientList("Harsh","Cardio patient","2016");
        patientlist.add(patient);

        patient = new PatientList("Suchita","Ortho patient","2016");
        patientlist.add(patient);

        patient = new PatientList("Avani","Eye patient","2016");
        patientlist.add(patient);

        patient = new PatientList("Kinjal","Uterus patient","2016");
        patientlist.add(patient);

        padapter.notifyDataSetChanged();

    }

    public interface ClickListener{
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }


    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private GestureDetector gestureDetector;
        private PatientListFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final PatientListFragment.ClickListener clickListener){
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(),e.getY());
                    if(child != null && clickListener != null){
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        public RecyclerTouchListener(Context context, RecyclerView recyclerView, FragmentDrawer.ClickListener clickListener) {

        }


        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(),e.getY());
            if(child != null && clickListener != null && gestureDetector.onTouchEvent(e)){
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }


}
