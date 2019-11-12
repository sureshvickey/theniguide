package com.thenikaran.guide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.thenikaran.guide.Fragments.amazon;

public class Affiliate_main extends AppCompatActivity {
    String from;
    LinearLayout toolbarContainer;
    TextView title;
    FrameLayout frameLayout;
    ProgressBar progressBar;
    public static Toolbar toolbar;
    amazon fragmentAffiliate;
    public EditText searchEditText;
    public SearchView searchView;
    String key;
    public static RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affiliate_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle("Amazon");
//        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Amazon");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbarContainer = findViewById(R.id.toolbar_container);
        frameLayout= findViewById(R.id.frameLayout);

        relativeLayout=findViewById(R.id.searchLayoutmain);
        searchView=findViewById(R.id.searchView);
        relativeLayout.setVisibility(View.VISIBLE);
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        searchEditText = searchView.findViewById(id);
        searchEditText.setTextSize(16);
        loadFragment(new amazon(),false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                key=searchView.getQuery().toString();
                fragmentAffiliate=new amazon();
                Bundle bundle=new Bundle();
                bundle.putString("keyword",key);
                fragmentAffiliate.setArguments(bundle);
                loadFragment(fragmentAffiliate,false);
                //  Toast.makeText(getApplicationContext(),key,Toast.LENGTH_SHORT).show();
                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText)
            {
                return false;
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp(){
        //  finish();
        onBackPressed();
        return true;
    }

    public void loadFragment(Fragment fragment, Boolean bool) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        if (bool) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }
    public void removeCurrentFragmentAndMoveBack() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        /*FragmentTransaction trans = fragmentManager.beginTransaction();
        trans.remove(fragment);
        trans.commit();*/
        fragmentManager.popBackStack();
    }

    @Override
    public void onBackPressed() {
        //   super.onBackPressed();
        relativeLayout.setVisibility(View.VISIBLE);
        int count=getSupportFragmentManager().getBackStackEntryCount();
        if (count==0) {
//            if (!fragmentAffiliate.searchView.isIconified())
//                fragmentAffiliate.searchView.setIconified(true);
//            fragmentAffiliate.searchView.setIconified(true);

            super.onBackPressed();
        }
//        if (new AffiliateFragmentHome()==null){
//            removeCurrentFragmentAndMoveBack();
//        }
        else {
            //    toolbar.setTitle(R.string.app_name);

            super.onBackPressed();
        }
    }


}
