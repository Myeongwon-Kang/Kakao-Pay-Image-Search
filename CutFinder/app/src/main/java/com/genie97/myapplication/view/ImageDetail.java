package com.genie97.myapplication.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.genie97.myapplication.R;
import com.genie97.myapplication.model.ImageSearchApplication;
import com.genie97.myapplication.viewmodel.MainViewModel;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;

public class ImageDetail extends AppCompatActivity {
    private ImageView photoView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        //toolbar - 이름
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("임효진");
        Intent intent = getIntent();
        String img_url = intent.getStringExtra("ImageUrl");
        String doc_url = intent.getStringExtra("DocUrl");
        int width = intent.getIntExtra("width", 200);
        int height = intent.getIntExtra("height", 300);

        photoView = (ImageView) findViewById(R.id.photoView);
        imageView = (ImageView) findViewById(R.id.imageView);

        CircularProgressDrawable cpd = new CircularProgressDrawable(ImageSearchApplication.getAppContext());
        cpd.setStrokeWidth(5f);
        cpd.setCenterRadius(30f);
        cpd.start();

        RequestOptions options = new RequestOptions()
                .fitCenter()
                .placeholder(R.mipmap.ic_launcher_imageloading_round)
                .placeholder(cpd)
                .override(width,height)
                .error(R.mipmap.ic_launcher_imageloading_round);

        Glide.with(ImageSearchApplication.getAppContext())
                .load(img_url)
                .apply(options)
                .into(photoView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(doc_url));
                startActivity(webIntent);
            }
        });
    }
}
