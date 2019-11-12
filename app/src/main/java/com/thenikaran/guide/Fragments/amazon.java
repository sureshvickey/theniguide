package com.thenikaran.guide.Fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.thenikaran.guide.DetectConnection;
import com.thenikaran.guide.GetSignedUrl.SignedRequestsHelper;
import com.thenikaran.guide.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class amazon extends Fragment {
    View view;
    @Bind(R.id.offerRecyclerView)
    RecyclerView offerRecyclerView;
    @BindString(R.string.app_name)
    String app_name;
    @Bind(R.id.moreresult)
    Button button;
    @Bind(R.id.ScrollView)
    NestedScrollView scroller;
    OkHttpClient client;
    StringBuilder featurestr;
    Response response;
    //@Bind(R.id.searchLayoutmain1)
    //RelativeLayout relativeLayout;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressDialog mProgressDialog;
    ArrayList<HashMap<String, String>> arraylist;

    public static String signurl;
    public static String URL = "DetailPageURL";
    public static String NAME = "Title";
    public static String IMG = "URL";
    public static String BRAND = "Brand";
    public static String PRICE = "FormattedPrice";
    public static String OFFPRICE;
    public static String OFFURL = "MoreOffersUrl";
    String OFFprice = "FormattedPrice";
    NodeList nodelist, nodelist1, nodelist2, nodelist3, nodeList4;
    String moreresult;
    static int y;
    public String keyword="";

    String versionName="";
    SignedRequestsHelper helper;
    public String ACCESS_KEY_ID = "";
    public String SECRET_KEY = "";
    public String ENDPOINT = "webservices.amazon.in";
    public static String requestUrl = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_amazon, container, false);
        ButterKnife.bind(this, view);
        button.setVisibility(View.GONE);
        ViewCompat.setNestedScrollingEnabled(offerRecyclerView, false);
        try
        {
//			HttpUrl.Builder movurl = HttpUrl.parse("http://thenikaran.co.in/privatedb/amazonkey.php").newBuilder();
//			String movstring = movurl.build().toString();
//			String movres = post(movstring);
//			JSONArray movref = new JSONArray(movres);
//			for(int i = 0; i<movref.length();i++)
//			{
//				JSONObject name = movref.getJSONObject(i);
//				if (name.getString("name").equals("keyword")) {
//					keyword = name.getString("secret_key");
//				}
//			}
            // Check whether internet is on or off and Start class when
            // FragmentAffiliate loaded
            if (getArguments()!=null){
                keyword=getArguments().getString("keyword");
            }
            if (DetectConnection.checkInternetConnection(getActivity()))
            {
                new GetDataAsync().execute();
            }
            else
            {
                Toast.makeText(getActivity(), "Internet Not Available", Toast.LENGTH_SHORT).show();
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return view;
    }
    public class GetDataAsync extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String response="";
            try{
                if (keyword.equals("")){
                    HttpUrl.Builder movurl = HttpUrl.parse("http://thenikaran.co.in/privatedb/amazonkey.php").newBuilder();
                    String movstring = movurl.build().toString();
                    String movres = post(movstring);
                    JSONArray movref = new JSONArray(movres);
                    for(int i = 0; i<movref.length();i++)
                    {
                        JSONObject name = movref.getJSONObject(i);
                        if (name.getString("name").equals("keyword")) {
                            keyword = name.getString("secret_key");
                            Log.d("keyword",keyword);
                        }
                        if (name.getString("name").equals("affiliate")) {
                            SECRET_KEY = name.getString("secret_key");
                            ACCESS_KEY_ID = name.getString("access_key");
                            Log.d("keyword",keyword);
                        }
                    }
                }else {
                    HttpUrl.Builder movurl = HttpUrl.parse("http://thenikaran.co.in/privatedb/amazonkey.php").newBuilder();
                    String movstring = movurl.build().toString();
                    String movres = post(movstring);
                    JSONArray movref = new JSONArray(movres);
                    for(int i = 0; i<movref.length();i++)
                    {
                        JSONObject name = movref.getJSONObject(i);
                        if (name.getString("name").equals("affiliate")) {
                            SECRET_KEY = name.getString("secret_key");
                            ACCESS_KEY_ID = name.getString("access_key");

                        }
                    }
                }
                helper = SignedRequestsHelper.getInstance(ENDPOINT, ACCESS_KEY_ID, SECRET_KEY);

                Map<String, String> params = new HashMap<String, String>();

                params.put("Service", "AWSECommerceService");
                params.put("Operation", "ItemSearch");
                params.put("AWSAccessKeyId", ACCESS_KEY_ID);
                params.put("AssociateTag", "thenikaran-21");
                params.put("SearchIndex", "All");
                if (keyword == null) {
                    params.put("Keywords", "iphone");
                } else {
                    params.put("Keywords", keyword);
                }
                params.put("ResponseGroup", "Images,ItemAttributes,Offers");

                requestUrl = helper.sign(params);

            }catch (JSONException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String string) {

            if (keyword!=null){
                // signurl = getSignedUrl.main(keyword);
                new AmazonXMLres().execute(requestUrl);}
            else {
                // signurl = getSignedUrl.main("apple");
                new AmazonXMLres().execute(requestUrl);
            }
        }
        private String post(String url) throws IOException {

            try {

                client = new OkHttpClient.Builder().readTimeout(190000, TimeUnit.MILLISECONDS).writeTimeout(190000, TimeUnit.MILLISECONDS)
                        .connectTimeout(190000, TimeUnit.MILLISECONDS).build();

                Request request = new Request.Builder()
                        .url(url)
                        .get().build();
                // .addHeader("cache-control", "no-cache")
                response = client.newCall(request).execute();
                Log.i("request ", String.valueOf(request));
                Log.i("Respcode is ", String.valueOf(response.code()));

            }catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response.body().string();
        }
    }
    private class AmazonXMLres extends AsyncTask<String, Void, Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getActivity());
            // mProgressDialog.setTitle("Parsing data from amazon");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }


        @Override
        protected Void doInBackground(String... Url)
        {
            try
            {
                arraylist = new ArrayList<HashMap<String, String>>();
                arraylist.clear();
                java.net.URL url = new URL(Url[0]);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                // Download the XML file
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();
                // Locate the Tag Name
                nodelist = doc.getElementsByTagName("Item");
                nodelist1 = doc.getElementsByTagName("ImageSets");
                nodelist2 = doc.getElementsByTagName("ItemAttributes");
                nodelist3 = doc.getElementsByTagName("Offers");
                nodeList4 = doc.getElementsByTagName("Items");
                for (int temp = 0; temp < nodelist.getLength(); temp++)
                {
                    Node nNode = nodelist.item(temp);
                    Node nNode1 = nodelist1.item(temp).getLastChild().getLastChild();
                    Node nNode2 = nodelist2.item(temp);
                    Node nNode3 = nodelist3.item(temp);
                    Log.i("len ", String.valueOf(temp));
                    HashMap<String, String> map = new HashMap<String, String>();
                    if (nNode.getNodeType() == Node.ELEMENT_NODE)
                    {
                        Element eElement = (Element) nNode;
                        Element eElement1 = (Element) nNode1;
                        Element eElement2 = (Element) nNode2;
                        Element eElement3 = (Element) nNode3;
                        map.put(NAME, getNode(NAME, eElement));
                        map.put(URL, getNode(URL, eElement));
                        map.put(IMG, getNode(IMG, eElement1));
                        map.put(BRAND, getNode(BRAND, eElement2));
                        map.put(PRICE, getNode(PRICE, eElement2));
                        map.put(OFFURL, getNode(OFFURL, eElement3));
                        map.put(OFFPRICE, getNode(OFFprice, eElement3));
                        arraylist.add(map);
                    }
                }
                for (int temp1 = 0; temp1 < nodeList4.getLength(); temp1++)
                {
                    Node nNode = nodeList4.item(temp1);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE)
                    {
                        Element eElement = (Element) nNode;
                        moreresult = String.valueOf(getNode("MoreSearchResultsUrl", eElement));
                        Log.d("moreresult", moreresult);
                    }
                }

            }
            catch (Exception e)
            {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void args)
        {
            if (mProgressDialog != null)
                mProgressDialog.dismiss();
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
            offerRecyclerView.setLayoutManager(gridLayoutManager);
            AffiliateCardAdapter affiliateCardAdapter = new AffiliateCardAdapter(getActivity(), arraylist);
            offerRecyclerView.setAdapter(affiliateCardAdapter); // set the Adapter to RecyclerView
            Log.d("secretkey",SECRET_KEY+ACCESS_KEY_ID);
            if (moreresult == null)
            {
                button.setVisibility(View.GONE);
            }
            else
            {
                button.setVisibility(View.VISIBLE);
                button.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, Uri.parse(moreresult));
                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(goToMarket);
                    }
                });
            }
        }


        private String getNode(String sTag, Element eElement)
        {
            NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
            Node nValue = nlList.item(0);
            return nValue.getNodeValue();
        }
    }


    public class AffiliateCardAdapter extends RecyclerView.Adapter<AffiliateCardAdapter.ProductViewHolder>
    {
        private ArrayList<HashMap<String, String>> arraylist;
        private Context context;
        private HashMap<String, String> resultp = new HashMap<String, String>();
        private String pos;
        // private static final String BACK_STACK_ROOT_TAG = "root_fragment";
        private amazonDetail myObj;


        public AffiliateCardAdapter(Context context, ArrayList<HashMap<String, String>> arraylist)
        {
            this.context = context;
            this.arraylist = arraylist;
        }


        @NonNull
        @Override
        public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_affiliate, parent, false);
            // set the view's size, margins, paddings and layout parameters
            ProductViewHolder vh = new ProductViewHolder(v); // pass the view to View Holder
            return vh;
        }


        @Override
        public void onBindViewHolder(@NonNull ProductViewHolder holder,
                                     @SuppressLint("RecyclerView") final int position)
        {
            // resultp.clear();
            resultp = arraylist.get(position);
            String price = resultp.get(amazon.OFFPRICE);
            String orgprice = resultp.get(amazon.PRICE);
            price = price.replace("INR", "");
            orgprice = orgprice.replace("INR", "");
            holder.title.setText(resultp.get(amazon.NAME));
            holder.brand.setText(resultp.get(amazon.BRAND));
            holder.price.setText(price);
            holder.orgprice.setText(orgprice);
            holder.orgprice.setPaintFlags(holder.orgprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            Picasso.with(context).load(resultp.get(amazon.IMG)).into(holder.proimage);
            holder.cardView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    resultp = arraylist.get(position);
                    pos = String.valueOf(position);
                    myObj = new amazonDetail();
                    Bundle bundle = new Bundle();
                    bundle.putString("title", resultp.get(amazon.NAME));
                    bundle.putString("offprice", resultp.get(amazon.OFFPRICE));
                    bundle.putString("orgprice", resultp.get(amazon.PRICE));
                    bundle.putString("image", resultp.get(amazon.IMG));
                    bundle.putString("offurl", resultp.get(amazon.OFFURL));
                    bundle.putString("pos", pos);
                    bundle.putString("brand", resultp.get(amazon.BRAND));
                    bundle.putString("redurl", resultp.get(amazon.URL));
                    bundle.putString("signedurl", amazon.requestUrl);
                    myObj.setArguments(bundle);
                    Log.d("bundle", String.valueOf(myObj.getArguments()));



                    Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.frameLayout);
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    Fragment fra = new amazonDetail();
                    fra.setArguments(bundle);
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    // transaction.replace(R.id.fragment_container, fra);
                    transaction.add(R.id.frameLayout, fra, "fdet");
                    transaction.hide(currentFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();


//					((FragmentActivity) context).getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, myObj)
//							.hide(new FragmentAffiliate()).addToBackStack(null).commit();
                    // AffiliateMain.toolbar.setTitle(resultp.get(FragmentAffiliate.NAME));
                    // Log.d("brand",resultp.get(FragmentAffiliate.BRAND));
                }
            });
        }


        @Override
        public int getItemCount()
        {
            return arraylist.size();
        }


        class ProductViewHolder extends RecyclerView.ViewHolder
        {
            TextView title, brand, price, offurl, orgprice;
            ImageView proimage;
            CardView cardView;


            ProductViewHolder(View itemView)
            {
                super(itemView);
                title = itemView.findViewById(R.id.productName1);
                brand = itemView.findViewById(R.id.brand1);
                price = itemView.findViewById(R.id.price1);
                offurl = itemView.findViewById(R.id.offurl);
                orgprice = itemView.findViewById(R.id.orgprice1);
                proimage = itemView.findViewById(R.id.productImage1);
                cardView = itemView.findViewById(R.id.procardview);
            }
        }
    }
}
