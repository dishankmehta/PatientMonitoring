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
import android.widget.TextView;
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
public class Tab2Fragment extends Fragment {

    private static final String GET_MED = "http://dmehta22.comli.com/get_curr_data.php";
    TextView pname1,ptemp,pblood,psugar;
    SharedPreferences pref1;
    String pat_name;
    private ProgressDialog pDialog;
    private RequestQueue mQueue;

    public Tab2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.tab2activity, container, false);

        pname1 = (TextView) view.findViewById(R.id.pname);
        ptemp = (TextView) view.findViewById(R.id.temp);
        pblood = (TextView) view.findViewById(R.id.blood);
        psugar = (TextView) view.findViewById(R.id.sugar);

        pref1 = getActivity().getSharedPreferences("EPass",0);
        pat_name = pref1.getString("Name","");

        mQueue = CustomVolleyRequestQueue.getInstance(this.getContext())
                .getRequestQueue();

        get_curr_data();

        return view;
    }


    private void get_curr_data(){
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading List..");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setCancelable(false);
        pDialog.setTitle("Connecting to server");
        pDialog.show();

        JSONObject obj = new JSONObject();
        try{
            obj.put("name",pat_name);
        }catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest req = new JsonObjectRequest(GET_MED, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //VolleyLog.v("Response:%n %s", response.toString(4));
                            pDialog.dismiss();
                            if(response.getInt("success") == 1){
                                String temp = response.getString("ptemp");
                                String blood = response.getString("pbloodp");
                                String sugar = response.getString("psugar");

                                pname1.setText(pat_name);
                                ptemp.setText(temp);
                                pblood.setText(blood);
                                psugar.setText(sugar);
                            }else{
                                Toast.makeText(getActivity(), "Some erroe maybe", Toast.LENGTH_SHORT).show();
                            }
                            Toast.makeText(getActivity(), "Data Received", Toast.LENGTH_SHORT).show();
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
                //Log.i("error", error.getMessage());
                Toast.makeText(getActivity(), "Some connection error maybe", Toast.LENGTH_SHORT).show();
            }
        });
        mQueue.add(req);

    }
}
