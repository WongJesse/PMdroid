package com.huang.pmdroid;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huang.pmdroid.db.DbHelper;
import com.huang.pmdroid.db.SensitivePermissionsData;
import com.huang.pmdroid.models.AppInfo;
import com.huang.pmdroid.utils.Constants;

import java.util.List;
import java.util.Map;

/**
 * Created by huang on 2017/4/10.
 *
 */
public class ScanningActivity extends AppCompatActivity {

    Context mContext;
    private Button btn_scan, btn_start_whiteList;
    private TextView tv_show_scan, tv_show_progress, tv_num, tv_size, tv_prompt;
    private ImageView iv_default_bg, iv_scan_anim;
    private LinearLayout ll_scan_display;
    private int pkgCnt = 0;
    private int pkgSum;
    private int addFlag;
    private int addCnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);
        btn_scan = (Button) findViewById(R.id.bt_scan);
        btn_start_whiteList = (Button) findViewById(R.id.btn_start_whiteList);
        tv_num = (TextView) findViewById(R.id.tv_num);
        tv_size = (TextView) findViewById(R.id.tv_size);
        tv_prompt = (TextView) findViewById(R.id.tv_prompt);
        tv_show_scan = (TextView) findViewById(R.id.tv_show_scan);
        tv_show_progress = (TextView) findViewById(R.id.tv_show_progress);
        iv_default_bg = (ImageView) findViewById(R.id.iv_default_bg);
        iv_scan_anim = (ImageView) findViewById(R.id.iv_scan_anim);
        ll_scan_display = (LinearLayout) findViewById(R.id.ll_scan_display);

        btn_scan.setOnClickListener(scanListener);
        mContext = this;
        initToolbar();
        btn_start_whiteList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanningActivity.this, WhiteListActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_scanning);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setHomeButtonEnabled(true);  //设置返回键可用
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    View.OnClickListener scanListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new Thread(){
                public void run(){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btn_scan.setVisibility(View.INVISIBLE);
                            iv_default_bg.setVisibility(View.INVISIBLE);
                            tv_num.setVisibility(View.VISIBLE);
                            tv_size.setVisibility(View.VISIBLE);
                            startAnim();
                        }
                    });
                    PackageManager packageManager = mContext.getPackageManager();
                    List<ApplicationInfo> infos = packageManager.getInstalledApplications(PackageManager.GET_ACTIVITIES);
                    pkgSum = infos.size();
                    //加载白名单数据库
                    List<String> dbPackNames = DbHelper.getInstance(mContext).getPackNameFromWhiteList();
                    //加载敏感权限库
                    Map<String,List> data = SensitivePermissionsData.getData();

                    for(ApplicationInfo info: infos){
                        //初始0,存在敏感权限1,加入白名单2
                        addFlag = 0;
                        pkgCnt++;
                 //       Log.i(Constants.TAG, "appName--->" + packageManager.getApplicationLabel(info) + "");
                 //       Log.i(Constants.TAG, info.packageName);
                        final String appName = packageManager.getApplicationLabel(info).toString();
                //      Log.i(Constants.TAG, appName);
                        if(!dbPackNames.contains(info.packageName)){
                            try {
                                PackageInfo packInfo = packageManager.getPackageInfo(info.packageName, PackageManager.GET_PERMISSIONS);
                                String permissions [] = packInfo.requestedPermissions;
                                //获取该app的所有权限
                                if(permissions != null){
                                    for(String eachPermission : permissions){
                                        if(data.containsKey(eachPermission)){
                                            addFlag = 1;
                                            break;
                                        }
                                       // Log.i(Constants.TAG,eachPermission);
                                    }
                                }
                                if(addFlag == 0){
                                    addFlag = 2;
                                    final AppInfo appInfo = new AppInfo();
                                    Drawable appIcon = info.loadIcon(packageManager);
                                    appInfo.setAppIcon(appIcon);
                                    appInfo.setAppName(appName);
                                    appInfo.setPackName(info.packageName);
                                    DbHelper.getInstance(mContext).insertWhiteList(appInfo);
                                    addCnt++;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            TextView view = new TextView(ScanningActivity.this);
                                            view.setTextColor(0x66000000);
                                            view.setText(getResources().getString(R.string.add_to_whiteList, appName));
                                            ll_scan_display.addView(view, 0);
                                            tv_num.setText(getResources().getString(R.string.add_count, addCnt));
                                        }
                                    });

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.i(Constants.TAG, e+"");
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(pkgCnt == pkgSum){
                                    tv_show_scan.setText("扫描完成");
                                    tv_show_progress.setText("敏感权限库已更新！");
                                    tv_prompt.setVisibility(View.VISIBLE);
                                    iv_scan_anim.clearAnimation();
                                    iv_scan_anim.setVisibility(View.INVISIBLE);
                                    btn_start_whiteList.setVisibility(View.VISIBLE);
                                }else {
                                    tv_show_scan.setText(getResources().getString(R.string.scanning, appName));
                                    tv_show_progress.setText(getResources().getString(R.string.show_progress, pkgCnt, pkgSum));
                                }
                            }
                        });

                    }



                }
            }.start();

        }
    };

    private void startAnim(){
        TranslateAnimation ta = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_PARENT, 0,
                TranslateAnimation.RELATIVE_TO_PARENT, 0,
                TranslateAnimation.RELATIVE_TO_PARENT, 0,
                TranslateAnimation.RELATIVE_TO_PARENT, -0.92f);
        ta.setDuration(1000);
        ta.setRepeatMode(TranslateAnimation.REVERSE);
        ta.setRepeatCount(-1);
        iv_scan_anim.startAnimation(ta);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}


