package com.example.dishank.patientmonitoring.TabsFragment;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class Tab1Fragment extends Fragment {


    private static final String GET_MED = "http://dmehta22.comli.com/get_med_data.php";
    TextView pissue,pmed,prob;
    SharedPreferences pref1;
    String pat_name;
    private ProgressDialog pDialog;
    private RequestQueue mQueue;

    public Tab1Fragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1activity, container, false);

        pissue =(TextView) view.findViewById(R.id.issue);
        pmed =(TextView) view.findViewById(R.id.medi);
        prob =(TextView) view.findViewById(R.id.prob);

        pref1 = getActivity().getSharedPreferences("EPass",0);
        pat_name = pref1.getString("Name","");

        Toast.makeText(getActivity(), pat_name, Toast.LENGTH_SHORT).show();

        mQueue = CustomVolleyRequestQueue.getInstance(this.getContext())
                .getRequestQueue();

        get_med_data();

        return view;
    }

    private void get_med_data(){
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
                                String med_issue = response.getString("med_issue");
                                String cur_med = response.getString("cur_med");
                                String cur_issue = response.getString("cur_issue");

                                pissue.setText(med_issue);
                                pmed.setText(cur_med);
                                prob.setText(cur_issue);
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
