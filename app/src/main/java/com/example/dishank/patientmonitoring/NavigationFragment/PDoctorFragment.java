package com.example.dishank.patientmonitoring.NavigationFragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dishank.patientmonitoring.R;
import com.example.dishank.patientmonitoring.TabsFragment.PTab1Fragment;
import com.example.dishank.patientmonitoring.TabsFragment.PTab2Fragment;
import com.example.dishank.patientmonitoring.TabsFragment.PTab3Fragment;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PDoctorFragment extends Fragment {


    private TabLayout ptabLayout;
    private ViewPager pviewPager;


    public PDoctorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pdoctor, container, false);


        pviewPager = (ViewPager) rootView.findViewById(R.id.pviewpager);
        setupViewPager(pviewPager);

        ptabLayout = (TabLayout) rootView.findViewById(R.id.ptabs);
        ptabLayout.setupWithViewPager(pviewPager);


        return rootView;
    }

    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(new PTab1Fragment(), "PASTDATA");
        adapter.addFragment(new PTab2Fragment(), "DIAGNOSIS");
        adapter.addFragment(new PTab3Fragment(), "COMMENTS");
        viewPager.setAdapter(adapter);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {


        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fmanager) {
            super(fmanager);
        }

        public void addFragment(Fragment fragment, String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }

}
