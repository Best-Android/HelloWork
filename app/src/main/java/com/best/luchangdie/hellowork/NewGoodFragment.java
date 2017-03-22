package com.best.luchangdie.hellowork;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.best.luchangdie.hellowork.bean.PicturesData;
import com.best.luchangdie.hellowork.bean.Results;
import com.bumptech.glide.Glide;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewGoodFragment extends Fragment {
    final static int ACTION_DOWNLOAD = 0;
    final static int ACTION_PULL_DOWN = 1;
    final static int ACTION_PULL_UP = 2;

    private final static String ROOT_URL = "http://gank.io/api/data/Android/10/";
    private static final String TAG = "lcd";
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
    int mNewState;
    Unbinder unbinder;


    public NewGoodFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_good, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView(view);
        setListener();
        return view;
    }

    private void setListener() {
        setPullDownListener();
        setPullUpListener();
    }

    private void setPullUpListener() {
        rvPicture.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastPosition;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mNewState = newState;
                lastPosition = mLayoutManager.findLastVisibleItemPosition();
                if (lastPosition >= mAdapter.getItemCount() - 1 && newState == RecyclerView.SCROLL_STATE_IDLE && mAdapter.isMore()) {
                    mPageId++;
                    downloadData(ACTION_PULL_UP,mPageId);
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
                downloadData(ACTION_PULL_DOWN,mPageId);
            }
        });
    }

    private void initView(View view) {
        mPicturesList = new ArrayList<>();
        mAdapter = new PicturesAdapter(view.getContext(), mPicturesList);
        rvPicture.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        rvPicture.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        downloadData(ACTION_DOWNLOAD,1);
    }

    private void downloadData(final int actionDown , int pageId) {
        Log.d(TAG, "downloadData: " + actionDown + ", " + pageId);
        String url = ROOT_URL + pageId;
        Log.d(TAG, "downloadData: " + url);
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                swipeRefreshLayout.setRefreshing(false);
                Log.d(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                PicturesData picturesData = PicturesData.parseJson(response);
                List<Results> list = picturesData.getResults();
                mAdapter.setMore(list != null && list.size() > 0);
                if (!mAdapter.isMore()) {
                    if (actionDown == ACTION_PULL_UP) {
                        mAdapter.setFooter("没有更多数据");
                    }
                    return;
                }
                switch (actionDown) {
                    case ACTION_DOWNLOAD:
                        mAdapter.initData(list);
                        mAdapter.setFooter("加载更多数据");
                        break;
                    case ACTION_PULL_DOWN:
                        mAdapter.initData(list);
                        mAdapter.setFooter("加载更多数据");
                        swipeRefreshLayout.setRefreshing(false);
                        tvRefreshHint.setVisibility(View.GONE);
                        break;
                    case ACTION_PULL_UP:
                        mAdapter.addData(list);
                        break;
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    class PicturesViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvDesc;
        TextView tvWho;
        TextView tvCreatedAt;

        public PicturesViewHolder(View itemView) {
            super(itemView);
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
            tvDesc = (TextView) itemView.findViewById(R.id.tvDesc);
            tvWho = (TextView) itemView.findViewById(R.id.tvWho);
            tvCreatedAt = (TextView) itemView.findViewById(R.id.tvCreatedAt);
        }
    }
    class FooterViewHolder extends RecyclerView.ViewHolder {
        TextView tvFooter;

        public FooterViewHolder(View itemView) {
            super(itemView);
            tvFooter = (TextView) itemView.findViewById(R.id.tvFooter);
        }
    }

    class PicturesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        final static int TYPE_ITEM = 0;//代表图片item类型的布局
        final static int TYPE_FOOTER = 1;//代表页脚item类型的布局

        Context context;
        ArrayList<Results> picturesList;
        String footerText;
        boolean isMore;

        public PicturesAdapter(Context context, ArrayList<Results> picturesList) {
            this.context = context;
            this.picturesList = picturesList;
        }

        public boolean isMore() {
            return isMore;
        }

        public void setMore(boolean more) {
            isMore = more;
        }

        public void setFooter(String footerText) {
            this.footerText = footerText;
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder holder = null;
            LayoutInflater inflater = LayoutInflater.from(context);
            View layout = null;
            switch (viewType) {
                case TYPE_FOOTER:
                    layout = inflater.inflate(R.layout.item_footer, parent, false);
                    holder = new FooterViewHolder(layout);
                    break;
                case TYPE_ITEM:
                    layout = inflater.inflate(R.layout.item_picture, parent, false);
                    holder = new PicturesViewHolder(layout);
                    break;
            }
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (position == getItemCount() - 1) {
                return;
            }
            PicturesViewHolder picturesViewHolder = (PicturesViewHolder) holder;
            Results picturesData = picturesList.get(position);
            picturesViewHolder.tvDesc.setText(picturesData.getDesc());
            picturesViewHolder.tvWho.setText(picturesData.getWho());
            picturesViewHolder.tvCreatedAt.setText(picturesData.getCreatedAt());
            if (picturesData.getImages().size() > 0) {
                Glide.with(context).load(picturesData.getImages().get(0)).into(((PicturesViewHolder) holder).ivImage);
            }
        }

        @Override
        public int getItemCount() {
            return picturesList == null ? 0 : picturesList.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == getItemCount() - 1) {
                return TYPE_FOOTER;
            } else {
                return TYPE_ITEM;
            }
        }

        public void initData(List<Results> list) {
            if (picturesList != null) {
                picturesList.clear();
            }
            picturesList.addAll(list);
            notifyDataSetChanged();
        }

        public void addData(List<Results> list) {
            this.picturesList.addAll(list);
            notifyDataSetChanged();
        }
    }

}
