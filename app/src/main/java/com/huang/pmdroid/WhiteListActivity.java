package com.huang.pmdroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.huang.pmdroid.adapters.AppInfoAdapter;
import com.huang.pmdroid.adapters.WhiteListAdapter;
import com.huang.pmdroid.db.DbHelper;
import com.huang.pmdroid.models.AppInfo;
import com.huang.pmdroid.models.Record;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by huang on 2017/4/10.
 */
public class WhiteListActivity extends AppCompatActivity {
    private TextView tvHintWhiteList;
    private Button btnAdd;
    private RecyclerView recyclerView;
    private WhiteListAdapter whiteListAdapter;
    private Context context;
    private List<AppInfo> whiteListInfos = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whitelist);
        context = this;
        tvHintWhiteList = (TextView) findViewById(R.id.tv_hint_whiteList);
        initToolbar();
        initRecyclerView();
        btnAdd = (Button) findViewById(R.id.btn_add_whiteList);
        btnAdd.setOnClickListener(addWhiteListListener);
    }

    View.OnClickListener addWhiteListListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(WhiteListActivity.this, AddWhiteListActivity.class);
            startActivity(intent);
        }
    };

    private void initToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_whitelist);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initRecyclerView(){
        recyclerView = (RecyclerView) findViewById(R.id.whiteList_recyclerView);
        whiteListAdapter = new WhiteListAdapter(context);
        recyclerView.setAdapter(whiteListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        whiteListAdapter.setOnButtonClickListener(new WhiteListAdapter.OnButtonClickListener() {
            @Override
            public void onButtonClick(int position) {

            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        try{
            updateWhiteList();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateWhiteList() {
        Observable.create(new Observable.OnSubscribe<List<AppInfo>>() {
            @Override
            public void call(Subscriber<? super List<AppInfo>> subscriber) {
                whiteListInfos.clear();
                whiteListInfos = DbHelper.getInstance(context).getAllAppInfo();
                subscriber.onNext(whiteListInfos);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<AppInfo>>() {
                    @Override
                    public void call(List<AppInfo> appInfos) {
                        if (appInfos.size() == 0) {
                            tvHintWhiteList.setText(R.string.hint_not_data);
                        } else {
                            tvHintWhiteList.setText("");
                        }
                        whiteListAdapter.setData(appInfos);
                    }
                });

    }


            public boolean onOptionsItemSelected(MenuItem item) {
                // TODO Auto-generated method stub
                if (item.getItemId() == android.R.id.home) {
                    finish();
                    return true;
                }
                return super.onOptionsItemSelected(item);
            }
        }
