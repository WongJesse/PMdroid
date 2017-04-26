package com.huang.pmdroid;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;

import com.huang.pmdroid.adapters.AppInfoAdapter;
import com.huang.pmdroid.db.DbHelper;
import com.huang.pmdroid.models.AppInfo;
import com.huang.pmdroid.models.ProgressView;
import com.huang.pmdroid.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huang on 2017/4/24.
 */
public class AddWhiteListActivity extends AppCompatActivity {
    private Context context;
    List<AppInfo> appInfos = new ArrayList<>();
    AsyncTask<Void, Integer, List<AppInfo>> task;
    private RecyclerView recyclerView;
    private AppInfoAdapter appInfoAdapter;
    private Map<Integer, Boolean> map = new HashMap<>();
    private Button btnChoice;
    private ProgressBar progressBar;
    private int cntCheckBox = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_add_whitelist);
        initToolbar();
        initRecyclerView();
        ProgressView progressView = new ProgressView();
        progressBar = progressView.createProgressBar(this, null);
        progressBar.setVisibility(View.VISIBLE);
        btnChoice = (Button) findViewById(R.id.btn_choice_whiteList);
        btnChoice.setEnabled(false);
        btnChoice.setTextColor(getResources().getColor(R.color.primary_text_disabled));
        btnChoice.setOnClickListener(btnChoiceListener);
    }

    private void initToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_add_whitelist);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initRecyclerView(){
        recyclerView = (RecyclerView) findViewById(R.id.add_whiteList_recyclerView);
        appInfoAdapter = new AppInfoAdapter(context);
        recyclerView.setAdapter(appInfoAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        appInfoAdapter.setOnCheckBoxClickListener(new AppInfoAdapter.OnCheckBoxClickListener() {
            @Override
            public void onCheckBoxClick(CompoundButton buttonView, boolean isChecked, int position) {
                map.put(position, isChecked);
                if (isChecked) {
                    cntCheckBox++;
                    btnChoice.setEnabled(true);
                    btnChoice.setTextColor(getResources().getColor(R.color.white));
                    btnChoice.setText("确定(" + cntCheckBox + ")");
                } else {
                    cntCheckBox--;
                    btnChoice.setEnabled(true);
                    btnChoice.setTextColor(getResources().getColor(R.color.white));
                    btnChoice.setText("确定(" + cntCheckBox + ")");
                }
                if (cntCheckBox == 0) {
                    btnChoice.setEnabled(false);
                    btnChoice.setTextColor(getResources().getColor(R.color.primary_text_disabled));
                    btnChoice.setText("确定");
                }
                Log.i(Constants.TAG, position +"");
            }
        });
    }

    View.OnClickListener btnChoiceListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            for(int i = 0; i < map.size(); i++){
                if(map.get(i)){
                    AppInfo appInfoTmp =  appInfos.get(i);
                    DbHelper.getInstance(context).insertWhiteList(appInfoTmp);
                }
            }
            map.clear();
            finish();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        fillData();
    }

    private void initMap(int size){
        for(int i = 0; i < size; i++){
            map.put(i, false);
        }
    }

    private void fillData(){
        task = new AsyncTask<Void, Integer, List<AppInfo>>(){
            @Override
            protected List<AppInfo> doInBackground(Void... params) {
                PackageManager pm = context.getPackageManager();
                List<PackageInfo> packageInfos = pm.getInstalledPackages(0);
                Log.i(Constants.TAG, packageInfos.size() + "get here");
                List<String> dbPackNames = DbHelper.getInstance(context).getPackNameFromWhiteList();
                appInfos.clear();
                for(PackageInfo packageInfo : packageInfos){
                    if(!dbPackNames.contains(packageInfo.packageName)){
                        final AppInfo appInfo = new AppInfo();
                        Drawable appIcon = packageInfo.applicationInfo.loadIcon(pm);
                        appInfo.setAppIcon(appIcon);
                        String appName = packageInfo.applicationInfo.loadLabel(pm).toString();
                        appInfo.setAppName(appName);
                        String packName = packageInfo.packageName;
                        appInfo.setPackName(packName);
                        appInfos.add(appInfo);
                    }
                }
                return appInfos;
            }
            @Override
            protected void onPostExecute(List<AppInfo> result) {
                super.onPostExecute(result);
                try{
                    initMap(result.size());
                    appInfoAdapter.setData(result);
                    progressBar.setVisibility(View.INVISIBLE);
                }catch (Exception e){
                    e.printStackTrace();
                }


            }



        };
        task.execute();
    }



    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
