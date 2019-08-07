package com.genie97.myapplication.view;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.genie97.myapplication.R;
import com.genie97.myapplication.model.ImageSearchApplication;
import com.genie97.myapplication.viewmodel.MainViewModel;

import java.text.SimpleDateFormat;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private List<String> mList;
    private List<String> thumbNailList;
    private List<String> siteNameList;
    private List<String> collectionList;
    private List<String> docUrlList;
    private List<String> dateTimeList;
    private List<Integer> widthList;
    private List<Integer> heightList;

    public RecyclerViewAdapter(List<String> mList, List<String> thumbNailList,
                               List<String> collectionList, List<String> siteNameList,
                               List<String> docUrlList, List<String> dateTimeList, List<Integer> widthList, List<Integer> heightList) {
        this.mList = mList;
        this.collectionList = collectionList;
        this.dateTimeList = dateTimeList;
        this.docUrlList = docUrlList;
        this.thumbNailList = thumbNailList;
        this.siteNameList = siteNameList;
        this.widthList = widthList;
        this.heightList = heightList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerViewHolder viewHolder = null;
        View layoutView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recyclerview_image, parent, false);

        viewHolder = new RecyclerViewHolder(layoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
        // 로딩바
        CircularProgressDrawable cpd = new CircularProgressDrawable(ImageSearchApplication.getAppContext());
        cpd.setStrokeWidth(5f);
        cpd.setCenterRadius(30f);
        cpd.start();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(ImageSearchApplication.getAppContext(), "Image URL: "+mList.get(position), Toast.LENGTH_SHORT).show();
                MainViewModel.viewClick(mList.get(position),widthList.get(position), heightList.get(position),docUrlList.get(position));
            }
        });

        //글라이드 오류 이미지 변경하기
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .placeholder(R.mipmap.ic_launcher_imageloading_round)
                .placeholder(cpd)
                .error(R.mipmap.ic_launcher_imageloading_round);

        Log.d("리싸이클러뷰", thumbNailList.get(position));
        Glide.with(ImageSearchApplication.getAppContext())
                .load(thumbNailList.get(position))
                .apply(options)
                .into(holder.thumbNail);
        String localTime = convertUtcToLocal(dateTimeList.get(position).toString());
        holder.dateTime.setText("문서작성시간: " + localTime);
        //holder.dateTime.setText("문서작성시간: " + dateTimeList.get(position).toString()); ISO8601형태
        holder.docUrl.setText("문서URL: " + docUrlList.get(position).toString());
        holder.siteName.setText("출처: " + siteNameList.get(position).toString());
        holder.collection.setText("분류: " + collectionList.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return this.thumbNailList.size();
    }

    public void clear() {
        int size = this.thumbNailList.size();
        this.thumbNailList.clear();
        notifyItemRangeRemoved(0, size);
    }

    private static String convertUtcToLocal(String utcTime) {
        String localTime = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-DD hh:mm:ss");
        try {
            localTime += utcTime.substring(0, 10);
            localTime += " ";
            localTime += utcTime.substring(11, 19);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localTime;
    }
}