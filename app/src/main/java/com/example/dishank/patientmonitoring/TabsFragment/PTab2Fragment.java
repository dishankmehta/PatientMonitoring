package com.example.dishank.patientmonitoring.TabsFragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.dishank.patientmonitoring.CustomVolleyRequestQueue;
import com.example.dishank.patientmonitoring.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class PTab2Fragment extends Fragment implements View.OnClickListener {

    EditText name,temp,bloodp,sugar;
    Button benter;

    private static final String MEDDATA_URL = "http://dmehta22.comli.com/med_data.php";
    private static final String COUNT_URL = "http://dmehta22.comli.com/get_count_data.php";
    private static boolean verify_flag = false;

    private static int COUNT;
    private static String Temp = null;
    private static String Bloodp = null;
    private static String Sugar = null;
    private static String Name = null;

    private ProgressDialog pDialog;
    private RequestQueue mQueue;

    SharedPreferences pref;
    String pat_email;


    public PTab2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ptab2, container, false);

        name = (EditText) rootView.findViewById(R.id.nameentry);
        temp = (EditText) rootView.findViewById(R.id.feverentry);
        bloodp = (EditText) rootView.findViewById(R.id.bpentry);
        sugar = (EditText) rootView.findViewById(R.id.sugarentry);

        benter = (Button) rootView.findViewById(R.id.enterb);

        benter.setOnClickListener(this);

        pref = getActivity().getSharedPreferences("Registration",0);
        pat_email = pref.getString("EmailP","");

        mQueue = CustomVolleyRequestQueue.getInstance(this.getActivity()).getRequestQueue();

        get_count();

        return rootView;
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.enterb:
                set();
                verify();
                if(verify_flag){
                    insert_data();
                }
                break;
        }
    }

    public void set(){
        Name = name.getText().toString().trim();
        Temp = temp.getText().toString().trim();
        Bloodp = bloodp.getText().toString().trim();
        Sugar = sugar.getText().toString().trim();
    }

    private void verify(){
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Error..!!");
        alertDialog.setMessage("Please fill all the details..!!");
        alertDialog.setButton(android.support.v7.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        if(Name.equals("")){
            alertDialog.setMessage("Enter name..!!");
            alertDialog.show();
        }else if(Temp.equals("")){
            alertDialog.setMessage("Enter temperature..!!");
            alertDialog.show();
        }else if(Bloodp.equals("")){
            alertDialog.setMessage("Enter blood pressure..!!");
            alertDialog.show();
        }else if(Sugar.equals("")){
            alertDialog.setMessage("Enter sugar level..!!");
            alertDialog.show();
        }else{
            verify_flag=true;
        }
    }

    private void get_count() {

        JSONObject abc = new JSONObject();
        JsonObjectRequest count_req = new JsonObjectRequest(COUNT_URL, abc,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));

                            COUNT = response.getInt("count");

                            String msg = "" + COUNT;
                            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.e("Error: ", error.getMessage());
                error.printStackTrace();


            }
        });
        mQueue.add(count_req);
    }

    private void insert_data() {

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Connecting to FTP Server...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setCancelable(false);
        pDialog.setTitle("Processing");
        pDialog.show();




        pDialog.setMessage("Inserting into the database...");

        JSONObject insert = new JSONObject();
        try {
            insert.put("eid", COUNT + 1);
            insert.put("email",pat_email);
            insert.put("pname",Name);
            insert.put("ptemp", Temp);
            insert.put("pbloodp", Bloodp);
            insert.put("psugar", Sugar);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final android.support.v7.app.AlertDialog alertDialog = new android.support.v7.app.AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Status");
        alertDialog.setMessage("");
        alertDialog.setButton(android.support.v7.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //finish();
                    }
                });


        JsonObjectRequest req = new JsonObjectRequest(MEDDATA_URL, insert,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                            pDialog.dismiss();
                            int i = response.getInt("success");
                            String msg = response.getString("message");
                            if (i == 1) {
                                alertDialog.setMessage(msg);
                                alertDialog.show();
                            }

                            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                VolleyLog.e("Error: ", error.getMessage());
                error.printStackTrace();
                Toast.makeText(getActivity(), "Some Connection error maybe", Toast.LENGTH_SHORT).show();
                Log.i("err", error.toString());
            }
        });
        mQueue.add(req);

    }

}
