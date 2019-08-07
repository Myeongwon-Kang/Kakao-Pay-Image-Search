package com.genie97.myapplication.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.genie97.myapplication.R;
import com.genie97.myapplication.contract.MainContract;
import com.genie97.myapplication.databinding.ActivityMainBinding;
import com.genie97.myapplication.model.ImageSearchApplication;
import com.genie97.myapplication.model.NetworkService;
import com.genie97.myapplication.viewmodel.MainViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainContract {

    private static final String TAG = "MainActivity";

    RecyclerView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //toolbar - 이름
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("임효진");

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        Log.d(TAG, "passed ActivityMainBinding");
        final NetworkService networkService = ((ImageSearchApplication) getApplication()).getNetworkService();
        Log.d(TAG, "passed getNetworkService()");
        final MainViewModel mainViewModel = new MainViewModel(this, networkService);
        binding.setMainViewModel(mainViewModel);
        Log.d(TAG, "passed setMainViewModel");
        initMaxSize();

        setupViews(networkService, this);
    }

    private RecyclerViewAdapter recyclerViewAdapter;
    private Parcelable recyclerViewState;
    private int maxSize;
    private boolean scrollLock = false;

    private RelativeLayout noResult;

    /*
     * 화면 요소 설정
     * */
    private void setupViews(final NetworkService mNetworkService, final MainContract mMainContract) {
        Log.d(TAG, "setupViews()");

        noResult = (RelativeLayout) findViewById(R.id.noResult);

        result = (RecyclerView) findViewById(R.id.result);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        result.setLayoutManager(linearLayoutManager);

        result.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastCompletelyVisibleItemPosition = 0;
                lastCompletelyVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                        .findLastVisibleItemPosition();

                if (lastCompletelyVisibleItemPosition == maxSize - 1) {
                    Log.d(TAG, "lastCompletelyVisibleItemPosition: " + lastCompletelyVisibleItemPosition);

                    // scrolling 오작동 방지
                    if (!scrollLock) {
                        scrollLock = true;
                        // state 저장
                        recyclerViewState = result.getLayoutManager().onSaveInstanceState();
                        MainViewModel.loadMore();
                    }
                }
            }
        });

        EditText search_query = (EditText) findViewById(R.id.search_query);
        search_query.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "Input start");
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "Input end");
                MainViewModel.searchImages(1);
            }
        });
    }

    @Override
    public void moveActivity(String img_url, int height, int width, String doc_url) {
        Intent intent = new Intent(getApplicationContext(), ImageDetail.class);
        intent.putExtra("ImageUrl", img_url);
        intent.putExtra("width", width);
        intent.putExtra("height", height);
        intent.putExtra("DocUrl", doc_url);
        startActivity(intent);
    }

    @Override
    public void displayInfo(List<String> mList, List<String> thumbNailList,
                            List<String> collectionList, List<String> siteNameList,
                            List<String> docUrlList, List<String> dateTimeList, List<Integer> widthList, List<Integer> heightList) {
        recyclerViewAdapter = new RecyclerViewAdapter(mList, thumbNailList, collectionList, siteNameList, docUrlList, dateTimeList, widthList, heightList);
        result.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void initMaxSize() {
        Log.d(TAG, "initMaxSize()");
        maxSize = 0;
        Log.d(TAG, "current maxSize: " + maxSize);
    }

    @Override
    public void setMaxSize(int max) {
        Log.d(TAG, "setMaxSize()");
        maxSize = max;
        Log.d(TAG, "current maxSize: " + maxSize);
    }

    @Override
    public void setResult() {
        Log.d(TAG, "setResult()");
        result.getLayoutManager().onRestoreInstanceState(recyclerViewState);

        // scrollLock 해제
        scrollLock = false;
    }

    @Override
    public void setNoResult(boolean flag) {
        if (flag) {
            noResult.setVisibility(View.VISIBLE);
        } else {
            noResult.setVisibility(View.GONE);
        }
    }

    @Override
    public void createToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainViewModel.timerStop();
    }

}