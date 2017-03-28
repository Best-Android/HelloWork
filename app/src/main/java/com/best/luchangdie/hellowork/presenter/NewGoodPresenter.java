package com.best.luchangdie.hellowork.presenter;

import com.best.luchangdie.hellowork.model.application.I;
import com.best.luchangdie.hellowork.model.bean.PicturesData;
import com.best.luchangdie.hellowork.model.bean.Results;
import com.best.luchangdie.hellowork.view.iviews.INewGoodView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/3/27.
 */

public class NewGoodPresenter implements INewGoodPresenter {
    private static String TAG = "NewGoodPresenter";
    INewGoodView mView;

    public NewGoodPresenter(INewGoodView mView) {
        this.mView = mView;
    }


    @Override
    public void downloadData(final int actionDown, int pageId) {
        String url = I.ROOT_URL + pageId;
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(String response, int id) {
                PicturesData picturesData = PicturesData.parseJson(response);
                List<Results> list = picturesData.getResults();
                mView.setMore(list != null && list.size() > 0);
                switch (actionDown) {
                    case I.ACTION_DOWNLOAD:
                        mView.initData(list);
                        break;
                    case I.ACTION_PULL_DOWN:
                        mView.initData(list);
                        break;
                    case I.ACTION_PULL_UP:
                        mView.addData(list);
                        break;
                }
            }
        });
    }
}
