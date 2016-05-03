package com.example.dishank.patientmonitoring.NavigationFragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dishank.patientmonitoring.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PHomeFragment extends Fragment {




    public PHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home,container,false);


        return rootView;
    }

}
