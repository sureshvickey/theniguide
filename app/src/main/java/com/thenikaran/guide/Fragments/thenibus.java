package com.thenikaran.guide.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.thenikaran.guide.MainActivity;
import com.thenikaran.guide.MapViewActivity;
import com.thenikaran.guide.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class thenibus extends Fragment {
    View view;
    TextView textView;
    Spinner spin1;
    Spinner spin2;
    Context context;
    Button gobtn;
    TextView bus_source,destination,via,type,timing,kms;
    public static Dialog SearchDialog;
    Button get,close;
    ImageView alertImage;
    public String alertName="";
    public String alertimg="";
    public String alerttitle="";
    public String alertmsg="";
    TextView alertMessage;
    SupportMapFragment mapFragment;
    public static String latitude="";
    public static String longitude="";
    FloatingActionButton floatingActionButton;
    Home home;
    private boolean _hasLoadedOnce= false;
    RelativeLayout busLayout;
    LinearLayout resultlayout;
    Button navigate,viewMap;
   public static String busdestination="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_thenibus, container, false);
        context=getContext();
        home=new Home();
        busLayout=(RelativeLayout) view.findViewById(R.id.buslayout);
        resultlayout=(LinearLayout) view.findViewById(R.id.resultlayout);
        textView=(TextView) view.findViewById(R.id.text);
        bus_source=(TextView) view.findViewById(R.id.source);
        destination=(TextView) view.findViewById(R.id.destination);
        kms=(TextView) view.findViewById(R.id.kms);
        via=(TextView) view.findViewById(R.id.via);
        type=(TextView) view.findViewById(R.id.type);
        timing=(TextView) view.findViewById(R.id.timing);
        floatingActionButton=(FloatingActionButton) view.findViewById(R.id.fab);
        navigate=(Button) view.findViewById(R.id.navigate);
        viewMap=(Button) view.findViewById(R.id.viewMap);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                if (SearchDialog.isShowing()){
                    SearchDialog.dismiss();
                }
                MyCustomAlertDialog();
            }            catch(Exception e){
                e.printStackTrace();
            }
            }
        });

        navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!longitude.isEmpty()&&!latitude.isEmpty()){
                String geoUri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude+" (" + busdestination + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                startActivity(intent);
                }
            }
        });

        viewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!longitude.isEmpty()&&!latitude.isEmpty()){
                    startActivity(new Intent(getActivity(), MapViewActivity.class).putExtra("lat", latitude).putExtra("long",longitude));
                }
            }
        });
        return view;
    }


    public void MyCustomAlertDialog(){
        SearchDialog = new Dialog(getActivity());
        SearchDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        SearchDialog.setContentView(R.layout.busdialog);
        SearchDialog.setTitle(alerttitle);

        get = (Button)SearchDialog.findViewById(R.id.get);
        close = (Button)SearchDialog.findViewById(R.id.close);
        spin1 = (Spinner) SearchDialog.findViewById(R.id.simpleSpinner1);
        spin2 = (Spinner) SearchDialog.findViewById(R.id.simpleSpinner2);
        alertImage=(ImageView) SearchDialog.findViewById(R.id.alertimg);
//        Picasso.with(MainActivity.this).load(alertimg)
//                .placeholder(R.drawable.app_icon)
//                .error(R.drawable.error_image).into(alertImage);


        get.setEnabled(true);
        close.setEnabled(true);
        final String[] source=new String[]{"Theni"};
        ArrayAdapter aa = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,source);
        ArrayAdapter<CharSequence> adp3 = ArrayAdapter.createFromResource(getActivity(),R.array.dest_list, android.R.layout.simple_list_item_1);
        spin2.setAdapter(adp3);
        spin1.setAdapter(aa);
        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value=spin2.getSelectedItem().toString();
                List<String> questions = new ArrayList();
                // Reading json file from assets folder
                StringBuffer sb = new StringBuffer();
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new InputStreamReader(getActivity().getAssets().open("theni.json")));
                    String temp;
                    while ((temp = br.readLine()) != null)
                        sb.append(temp);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        br.close(); // stop reading
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                String myjsonstring = sb.toString();

                // Try to parse JSON
                try {
                    // Creating JSONObject from String
                    JSONObject jsonObjMain = new JSONObject(myjsonstring);

                    // Creating JSONArray from JSONObject
                    JSONArray jsonArray = jsonObjMain.getJSONArray("bus_list");

                    // JSONArray has x JSONObject
                    for (int i = 0; i < jsonArray.length(); i++) {

                        // Creating JSONObject from JSONArray
                        JSONObject jsonObj = jsonArray.getJSONObject(i);

                        // Getting data from individual JSONObject
                        String bus_id = jsonObj.getString("id");
                        String bus_from = jsonObj.getString("bus_from");
                        String bus_to = jsonObj.getString("bus_to");
                        String bus_via = jsonObj.getString("bus_via");
                        String bus_type = jsonObj.getString("bus_type");
                        String bus_time = jsonObj.getString("bus_time");
                        String latit = jsonObj.getString("latitude");
                        String longit = jsonObj.getString("longitude");
                        if (bus_to.equals(value)) {
                            // questions.add(bus_from + " To " + bus_to + "\n" + bus_via + "\n" + bus_type + "\n" + bus_time);
                            bus_source.setText(bus_from);
                            destination.setText(bus_to);
                            via.setText(bus_via);
                            type.setText(bus_type);
                            timing.setText(bus_time);
                            latitude=latit;
                            longitude=longit;
                            busdestination=bus_to;
                            integrateMap();
                            busLayout.setVisibility(View.GONE);
                            resultlayout.setVisibility(View.VISIBLE);
                        }

                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

//                if (questions.size() > 0) {
//                    StringBuilder stringBuilder=new StringBuilder();
//                    for (int i = 0; i < questions.size(); i++) {
//                        // Use the first question
//                        stringBuilder.append(questions.get(i)+"\n");
//                    }
//                    textView.setText(stringBuilder.toString());
//                }
                SearchDialog.cancel();

            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchDialog.cancel();
            }
        });

        SearchDialog.show();
    }
    private void integrateMap() {
        mapFragment = new SupportMapFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.framelayout, mapFragment).commit();
        Bundle args = new Bundle();
        args.putString("longitude",latitude);
        args.putString("latitude", longitude);
        mapFragment.setArguments(args);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

              //  LatLng sourcelocation = new LatLng(Double.parseDouble("10.0104"), Double.parseDouble("77.4768"));
                LatLng destlocation = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
               // googleMap.addMarker(new MarkerOptions().position(sourcelocation).title("Marker Title").snippet("Marker Description"));
                googleMap.addMarker(new MarkerOptions().position(destlocation).title("Marker Title").snippet("Marker Description"));
//                PolylineOptions polylineOptions = new PolylineOptions();
//                polylineOptions.add(new LatLng(Double.parseDouble("10.0104"), Double.parseDouble("77.4768")))
//                        .add(new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude)));
//                googleMap.addPolyline(polylineOptions);
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(destlocation).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                try {
                    float distanceInMeters;
                    Location loc1 = new Location("");
                    loc1.setLatitude(Double.parseDouble("10.0104"));
                    loc1.setLongitude(Double.parseDouble("77.4768"));

                    Location loc2 = new Location("");
                    loc2.setLatitude(Double.parseDouble(latitude));
                    loc2.setLongitude(Double.parseDouble(longitude));

                    distanceInMeters = loc1.distanceTo(loc2);
                    Log.d("distance", distanceInMeters + "");
                    distanceInMeters = distanceInMeters / 1000;
                    kms.setText(new DecimalFormat("##.#").format(distanceInMeters) + " km away");
                } catch (Exception e) {
                    kms.setText("Not Found");
                }
            }
        });

    }
    @Override
    public void setUserVisibleHint(boolean isFragmentVisible_) {
        super.setUserVisibleHint(true);


        if (this.isVisible()) {
            // we check that the fragment is becoming visible
            if (isFragmentVisible_) {
                MyCustomAlertDialog();
            }
        }
    }
}
