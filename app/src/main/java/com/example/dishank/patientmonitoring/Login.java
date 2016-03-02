package com.example.dishank.patientmonitoring;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dishank.patientmonitoring.NavigationDrawer.NavigationActivity;


public class Login extends AppCompatActivity implements View.OnClickListener {

    Button blogin,signup;
    EditText email,password;
    String s1,s2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        blogin = (Button) findViewById(R.id.login_btn);
        signup = (Button) findViewById(R.id.signup_btn);

        email = (EditText) findViewById(R.id.login_id);
        password = (EditText) findViewById(R.id.login_pass);

        blogin.setOnClickListener(this);
        signup.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.login_btn:
                s1 = email.getText().toString();
                s2 = password.getText().toString();
                if(s1.equals("doctor") && s2.equals("doctor")){
                    Intent i = new Intent(Login.this, NavigationActivity.class);
                    startActivity(i);
                }else{
                    Toast.makeText(Login.this, "Wrong ID and password.!", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.signup_btn:
                Intent i1 = new Intent(Login.this, SignUp.class);
                startActivity(i1);
                break;
        }
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
