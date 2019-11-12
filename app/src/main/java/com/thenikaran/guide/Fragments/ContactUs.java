package com.thenikaran.guide.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.thenikaran.guide.MainActivity;
import com.thenikaran.guide.R;

public class ContactUs extends Fragment {

    View view;
   String fileName = "contactus.html";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_about_us, container, false);
        MainActivity.share.setVisibility(View.INVISIBLE);
        MainActivity.weatherimg.setVisibility(View.INVISIBLE);
        MainActivity.searchView.setVisibility(View.INVISIBLE);
        // display content of local HTML file
       WebView webView = (WebView) view.findViewById(R.id.simpleWebView);
        webView.getSettings().setJavaScriptEnabled(true);
        WebSettings webSettings=webView.getSettings();
        webSettings.setDefaultTextEncodingName("utf-8");
        webView.loadUrl("file:///android_asset/" + fileName);

    /*    Intent intent=new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","thenikaran@outlook.com", null));

        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        intent.putExtra(Intent.EXTRA_TEXT, "Write what you want");

        startActivity(Intent.createChooser(intent,"Send Email"));  */

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        MainActivity.share.setVisibility(View.VISIBLE);
        MainActivity.searchView.setVisibility(View.VISIBLE);
        MainActivity.weatherimg.setVisibility(View.VISIBLE);
        WebView webView = (WebView) view.findViewById(R.id.simpleWebView);
        webView.getSettings().setJavaScriptEnabled(true);
        WebSettings webSettings=webView.getSettings();
        webSettings.setDefaultTextEncodingName("utf-8");
        webView.loadUrl("file:///android_asset/" + fileName);
    }


}
