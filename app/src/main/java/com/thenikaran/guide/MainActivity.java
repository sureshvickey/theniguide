package com.thenikaran.guide;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import com.squareup.picasso.Picasso;
import com.thenikaran.guide.Fragments.PlaceDetail;
import com.google.firebase.analytics.FirebaseAnalytics;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.thenikaran.guide.Fragments.Categories;
import com.thenikaran.guide.Fragments.Home;
import com.thenikaran.guide.Fragments.AllPlaces;
import com.thenikaran.guide.background.asynTask;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends FragmentActivity implements LocationListener, asynTask.OnTaskCompleted {

    @Bind(R.id.recyclerview)
    RecyclerView recyclerView;
    public static DrawerLayout drawerLayout;
    public static List<String> menuTitles;
    public static ArrayList<Integer> menuIcons = new ArrayList<>(Arrays.asList(R.drawable.home_icon, R.drawable.offericon,R.drawable.star_icon, R.drawable.contact_icon, R.drawable.rate_icon, R.drawable.about_icon, R.drawable.more_icon));
    public static CustomDrawerAdapter customDrawerAdapter;
    private AdView mAdView;
    public static ImageView menu, share, search;
    public static TextView title;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    boolean doubleBackToExitPressedOnce = false;
    private FirebaseAnalytics mFirebaseAnalytics;
    public static SearchView searchView;
    public static ArrayList<String> imageIds = new ArrayList<>();
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    boolean isGPSEnabled, isNetworkEnabled;
    LocationManager locationManager;
    Location location;
    public static double latitude, longitude;
    private PrefManager prefManager;
    public static boolean firstTime = true;
    public static boolean weatherDialog=false;
    public static ImageView weatherimg;
    Dialog MyDialog;
    TextView hello,close,alertTitle,placeText,humidityText,pressureText,tempText;
    ImageView alertImage;
    public static String tempraturetxt="";
    public static String humidity="";
    public static String pressure="";
    public static String cityname="";
    public static float tempraturevalue=0;
    TextView alertMessage;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.activity_main);
            ButterKnife.bind(this);
            prefManager = new PrefManager(this);
            getFavoriteData(); // get saved favorite list data

            // Obtain the FirebaseAnalytics instance.
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
            menuTitles = Arrays.asList(getResources().getStringArray(R.array.menuArray));
            title = (TextView) findViewById(R.id.title);
            menu = (ImageView) findViewById(R.id.menu);
            share = (ImageView) findViewById(R.id.share);
            search = (ImageView) findViewById(R.id.search);
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            searchView = (SearchView) findViewById(R.id.searchView);
            weatherimg=(ImageView) findViewById(R.id.weather);
            weatherimg.setVisibility(View.VISIBLE);
            // customized searchView
            int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
            EditText searchEditText = (EditText) searchView.findViewById(id);
            searchEditText.setTextColor(getResources().getColor(R.color.color_white));
            searchEditText.setHintTextColor(getResources().getColor(R.color.light_white));
            // display Banner Ads
            mAdView = (AdView) findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice("A9C93B8F0F67284AB70DF28784CA0F1C")
                    .build();
            mAdView.loadAd(adRequest);
            // load home fragment
            loadFragment(new Home(), false);
            setRecyclerData(); // set drawer items
            // implement onQueryTextListener on searchView
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    // filter news list
                    String text = s;
                    AllPlaces.allPlacesAdapter.filter(text);
                    Categories.categoriesAdapter.filter(text);
                    return false;
                }
            });
            displayFirebaseRegId(); // display firebase id
            if (SplashScreen.id.length() > 0) {
                Intent intent = new Intent(MainActivity.this, PlaceDetail.class);
                intent.putExtra("pos", 0);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            //blink();
            if(DetectConnection.checkInternetConnection(MainActivity.this)&&location!=null) {
                asynTask asynTask = new asynTask(MainActivity.this);
                asynTask.execute("https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=886705b4c1182eb1c69f28eb8c520e20&units=metric");
            }
            weatherimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                    if(DetectConnection.checkInternetConnection(MainActivity.this)&&location!=null) {
                        weatherDialog=true;
                        progressDialog=new ProgressDialog(MainActivity.this);
                        progressDialog.setMessage("Collecting Weather Data...");
                        progressDialog.show();
                        asynTask asynTask = new asynTask(MainActivity.this);
                        asynTask.execute("https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=886705b4c1182eb1c69f28eb8c520e20&units=metric");

                }else{
                        if (progressDialog!=null){
                            progressDialog.dismiss();
                        }
Toast.makeText(MainActivity.this,"Location not detected",Toast.LENGTH_SHORT).show();
                    }}catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            updateweather();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void getFavoriteData() {
        sharedPreferences = getSharedPreferences("favoriteData", 0);
        editor = sharedPreferences.edit();
        Log.d("favoriteData", sharedPreferences.getString("data", "0"));
        String data = sharedPreferences.getString("data", "0");
        data = data.replace("[", "");
        data = data.replace("]", "");
        String[] array = data.split(", ");
        imageIds = new ArrayList<>(Arrays.asList(array));
        Log.d("arrayList", imageIds.toString().trim());
    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);
        Log.e("FCM", "Firebase reg id: " + regId);
        if (!TextUtils.isEmpty(regId)) {
        } else
            Log.d("Firebase", "Firebase Reg Id is not received yet!");
    }
    private void blink(){
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int timeToBlink = 1000;    //in milissegunds
                try{Thread.sleep(timeToBlink);}catch (Exception e) {}
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        if(title.getVisibility() == View.VISIBLE){
                            title.setVisibility(View.INVISIBLE);
                        }else{
                            title.setVisibility(View.VISIBLE);
                        }
                        blink();
                    }
                });
            }
        }).start();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    protected void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    private void setRecyclerData() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        customDrawerAdapter = new CustomDrawerAdapter(MainActivity.this, menuTitles, menuIcons);
        recyclerView.setAdapter(customDrawerAdapter);
    }


    @OnClick({R.id.menuHomeImage, R.id.menu, R.id.share, R.id.search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menuHomeImage:
                drawerLayout.closeDrawers();
                CustomDrawerAdapter.selected_item = 0;
                customDrawerAdapter.notifyDataSetChanged();
                title.setText(menuTitles.get(0));
                loadFragment(new Home(), false);
                break;
            case R.id.menu:
                if (!MainActivity.drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    MainActivity.drawerLayout.openDrawer(Gravity.LEFT);
                }
                break;
            case R.id.share:
                shareApp();
                break;

        }
    }


    public void shareApp() {
        // share app with your friends
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/*");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Try this City Info App: https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
        startActivity(Intent.createChooser(shareIntent, "Share Using"));
    }

    @Override
    public void onBackPressed() {
        title.setText("THENI GUIDE");
        // double press to exit
        int count=getSupportFragmentManager().getBackStackEntryCount();
        if (count==0) {

            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press back once more to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
        else{
            super.onBackPressed();
        }


    }

    public void loadFragment(Fragment fragment, Boolean bool) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        if (bool)
            transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("onStart", "onStartCalled");
        checkConnection();
    }

    public void updateLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (isGPSEnabled) {
            Log.d("GPS", "Enabled");
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

                return;
            }
            getLocation();

        } else {
            if (firstTime) {
                GPSManager gps = new GPSManager(MainActivity.this);
                gps.start();
                firstTime = false;
            }
        }


    }

    private void turnGPSOn() {
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (!provider.contains("gps")) { //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }

    private void getLocation() {
try {
    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        // TODO: Consider calling
        return;
    }
    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 0, this);
    if (locationManager != null) {
        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    }
    // if GPS Enabled get lat/long using GPS Services
    if (isGPSEnabled) {
        if (location == null) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, this);
            Log.d("GPS Enabled", "GPS Enabled");
            if (locationManager != null) {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            }
        }
    }
    if (location != null) {
        onLocationChanged(location);
    }
}catch(Exception e){
    e.printStackTrace();
}
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                prefManager.setFirstTimeLaunch(false);
                getLocation();
            }
        }
    }


    private void checkConnection() {
        if (isNetworkAvailable())
            updateLocation();

    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Log.d("LocationCoordinates", latitude + "   " + longitude);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void updateweather(){
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int timeToBlink = 100000;    //in milissegunds
                try{Thread.sleep(timeToBlink);}catch (Exception e) {}
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        if(DetectConnection.checkInternetConnection(MainActivity.this)&&location!=null){
                            asynTask asynTask =new asynTask(MainActivity.this);
                            asynTask.execute("https://api.openweathermap.org/data/2.5/weather?lat="+latitude+"&lon="+longitude+"&appid=886705b4c1182eb1c69f28eb8c520e20&units=metric");
                        }
                        updateweather();
                    }
                });
            }
        }).start();
    }
    @Override
    public void onTaskCompleted(String response) {
        //weathertext.setVisibility(View.VISIBLE);
        String temp="";
        String city="";
        float temprature=0;
        try {
            JSONObject jsonObject= new JSONObject(response);
            JSONObject main=jsonObject.getJSONObject("main");
            temp=main.getString("temp");
            temprature=Float.parseFloat(temp);
            tempraturevalue=temprature;
            tempraturetxt=""+temprature;
            cityname=jsonObject.getString("name");
            humidity=main.getString("humidity");
            pressure=main.getString("pressure");

            if (temprature<=25){
                weatherimg.setBackgroundResource(R.drawable.storm);
            }else if (temprature>=35){
                weatherimg.setBackgroundResource(R.drawable.sun);
            }else{
                weatherimg.setBackgroundResource(R.drawable.cloud);
            }
if (weatherDialog){
            weatherDialog=false;
            MyDialog = new Dialog(MainActivity.this);
            MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            MyDialog.setContentView(R.layout.customdialog);
            MyDialog.setTitle("Weather Report");

            alertTitle =(TextView)MyDialog.findViewById(R.id.alerttitle);
            tempText =(TextView)MyDialog.findViewById(R.id.degreealert);
            tempText.setText(tempraturetxt + (char) 0x00B0+"c  " );
            humidityText =(TextView)MyDialog.findViewById(R.id.humidity);
            humidityText.setText("Humidity :"+humidity+"%");
            pressureText =(TextView)MyDialog.findViewById(R.id.pressure);
            pressureText.setText("Pressure: "+pressure+" mb");
            placeText =(TextView)MyDialog.findViewById(R.id.place);
            placeText.setText("Place : "+cityname);
            hello = (TextView)MyDialog.findViewById(R.id.hello);
            close = (TextView)MyDialog.findViewById(R.id.close);
            alertImage=(ImageView) MyDialog.findViewById(R.id.alertimg);
            alertMessage=(TextView)MyDialog.findViewById(R.id.alertmsg);

            if (tempraturevalue<=25){
//                        Picasso.with(MainActivity.this).load(R.drawable.storm)
//                                .placeholder(R.color.color_white)
//                                .error(R.drawable.error_image).into(alertImage);
                alertImage.setBackgroundResource(R.drawable.storm);
            }else if (tempraturevalue>=35){
                alertImage.setBackgroundResource(R.drawable.sun);
            }else{
                alertImage.setBackgroundResource(R.drawable.cloud);
            }

            alertMessage.setText("");
            hello.setEnabled(true);
            close.setEnabled(true);

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MyDialog.dismiss();
                }
            });
    progressDialog.dismiss();
    MyDialog.show();

}
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (ParseException e){
            e.printStackTrace();
        }

       // weathertext.setText(temp + (char) 0x00B0);
    }
}