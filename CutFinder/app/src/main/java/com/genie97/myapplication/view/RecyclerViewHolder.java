package com.genie97.myapplication.view;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.genie97.myapplication.R;

import org.w3c.dom.Text;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    public ImageView thumbNail;
    public TextView siteName;
    public TextView collection;
    public TextView docUrl;
    public TextView dateTime;


    public RecyclerViewHolder(View itemView) {
        super(itemView);

        thumbNail = (ImageView) itemView.findViewById(R.id.thumbNail);
        siteName = (TextView) itemView.findViewById(R.id.siteName);
        collection = (TextView) itemView.findViewById(R.id.collection);
        docUrl = (TextView) itemView.findViewById(R.id.docUrl);
        dateTime = (TextView) itemView.findViewById(R.id.dateTime);
    }
}