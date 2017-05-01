package com.huang.pmdroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huang.pmdroid.db.DbHelper;
import com.huang.pmdroid.db.SensitivePermissionsData;
import com.huang.pmdroid.services.MonitorService;
import com.huang.pmdroid.utils.Constants;
import com.huang.pmdroid.utils.ServiceUtil;

public class MainActivity extends AppCompatActivity {
    private static long lastBackTime = 0;
    private DrawerLayout mDrawerLayout;
    private Context context;
    private TextView tv_text_show;
    private ImageView iv_main_bg, iv_main_bg_anim;
    private ImageView iv_security_bg;
    private Button btn_close;
    private TextView tv_show_open;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        context = this;
        iv_main_bg = (ImageView) findViewById(R.id.iv_main_bg);
        iv_main_bg_anim = (ImageView) findViewById(R.id.iv_main_bg_anim);
        tv_text_show = (TextView) findViewById(R.id.tv_text_show);
        tv_show_open = (TextView) findViewById(R.id.tv_show_open);
        iv_security_bg = (ImageView) findViewById(R.id.iv_security_bg);
        btn_close = (Button) findViewById(R.id.bt_close);
        //初始化敏感权限库
        SensitivePermissionsData.setInitData();

        //初始化拦截记录数据库
        DbHelper dbHelper = new DbHelper(this);
        dbHelper.getWritableDatabase();

        iv_main_bg.setOnClickListener(startServiceListener);
        btn_close.setOnClickListener(closeServiceListener);
  //      addDataTest();

    }

    private void initToolbar(){
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open,
                R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        setupDrawerContent(mNavigationView);
    }

 /*       public void addDataTest(){
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("time",System.currentTimeMillis());
            values.put("origin", "test");
            values.put("dest","test");
            values.put("method","test");
            values.put("action","test");
            values.put("component_name","test");
            values.put("intent_extras","test");
            db.insert("records", null, values);
            Log.i(Constants.TAG, "successful insert!");
        }  */




    View.OnClickListener startServiceListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!isModuleActive()){
                new AlertDialog.Builder(context)
                        .setTitle(context.getResources().getString(R.string.mention))
                        .setMessage(context.getResources().getString(R.string.mention_xposed))
                        .setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
            }else{
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startAnim();
                                tv_text_show.setText(getResources().getString(R.string.opening));
                            }
                        });
                        try {
                            Thread.sleep( 3000 );
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(context,MonitorService.class);
                        startService(intent);
                        if(ServiceUtil.isServiceRunning(context, Constants.SERVICE_NAME)) {
                            //           Log.i(Constants.TAG, "serviceIsStarting");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    iv_main_bg_anim.clearAnimation();
                                    iv_main_bg_anim.setVisibility(View.INVISIBLE);
                                    tv_text_show.setVisibility(View.INVISIBLE);
                                    iv_main_bg.setVisibility(View.INVISIBLE);

                                    iv_security_bg.setVisibility(View.VISIBLE);
                                    tv_show_open.setVisibility(View.VISIBLE);
                                    btn_close.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }
                }).start();
            }

        }
    };


/*    View.OnClickListener startServiceListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startAnim();
                                tv_text_show.setText(getResources().getString(R.string.opening));
                            }
                        });
                        try {
                            Thread.sleep( 3000 );
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(context,MonitorService.class);
                        startService(intent);
                        Log.i(Constants.TAG, ServiceUtil.isServiceRunning(context, Constants.SERVICE_NAME) + "");
                        if(ServiceUtil.isServiceRunning(context, Constants.SERVICE_NAME)) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    iv_main_bg_anim.clearAnimation();
                                    iv_main_bg_anim.setVisibility(View.INVISIBLE);
                                    tv_text_show.setVisibility(View.INVISIBLE);
                                    iv_main_bg.setVisibility(View.INVISIBLE);

                                    iv_security_bg.setVisibility(View.VISIBLE);
                                    tv_show_open.setVisibility(View.VISIBLE);
                                    btn_close.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }
                }).start();
            }


    };
  */

    View.OnClickListener closeServiceListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            iv_security_bg.setVisibility(View.INVISIBLE);
                            tv_show_open.setVisibility(View.INVISIBLE);
                            btn_close.setVisibility(View.INVISIBLE);
                            iv_main_bg.setVisibility(View.VISIBLE);
                            startAnim();
                            tv_text_show.setVisibility(View.VISIBLE);
                            tv_text_show.setText(getResources().getString(R.string.closing));
                        }
                    });
                    try {
                        Thread.sleep( 2000 );
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(context,MonitorService.class);
                    stopService(intent);
                    if(!ServiceUtil.isServiceRunning(context, Constants.SERVICE_NAME)) {
                        Log.i(Constants.TAG, "serviceIsStop");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                iv_main_bg_anim.clearAnimation();
                                iv_main_bg_anim.setVisibility(View.INVISIBLE);
                                tv_text_show.setText(getResources().getString(R.string.open_service));
                            }
                        });
                    }
                }
            }).start();
        }
    };

    //查询Xposed是否启用
    private static boolean isModuleActive(){
        return false;
    }


    private void loadContentView(){
        if(ServiceUtil.isServiceRunning(context, Constants.SERVICE_NAME)){
            Log.i(Constants.TAG, "serviceIsRunning");
            iv_security_bg.setVisibility(View.VISIBLE);
            tv_show_open.setVisibility(View.VISIBLE);
            btn_close.setVisibility(View.VISIBLE);

            iv_main_bg.setVisibility(View.INVISIBLE);
            tv_text_show.setVisibility(View.INVISIBLE);
        }else{
            iv_main_bg.setVisibility(View.VISIBLE);
            tv_text_show.setVisibility(View.VISIBLE);
            tv_text_show.setText(getResources().getString(R.string.open_service));

            iv_security_bg.setVisibility(View.INVISIBLE);
            tv_show_open.setVisibility(View.INVISIBLE);
            btn_close.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onResume(){
        super.onResume();
        //设置默认选中项
        mNavigationView.getMenu().getItem(0).setChecked(true);
        loadContentView();
    }

    private void startAnim(){
        RotateAnimation ra = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        ra.setInterpolator(new LinearInterpolator());
        ra.setDuration(1000);
        ra.setRepeatCount(-1);
        iv_main_bg_anim.startAnimation(ra);
    }



    private void switchToScanning(){
        Intent intent = new Intent(MainActivity.this, ScanningActivity.class);
        MainActivity.this.startActivity(intent);
    }
    private void switchToRecord(){
        Intent intent = new Intent(MainActivity.this, RecordActivity.class);
        MainActivity.this.startActivity(intent);
    }

    private  void switchToWhiteList(){
        Intent intent = new Intent(MainActivity.this, WhiteListActivity.class);
        MainActivity.this.startActivity(intent);
    }



    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.navigation_item_main:
                                break;
                            case R.id.navigation_item_appscanning:
                                switchToScanning();
                                break;
                            case R.id.navigation_item_record:
                                switchToRecord();
                                break;
                            case R.id.navigation_item_whitelisting:
                                switchToWhiteList();
                                break;

                        }
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastBackTime < 2000) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, R.string.press_again_exit_app, Toast.LENGTH_SHORT).show();
        }
        lastBackTime = currentTime;
    }
}
