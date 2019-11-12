package com.thenikaran.guide.background;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class asynTask extends AsyncTask<String, Void, String> {
    OkHttpClient client;
    StringBuilder featurestr;
    Response response;
    private OnTaskCompleted taskCompleted;

    public asynTask(OnTaskCompleted activityContext){
        this.taskCompleted = activityContext;
    }

    @Override
    protected String doInBackground(String... Urls) {
        String getResponse = "";
        try {
            featurestr = new StringBuilder();
            HttpUrl.Builder urlBuilder = HttpUrl.parse(Urls[0]).newBuilder();
            String url = urlBuilder.build().toString();
            getResponse = post(url);

            Log.e("response", getResponse);
            Log.e("url", url);

        }
        catch (MalformedURLException e) {
            Log.e("MalformedURLException: " , e.getMessage());
        } catch (ProtocolException e) {
            Log.e("ProtocolException: " , e.getMessage());
        }  catch (NullPointerException e){
            e.printStackTrace();
        }  catch (IOException e) {
            e.printStackTrace();
        }
        return getResponse;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        taskCompleted.onTaskCompleted(s);
    }


    private String post(String url) throws IOException {

        try {

            client = new OkHttpClient.Builder().build();

            Request request = new Request.Builder()
                    .url(url)
                    .get().build();
            // .addHeader("cache-control", "no-cache")
            response = client.newCall(request).execute();
            Log.e("request ", String.valueOf(request));
            Log.e("Respcode is ", String.valueOf(response.code()));

        }  catch (MalformedURLException e) {
            Log.e("MalformedURLException: " , e.getMessage());
        } catch (ProtocolException e) {
            Log.e("ProtocolException: " , e.getMessage());
        }  catch (NullPointerException e){
            e.printStackTrace();
        }  catch (IOException e) {
            e.printStackTrace();
        }
        return response.body().string();
    }

    public interface OnTaskCompleted {
        void onTaskCompleted(String response);
    }

}
