package com.best.luchangdie.hellowork.bean;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.best.luchangdie.hellowork.MyApplication.TAG;

/**
 * Created by Administrator on 2017/3/13.
 */

public class PicturesData {
    private boolean error;
    private List<Results> results;

    public PicturesData() {
    }

    public PicturesData(boolean error, List<Results> results) {
        this.error = error;
        this.results = results;
    }

    public static PicturesData parseJson(String json) {
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
        return picturesData;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<Results> getResults() {
        return results;
    }

    public void setResults(List<Results> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "PicturesData{" +
                "error=" + error +
                ", results=" + results +
                '}';
    }
}
