package com.example.dishank.patientmonitoring;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.dishank.patientmonitoring.AdminFiles.AdminMain;
import com.example.dishank.patientmonitoring.NavigationDrawer.DoctorNavigation;
import com.example.dishank.patientmonitoring.NavigationDrawer.PatientNavigation;

import org.json.JSONException;
import org.json.JSONObject;


public class Login extends AppCompatActivity implements View.OnClickListener {

    Button blogin,signup;
    EditText email,pass;
    String username,password;
    private RadioButton bdoctor,bpatient;

    public static int COUNT = 0;

    private static final String LOGIN_URL = "http://dmehta22.comli.com/login_main_final.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_CHECK = "";
    private ProgressDialog pDialog;
    private RequestQueue mQueue;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pref = getSharedPreferences("Registration", 0);
        editor = pref.edit();

        blogin = (Button) findViewById(R.id.login_btn);
        signup = (Button) findViewById(R.id.signup_btn);

        email = (EditText) findViewById(R.id.login_id);
        pass = (EditText) findViewById(R.id.login_pass);


        blogin.setOnClickListener(this);
        signup.setOnClickListener(this);

        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();

    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.login_btn:
                username = email.getText().toString();
                password = pass.getText().toString();
                Log.i("username",username);
                Log.i("password",password);
                if(username.equals("admin") && password.equals("admin")){
                    Intent i = new Intent(Login.this, AdminMain.class);
                    startActivity(i);
                }else if(username.equals("admin1") && password.equals("admin1")){
                    Intent i = new Intent(Login.this, DoctorNavigation.class);
                    startActivity(i);
                }else if(username.equals("") && password.equals("")){
                    Toast.makeText(Login.this, "Username and password not entered.!!",Toast.LENGTH_SHORT).show();
                }else{
                    jsonconnect(username,password);

                    break;
                }
                break;
            case R.id.signup_btn:

                final Dialog dialog = new Dialog(Login.this);
                dialog.setContentView(R.layout.custom_dialog);

                ImageView image = (ImageView) dialog.findViewById(R.id.dialogimage);
                image.setImageResource(R.drawable.ic_info_black_24dp);

                Button bsubmit = (Button) dialog.findViewById(R.id.dialogbutton);

                final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radioGroup);

                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if(checkedId == R.id.radiob1){
                            Toast.makeText(getApplicationContext(), "choice: Doctor", Toast.LENGTH_SHORT).show();
                        }else if(checkedId == R.id.radiob2){
                            Toast.makeText(getApplicationContext(), "choice: Patient", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                bdoctor = (RadioButton) dialog.findViewById(R.id.radiob1);
                bpatient = (RadioButton) dialog.findViewById(R.id.radiob2);

                bsubmit.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       int selectedID = radioGroup.getCheckedRadioButtonId();
                       if(selectedID == bdoctor.getId()){
                           Intent i1 = new Intent(Login.this, DSignUp.class);
                           startActivity(i1);
                       }else if(selectedID == bpatient.getId()){
                           Intent i2 = new Intent(Login.this, PSignUp.class);
                           startActivity(i2);
                       }
                       dialog.dismiss();
                   }
                });
                dialog.show();

                break;
        }
    }


    private void jsonconnect(final String username, String password){
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Signing in...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setCancelable(false);
        pDialog.setTitle("Connecting to server");
        pDialog.show();


        JSONObject jobj = new JSONObject();
        try{
            jobj.put("email",username);
            jobj.put("password",password);
        }catch (JSONException e){
            e.printStackTrace();
        }


        JsonObjectRequest req = new JsonObjectRequest(LOGIN_URL, jobj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                            pDialog.dismiss();
                            int i = response.getInt("success");
                            int j = response.getInt("tag");
                            String msg = response.getString("message");
                            if(i==1){
                                //COUNT = response.getInt("count");
                                if(j==1) {
                                    Intent i4 = new Intent(Login.this, PatientNavigation.class);
                                    finish();
                                    startActivity(i4);
                                }else if(j==2){
                                    editor.putString("Email",username);
                                    editor.commit();
                                    Intent i3 = new Intent(Login.this, DoctorNavigation.class);
                                    finish();
                                    startActivity(i3);
                                }
                            }
                            Toast.makeText(Login.this, msg, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Login.this, "Some connection error maybe", Toast.LENGTH_SHORT).show();
            }
        });
        mQueue.add(req);
    }

    @Override
    protected void onStop() {
        super.onStop();

        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
