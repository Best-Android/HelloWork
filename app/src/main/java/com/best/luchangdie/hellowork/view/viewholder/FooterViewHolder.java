package com.best.luchangdie.hellowork.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.best.luchangdie.hellowork.R;

/**
 * Created by Administrator on 2017/3/28.
 */

public class FooterViewHolder extends RecyclerView.ViewHolder{
    TextView tvFooter;
    public FooterViewHolder(View itemView) {
        super(itemView);
        tvFooter = (TextView) itemView.findViewById(R.id.tvFooter);
    }
}
