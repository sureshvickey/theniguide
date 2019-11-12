package com.thenikaran.guide.Fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.thenikaran.guide.MainActivity;
import com.thenikaran.guide.R;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class About extends Fragment {

    View view;
    @Bind(R.id.emailId)
    TextView emailId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_more, container, false);
        ButterKnife.bind(this, view);
        MainActivity.share.setVisibility(View.INVISIBLE);
        MainActivity.searchView.setVisibility(View.INVISIBLE);
        MainActivity.weatherimg.setVisibility(View.INVISIBLE);
        return view;
    }

    @OnClick({R.id.emailLayout,R.id.weblayout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.emailLayout:
                // perform click on Email ID
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/html");
                final PackageManager pm = getActivity().getPackageManager();
                final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
                String className = null;
                for (final ResolveInfo info : matches) {
                    if (info.activityInfo.packageName.equals("com.google.android.gm")) {
                        className = info.activityInfo.name;

                        if(className != null && !className.isEmpty()){
                            break;
                        }
                    }
                }
                emailIntent.setData(Uri.parse("mailto:"+emailId.getText().toString().trim()));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback Theni Guide App");
                emailIntent.setClassName("com.google.android.gm", className);
                try {
                    startActivity(emailIntent);
                } catch(ActivityNotFoundException ex) {
                    // handle error
                }

                break;
            case R.id.weblayout:
                Intent gintent = new Intent(android.content.Intent.ACTION_VIEW);
                //Copy App URL from Google Play Store.
                gintent.setData(Uri.parse("http://www.thenikaran.co.in/"));
                startActivity(gintent);
                break;
        }
    }

    @OnClick({R.id.googleimg})
    public void onclick(View view){
        switch (view.getId()) {
            case R.id.googleimg:
                // perform click on Email ID
                       //case R.id.googlelayout:
                    Intent gintent = new Intent(android.content.Intent.ACTION_VIEW);
                    //Copy App URL from Google Play Store.
                    gintent.setData(Uri.parse("https://plus.google.com/u/1/101863452931595447215"));
                    startActivity(gintent);

                break;
        }
    }
    @OnClick({R.id.fbimg})
    public void onfbclick(View view){
        switch (view.getId()) {
            case R.id.fbimg:
                // perform click on Email ID
                //case R.id.googlelayout:
                Intent gintent = new Intent(android.content.Intent.ACTION_VIEW);
                //Copy App URL from Google Play Store.
                gintent.setData(Uri.parse("https://www.facebook.com/theni.karan.1"));
                startActivity(gintent);

                break;
        }
    }
    @OnClick({R.id.twitimg})
    public void ontwitclick(View view){
        switch (view.getId()) {
            case R.id.twitimg:
                // perform click on Email ID
                //case R.id.googlelayout:
                Intent gintent = new Intent(android.content.Intent.ACTION_VIEW);
                //Copy App URL from Google Play Store.
                gintent.setData(Uri.parse("https://twitter.com/Thenikaran_"));
                startActivity(gintent);

                break;
        }
    }
    @OnClick({R.id.instaimg})
    public void oninstaclick(View view){
        switch (view.getId()) {
            case R.id.instaimg:
                // perform click on Email ID
                //case R.id.googlelayout:
                Intent gintent = new Intent(android.content.Intent.ACTION_VIEW);
                //Copy App URL from Google Play Store.
                gintent.setData(Uri.parse("https://www.instagram.com/thenikaran/"));
                startActivity(gintent);

                break;
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        MainActivity.share.setVisibility(View.VISIBLE);
        MainActivity.searchView.setVisibility(View.VISIBLE);
        MainActivity.weatherimg.setVisibility(View.VISIBLE);
    }

}
