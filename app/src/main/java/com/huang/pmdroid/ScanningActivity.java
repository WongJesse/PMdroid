package com.huang.pmdroid;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by huang on 2017/4/10.
 */
public class ScanningActivity extends AppCompatActivity {

    Context mContext;
    private Button btn_scan;
    private TextView tv_show_scan, tv_show_progress;
    private int pkgCnt = 0;
    private int pkgSum;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);
        btn_scan = (Button) this.findViewById(R.id.bt_scan);
        tv_show_scan = (TextView) this.findViewById(R.id.tv_show_scan);
        tv_show_progress = (TextView) this.findViewById(R.id.tv_show_progress);
        btn_scan.setOnClickListener(scanListener);
        mContext = this;
        inittoolbar();
    }

    private void inittoolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_scanning);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    View.OnClickListener scanListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new Thread(){
                public void run(){
                    PackageManager packageManager = mContext.getPackageManager();
                    List<ApplicationInfo> infos = packageManager.getInstalledApplications(PackageManager.GET_ACTIVITIES);
                    pkgSum = infos.size();
                    for(ApplicationInfo info: infos){
                        pkgCnt++;
                        Log.e("app", "appName--->" + packageManager.getApplicationLabel(info) + "");
                        Log.e("pkgName: ", info.packageName);
                        final String appName = packageManager.getApplicationLabel(info).toString();
                        Log.e("appName", appName);
                        try {
                            PackageInfo packInfo = packageManager.getPackageInfo(info.packageName, PackageManager.GET_PERMISSIONS);
                            String permissons [] = packInfo.requestedPermissions;
                            //获取该app的所有权限
                            int length = permissons.length;
                            for(int i = 0 ;i < length ;i ++){
                                Log.e("app",permissons[i]);
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                                e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv_show_scan.setText("正在扫描:"+ appName);
                                tv_show_progress.setText("("+ pkgCnt + "/" + pkgSum + ")");
                            }
                        });

                    }



                }
            }.start();

        }
    };

    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


