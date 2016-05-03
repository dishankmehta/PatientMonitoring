package com.example.dishank.patientmonitoring.NavigationFragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.dishank.patientmonitoring.CustomVolleyRequestQueue;
import com.example.dishank.patientmonitoring.R;
import com.example.dishank.patientmonitoring.RecycleViewList.DividerItemDecoration;
import com.example.dishank.patientmonitoring.RecycleViewList.PatientAdapter;
import com.example.dishank.patientmonitoring.RecycleViewList.PatientList;
import com.example.dishank.patientmonitoring.TabMain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PatientListFragment extends Fragment {


    private static final String GET_DATA = "http://dmehta22.comli.com/get_list_data.php";
    private List<PatientList> patientlist = new ArrayList<>();
    private RecyclerView recyclerView;
    private PatientAdapter padapter;
    private Dialog dialog;
    String doc_email;
    String pname,pprob,pyear;
    SharedPreferences pref;


    private ProgressDialog pDialog;
    private RequestQueue mQueue;

    public PatientListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_mainpatientlist, container, false);

        pref = getActivity().getSharedPreferences("Registration",0);
        doc_email = pref.getString("Email","");

        Toast.makeText(getActivity(),doc_email,Toast.LENGTH_SHORT).show();

        mQueue = CustomVolleyRequestQueue.getInstance(this.getContext()).getRequestQueue();


        recyclerView = (RecyclerView) rootview.findViewById(R.id.recycleview);

        padapter = new PatientAdapter(patientlist);
        RecyclerView.LayoutManager pLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(pLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(padapter);
        get_data();

        patientlist.clear();

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
       // patientdata();






        return rootview;
    }


   /* private void patientdata(){
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

    }*/


    private void get_data(){
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading List..");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setCancelable(false);
        pDialog.setTitle("Connecting to server");
        pDialog.show();

        JSONObject obj = new JSONObject();
        try{
            obj.put("doc_email",doc_email);
        }catch (JSONException e){
            e.printStackTrace();
        }


        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, GET_DATA, obj,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                int len = response.length();
                JSONArray id,pat_name,pat_prob,pat_year;
                try{
                    id = response.getJSONArray(0);
                    pat_name = response.getJSONArray(1);
                    pat_prob = response.getJSONArray(2);
                    pat_year = response.getJSONArray(3);

                    for(int i=0; i< id.length(); i++){
                        PatientList patient = new PatientList();
                        patient.setName(pat_name.getString(i));
                        patient.setDes(pat_prob.getString(i));
                        patient.setYear(pat_year.getString(i));
                        patientlist.add(patient);
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
                padapter.notifyDataSetChanged();
                pDialog.dismiss();
                Toast.makeText(getActivity(),"Success", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                VolleyLog.e("Error: ", error.getMessage());
                error.printStackTrace();
                Toast.makeText(getActivity(),"Some Connection error maybe", Toast.LENGTH_SHORT).show();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mQueue.add(request);
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

       /* public RecyclerTouchListener(Context context, RecyclerView recyclerView, FragmentDrawer.ClickListener clickListener) {

        }*/



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
