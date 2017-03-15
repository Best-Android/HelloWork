package com.best.luchangdie.hellowork;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.OkHttpClient;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String TAG = "MainActivity";

    @BindView(R.id.rb_new_good)
    RadioButton rbNewGood;
    @BindView(R.id.rb_boutique)
    RadioButton rbBoutique;
    @BindView(R.id.rb_cart)
    RadioButton rbCart;
    @BindView(R.id.rb_personal)
    RadioButton rbPersonal;
    @BindView(R.id.layout_fragment)
    LinearLayout layoutFragment;

    Fragment newGoodFragment = new NewGoodFragment();
    Fragment boutiqueFragment = new BoutiqueFragment();
    Fragment cartFragment = new CartFragment();
    Fragment personalFragment = new PersonalFragment();
    Fragment mTempFragment = newGoodFragment;

    String url = "http://gank.io/api/data/Android/10/1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        rbNewGood.setChecked(true);
        rbNewGood.setOnClickListener(this);
        rbBoutique.setOnClickListener(this);
        rbCart.setOnClickListener(this);
        rbPersonal.setOnClickListener(this);
        getSupportFragmentManager().beginTransaction().add(R.id.layout_fragment,newGoodFragment).commit();
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
                PicturesData picturesData = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
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
                                Log.d(TAG, "onResponse: " + images);
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
                    Log.d(TAG,list.toString());
                    picturesData = new PicturesData();
                    picturesData.setError(error);
                    picturesData.setResults(list);
                    Log.d(TAG, picturesData.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void switchContent (Fragment fragment) {
        if (fragment != mTempFragment) {
            if (!fragment.isAdded()) {
                getSupportFragmentManager().beginTransaction().hide(mTempFragment).add(R.id.layout_fragment,fragment).commit();
            } else {
                getSupportFragmentManager().beginTransaction().hide(mTempFragment).show(fragment).commit();
            }
            mTempFragment = fragment;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_new_good:
                switchContent(newGoodFragment);
                break;
            case R.id.rb_boutique:
                switchContent(boutiqueFragment);
                break;
            case R.id.rb_cart:
                switchContent(cartFragment);
                break;
            case R.id.rb_personal:
                switchContent(personalFragment);
                break;
        }
    }

}
