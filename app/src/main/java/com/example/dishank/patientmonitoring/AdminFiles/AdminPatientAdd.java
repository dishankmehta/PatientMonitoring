package com.example.dishank.patientmonitoring.AdminFiles;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
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
 * Created by Dishank on 4/30/2016.
 */
public class AdminPatientAdd extends Activity implements View.OnClickListener {

    EditText edoc,epat,epatname,epob,epyear;
    Button bpatadd;
    private static int COUNT;
    private static boolean verify_flag = false;

    public static final String PATIENTADD_URL = "http://dmehta22.comli.com/pat_data.php";
    public static final String COUNT_URL = "http://dmehta22.comli.com/get_count_pat_data.php";

    private static String doc_email = null;
    private static String pat_email = null;
    private static String pat_name = null;
    private static String pat_prob = null;
    private static String pat_year = null;

    private ProgressDialog pDialog;
    private RequestQueue mQueue;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminpatientadd);

        edoc = (EditText) findViewById(R.id.docemail);
        epat = (EditText) findViewById(R.id.patemail);
        epatname = (EditText) findViewById(R.id.patname);
        epob = (EditText) findViewById(R.id.patprob);
        epyear = (EditText) findViewById(R.id.patyear);

        bpatadd = (Button) findViewById(R.id.addpat);

        bpatadd.setOnClickListener(this);

        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();

        get_count();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addpat:
                set();
                verify();
                if(verify_flag){
                    insert_data();
                }
                break;
        }
    }

    public void set(){
        doc_email = edoc.getText().toString().trim();
        pat_email = epat.getText().toString().trim();
        pat_name = epatname.getText().toString().trim();
        pat_prob = epob.getText().toString().trim();
        pat_year = epyear.getText().toString().trim();
    }

    private void verify() {
        AlertDialog alertDialog = new AlertDialog.Builder(AdminPatientAdd.this).create();
        alertDialog.setTitle("Error..!!");
        alertDialog.setMessage("Please fill all the details..!!");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        if (doc_email.equals("")) {
            alertDialog.setMessage("Enter doctor email..!!");
            alertDialog.show();
        } else if (pat_email.equals("")) {
            alertDialog.setMessage("Enter patient email..!!");
            alertDialog.show();
        } else if (pat_name.equals("")) {
            alertDialog.setMessage("Enter patient name..!!");
            alertDialog.show();
        }else if (pat_prob.equals("")) {
            alertDialog.setMessage("Enter patient problem..!!");
            alertDialog.show();
        } else if (pat_year.equals("")) {
            alertDialog.setMessage("Enter patient year..!!");
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
                            Toast.makeText(AdminPatientAdd.this, msg, Toast.LENGTH_SHORT).show();
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

        pDialog = new ProgressDialog(AdminPatientAdd.this);
        pDialog.setMessage("Connecting to FTP Server...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setCancelable(false);
        pDialog.setTitle("Processing");
        pDialog.show();




        pDialog.setMessage("Inserting into the database...");

        JSONObject insert = new JSONObject();
        try {
            insert.put("id", COUNT + 1);
            insert.put("doc_email", doc_email);
            insert.put("pat_email", pat_email);
            insert.put("pat_name", pat_name);
            insert.put("pat_prob", pat_prob);
            insert.put("pat_year", pat_year);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final AlertDialog alertDialog = new AlertDialog.Builder(AdminPatientAdd.this).create();
        alertDialog.setTitle("Status");
        alertDialog.setMessage("");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // finish();
                    }
                });


        JsonObjectRequest req = new JsonObjectRequest(PATIENTADD_URL, insert,
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

                            Toast.makeText(AdminPatientAdd.this, msg, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(AdminPatientAdd.this, "Some Connection error maybe", Toast.LENGTH_SHORT).show();
                Log.i("err",error.toString());
            }
        });
        mQueue.add(req);

    }


}
