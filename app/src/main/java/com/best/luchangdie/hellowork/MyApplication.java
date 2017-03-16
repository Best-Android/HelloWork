package com.best.luchangdie.hellowork;

import android.app.Application;
import android.util.Log;

import com.best.luchangdie.hellowork.bean.PicturesData;
import com.best.luchangdie.hellowork.bean.Results;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2017/3/16.
 */

public class MyApplication extends Application {
    public static final String TAG = "MyApplication";
    String url = "http://gank.io/api/data/Android/10/1";
    @Override
    public void onCreate() {
        super.onCreate();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .build();
        OkHttpUtils.initClient(okHttpClient);
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                parseJson(response);
            }
        });
    }
    public void parseJson(String json) {
        PicturesData picturesData = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            boolean error = jsonObject.getBoolean("error");
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            List<Results> list = new ArrayList<Results>();
            List<String> imagesList = new ArrayList<String>();
            for (int i = 0;i < jsonArray.length();i++) {
                JSONObject jsonResults = (JSONObject) jsonArray.get(i);
                String _id = jsonResults.getString("_id");
                String createdAt = jsonResults.getString("createdAt");
                String desc = jsonResults.getString("desc");
                if (jsonResults.has("images")) {
                    JSONArray imagesArray = jsonResults.getJSONArray("images");
                    for (int j = 0; j < imagesArray.length(); j++) {
                        String images = (String) imagesArray.get(j);
                        //Log.d(TAG, "onResponse: " + images);
                        imagesList.add(images);
                    }
                }
                String publishedAt = jsonResults.getString("publishedAt");
                String source = jsonResults.getString("source");
                String type = jsonResults.getString("type");
                String url = jsonResults.getString("url");
                Boolean used = jsonResults.getBoolean("used");
                String who = jsonResults.getString("who");
                Results results = new Results(_id,createdAt,desc, imagesList ,publishedAt,source,type,url,used,who);
                list.add(results);
            }
            //Log.d(TAG,list.toString());
            picturesData = new PicturesData();
            picturesData.setError(error);
            picturesData.setResults(list);
            Log.d(TAG, picturesData.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
