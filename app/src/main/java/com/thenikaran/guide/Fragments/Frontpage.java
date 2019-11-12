package com.thenikaran.guide.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thenikaran.guide.R;


public class Frontpage extends Fragment {
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_frontpage, container, false);
        ButterKnife.bind(this, view);

        return view;
    }
    @OnClick({R.id.placeImage,R.id.placeImage1})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.placeImage:
                loadFragment(new thenibus(),false);
                break;
            case R.id.placeImage1:
                loadFragment(new Home(),false);
                break;
        }
    }
    public void loadFragment(Fragment fragment, Boolean bool) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        if (bool)
            transaction.addToBackStack(null);
        transaction.commit();
    }


//    Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.frameLayout);
//    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//            transaction.add(R.id.frameLayout, new Home(), "tour");
//            transaction.hide(currentFragment);
//            transaction.addToBackStack(null);
//            transaction.commit();
//    public void removeCurrentFragmentAndMoveBack() {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        /*FragmentTransaction trans = fragmentManager.beginTransaction();
//        trans.remove(fragment);
//        trans.commit();*/
//        fragmentManager.popBackStack();
//    }
//    @Override
//    public void onBackPressed() {
//        //super.onBackPressed();
//        RailwayMenu.toolbar.setTitle("RAILWAY");
//        int count=getSupportFragmentManager().getBackStackEntryCount();
//        if (count==0){
//            super.onBackPressed();
//        }
//        removeCurrentFragmentAndMoveBack();
//    }
}
