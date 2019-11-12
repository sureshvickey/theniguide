package com.thenikaran.guide.Fragments;


import android.os.Bundle;

import com.thenikaran.guide.MainActivity;
import com.thenikaran.guide.R;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class Home extends Fragment {

    View view;

    @Bind(R.id.simpleTabLayout)
    TabLayout simpleTabLayout;
    @Bind(R.id.viewpager)
    public ViewPager viewPager;
    ViewPager.OnPageChangeListener mOnPageChangeListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        MainActivity.drawerLayout.closeDrawers(); // close drawer
        createTabs(); // create custom tabs

        return view;
    }

    private void createTabs() {
        setupViewPager(viewPager);
        simpleTabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new Categories(), "Categories");
        adapter.addFragment(new AllPlaces(), "All Places");
        adapter.addFragment(new thenibus(), "Buses");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
