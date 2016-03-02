package com.example.dishank.patientmonitoring;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dishank.patientmonitoring.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class SignUp extends AppCompatActivity implements View.OnClickListener {


    EditText fname, lname, semail, pass, cpass, sdob, cno;
    Button bsignup;

    public static final String REGISTER_URL = "http://dmehta22.comli.com/Register.php";
    public static final String COUNT_URL = "http://dmehta22.comli.com/get_count.php";
    private static int COUNT;
    private static boolean verify_flag = false;


    private static String firstname = null;
    private static String lastname = null;
    private static String email = null;
    private static String password = null;
    private static String cpassword = null;
    private static String dob = null;
    private static String contactno = null;

    private ProgressDialog pDialog;
    private RequestQueue mQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        fname = (EditText) findViewById(R.id.signup_fn);
        lname = (EditText) findViewById(R.id.signup_ln);
        semail = (EditText) findViewById(R.id.signup_email);
        pass = (EditText) findViewById(R.id.signup_password);
        cpass = (EditText) findViewById(R.id.signup_confirm_password);
        sdob = (EditText) findViewById(R.id.signup_dob);
        cno = (EditText) findViewById(R.id.signup_contactno);

        bsignup = (Button) findViewById(R.id.signup_btn);


        bsignup.setOnClickListener(this);


        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();

        get_count();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.signup_btn:
                set();
                verify();
                if (verify_flag) {
                    insert_data();
                }
                break;
        }
    }

    public void set() {
        firstname = fname.getText().toString().trim();
        lastname = lname.getText().toString().trim();
        email = semail.getText().toString().trim();
        password = pass.getText().toString().trim();
        cpassword = cpass.getText().toString().trim();
        dob = sdob.getText().toString().trim();
        contactno = cno.getText().toString().trim();
    }


    private void verify() {
        AlertDialog alertDialog = new AlertDialog.Builder(SignUp.this).create();
        alertDialog.setTitle("Error..!!");
        alertDialog.setMessage("Please fill all the details..!!");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        if (firstname.equals("")) {
            alertDialog.setMessage("Enter your first name..!!");
            alertDialog.show();
        } else if (lastname.equals("")) {
            alertDialog.setMessage("Enter your last name..!!");
            alertDialog.show();
        } else if (email.equals("")) {
            alertDialog.setMessage("Enter your email-id..!!");
            alertDialog.show();
        } else if (password.equals("")) {
            alertDialog.setMessage("Set your password..!!");
            alertDialog.show();
        } else if (cpassword.equals("")) {
            alertDialog.setMessage("Confirm your password..!!");
            alertDialog.show();
        } else if (dob.equals("")) {
            alertDialog.setMessage("Enter your date of birth..!!");
            alertDialog.show();
        } else if (contactno.equals("")) {
            alertDialog.setMessage("Enter your contactno..!!");
            alertDialog.show();
        } else if (!password.equals(cpassword)) {
            alertDialog.setMessage("Your password doesnt match...!!");
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
                            Toast.makeText(SignUp.this, msg, Toast.LENGTH_SHORT).show();
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

        pDialog = new ProgressDialog(SignUp.this);
        pDialog.setMessage("Connecting to FTP Server...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setCancelable(false);
        pDialog.setTitle("Processing");
        pDialog.show();




        pDialog.setMessage("Inserting into the database...");

        JSONObject insert = new JSONObject();
        try {
            insert.put("pid", COUNT + 1);
            insert.put("firstname", firstname);
            insert.put("lastname", lastname);
            insert.put("email", email);
            insert.put("password", password);
            insert.put("cpassword", cpassword);
            insert.put("dob", dob);
            insert.put("contactno", contactno);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final AlertDialog alertDialog = new AlertDialog.Builder(SignUp.this).create();
        alertDialog.setTitle("Status");
        alertDialog.setMessage("");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();

                    }
                });


        JsonObjectRequest req = new JsonObjectRequest(REGISTER_URL, insert,
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

                            Toast.makeText(SignUp.this, msg, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SignUp.this, "Some Connection error maybe", Toast.LENGTH_SHORT).show();
                Log.i("err",error.toString());
            }
        });
        mQueue.add(req);

    }





        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_signup, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
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
