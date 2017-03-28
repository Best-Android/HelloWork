package com.best.luchangdie.hellowork.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.best.luchangdie.hellowork.R;
import com.best.luchangdie.hellowork.model.application.I;
import com.best.luchangdie.hellowork.model.bean.Results;
import com.best.luchangdie.hellowork.view.activity.WebViewActivity;
import com.best.luchangdie.hellowork.view.viewholder.FooterViewHolder;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import static com.best.luchangdie.hellowork.model.application.I.TYPE_FOOTER;
import static com.best.luchangdie.hellowork.model.application.I.TYPE_ITEM;

/**
 * Created by Administrator on 2017/3/27.
 */

public class PicturesAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<Results> picturesList;
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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = null;
        switch (viewType) {
            case I.TYPE_FOOTER:
                layout = inflater.inflate(R.layout.item_footer, parent, false);
                holder = new FooterViewHolder(layout);
                break;
            case I.TYPE_ITEM:
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
        picturesViewHolder.itemPicture.setTag(picturesData);
        picturesViewHolder.itemPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Results result = (Results) v.getTag();
                String url = result.getUrl();
                context.startActivity(new Intent(context, WebViewActivity.class).putExtra("url", url));
            }
        });
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

    class PicturesViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout itemPicture;
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
            itemPicture = (RelativeLayout) itemView.findViewById(R.id.itemPicture);
        }
    }
}
