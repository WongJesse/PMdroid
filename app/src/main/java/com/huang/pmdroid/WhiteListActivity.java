package com.huang.pmdroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.huang.pmdroid.adapters.WhiteListAdapter;
import com.huang.pmdroid.db.DbHelper;
import com.huang.pmdroid.models.AppInfo;
import com.huang.pmdroid.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by huang on 2017/4/10.
 *
 */
public class WhiteListActivity extends AppCompatActivity {
    private TextView tvHintWhiteList;
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
        Button btnAdd = (Button) findViewById(R.id.btn_add_whiteList);
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
        if(getSupportActionBar() != null){
            getSupportActionBar().setHomeButtonEnabled(true);  //设置返回键可用
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.whiteList_recyclerView);
        whiteListAdapter = new WhiteListAdapter(context);
        recyclerView.setAdapter(whiteListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        whiteListAdapter.setOnButtonClickListener(new WhiteListAdapter.OnButtonClickListener() {
            @Override
            public void onButtonClick(final int position) {
                String message = context.getResources().getString(R.string.dialog_delete_whiteList_message)
                        + whiteListInfos.get(position).getAppName()
                        + context.getResources().getString(R.string.dialog_delete_whiteList_message1);
                new AlertDialog.Builder(context)
                        .setTitle(context.getResources().getString(R.string.dialog_delete_whiteList_title))
                        .setMessage(message)
                        .setNegativeButton(android.R.string.cancel,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                        .setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            DbHelper.getInstance(context).deleteWhiteList(whiteListInfos.get(position).getPackName());
                                          //  Log.i(Constants.TAG, whiteListInfos.get(position).getPackName());
                                            Log.i(Constants.TAG, position + "");
                                            whiteListAdapter.removeData(position);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).show();

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
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        if(item.getItemId() == 0){
            new AlertDialog.Builder(context)
                    .setMessage(R.string.dialog_clear_whiteList_message)
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        DbHelper.getInstance(context).clearAllFromWhiteList();
                                        whiteListAdapter.clearData();
                                        tvHintWhiteList.setText(R.string.hint_not_data);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.add(0, 0, 1, getResources().getString(R.string.action_clear)).setIcon(R.drawable.ic_white_clear).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

}
