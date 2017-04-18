package com.huang.pmdroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.huang.pmdroid.db.SensitivePermissionsData;
import com.huang.pmdroid.services.MonitorService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar mToolbar;
    private Button btn_start_service;
    private NavigationView mNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open,
                R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        setupDrawerContent(mNavigationView);

        //初始化敏感权限库
        SensitivePermissionsData.setInitData();

        btn_start_service = (Button) findViewById(R.id.bt_start_service);
        btn_start_service.setOnClickListener(this);
    }

        @Override
        public void onClick(View v){
            Intent intent = new Intent(this,MonitorService.class);
            startService(intent);
            Log.e("777","getInOnclick");
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
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }
}
