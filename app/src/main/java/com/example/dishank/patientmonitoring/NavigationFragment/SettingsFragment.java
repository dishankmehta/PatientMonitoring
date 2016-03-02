package com.example.dishank.patientmonitoring.NavigationFragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dishank.patientmonitoring.Login;
import com.example.dishank.patientmonitoring.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    ListView slist;
    String[] values = new String[]{"Settings", "Logout"};

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        slist = (ListView) rootView.findViewById(R.id.settingslist);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, values);
        slist.setAdapter(adapter);

        slist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //int itempos = position;
                String itemValue = (String) slist.getItemAtPosition(position);

                switch(itemValue){
                    case "Settings":

                    case "Logout":
                        Intent i = new Intent(getActivity(), Login.class);
                        startActivity(i);
                }
            }
        });

        return rootView;
    }

}
