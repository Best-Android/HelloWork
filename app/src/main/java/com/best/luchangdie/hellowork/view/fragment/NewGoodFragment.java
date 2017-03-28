package com.best.luchangdie.hellowork.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.best.luchangdie.hellowork.R;
import com.best.luchangdie.hellowork.model.application.I;
import com.best.luchangdie.hellowork.model.bean.Results;
import com.best.luchangdie.hellowork.presenter.INewGoodPresenter;
import com.best.luchangdie.hellowork.presenter.NewGoodPresenter;
import com.best.luchangdie.hellowork.view.adapter.PicturesAdapter;
import com.best.luchangdie.hellowork.view.base.BaseFragment;
import com.best.luchangdie.hellowork.view.iviews.INewGoodView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.best.luchangdie.hellowork.model.application.I.ACTION_PULL_DOWN;
import static com.best.luchangdie.hellowork.model.application.I.ACTION_PULL_UP;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewGoodFragment extends BaseFragment implements INewGoodView{
    @BindView(R.id.tvRefreshHint)
    TextView tvRefreshHint;
    @BindView(R.id.rvPicture)
    RecyclerView rvPicture;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<Results> mPicturesList;
    PicturesAdapter mAdapter;
    LinearLayoutManager mLayoutManager;
    int mPageId = 1;
    Unbinder unbinder;

    INewGoodPresenter mPresenter;


    public NewGoodFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_good, container, false);
        unbinder = ButterKnife.bind(this, view);
        mPresenter = new NewGoodPresenter(this);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void initView() {
        mPicturesList = new ArrayList<>();
        mAdapter = new PicturesAdapter(getContext(), mPicturesList);
        rvPicture.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getContext());
        rvPicture.setLayoutManager(mLayoutManager);
    }

    @Override
    public void initData() {
        mPresenter.downloadData(I.ACTION_DOWNLOAD,1);
    }

    @Override
    public void setListener() {
        setPullDownListener();
        setPullUpListener();
    }

    private void setPullUpListener() {
        rvPicture.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastPosition;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                lastPosition = mLayoutManager.findLastVisibleItemPosition();
                if (lastPosition >= mAdapter.getItemCount() - 1 && newState == RecyclerView.SCROLL_STATE_IDLE && mAdapter.isMore()) {
                    mPageId++;
                    mPresenter.downloadData(ACTION_PULL_UP,mPageId);
                }
                if (newState != RecyclerView.SCROLL_STATE_DRAGGING) {
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastPosition = mLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    private void setPullDownListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setEnabled(true);
                swipeRefreshLayout.setRefreshing(true);
                tvRefreshHint.setVisibility(View.VISIBLE);
                mPageId = 1;
                mPresenter.downloadData(ACTION_PULL_DOWN,mPageId);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void setMore(boolean more) {
        mAdapter.setMore(more);
    }

    @Override
    public void addData(List list) {
        mAdapter.addData(new ArrayList<>(list));
    }

    @Override
    public void initData(List list) {
        mAdapter.initData(new ArrayList<>(list));
    }

}
