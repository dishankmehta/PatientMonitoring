package com.example.dishank.patientmonitoring.AdminFiles;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.dishank.patientmonitoring.R;

/**
 * Created by Dishank on 4/30/2016.
 */
public class AdminMain extends AppCompatActivity implements View.OnClickListener {

    Button badddoc,baddpat,bremdoc,brempat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        badddoc = (Button) findViewById(R.id.add_doc);
        baddpat = (Button) findViewById(R.id.add_pat);

        bremdoc = (Button) findViewById(R.id.rmv_doc);
        brempat = (Button) findViewById(R.id.rmv_pat);


        badddoc.setOnClickListener(this);
        baddpat.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.add_pat:
                Intent i = new Intent(AdminMain.this, AdminPatientAdd.class);
                startActivity(i);
                break;
        }
    }
}
