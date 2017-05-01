package com.huang.pmdroid;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.WindowManager;
import android.widget.TextView;

import com.huang.pmdroid.adapters.RecordAdapter;
import com.huang.pmdroid.db.DbHelper;
import com.huang.pmdroid.models.Record;
import com.huang.pmdroid.utils.Constants;
import com.huang.pmdroid.utils.DataUtil;
import com.huang.pmdroid.utils.HtmlBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
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
public class RecordActivity extends AppCompatActivity {
    private List<Record> mResultlist = new ArrayList<>();
    private Toolbar toolbar;
    private RecordAdapter recordAdapter;
    private TextView tvHintRecord;
    private Context context;
    private boolean selectingState=false;
    private int selectNumber=0;
    private int menuState=0;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_record);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolbar();
        initRecyclerView();
    }

    @Override
    protected void onResume(){
        super.onResume();
        try{
            updateList();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void initToolbar()
    {
        toolbar.setTitle(R.string.title_record);
        setSupportActionBar(toolbar);
        selectingState = false;
        selectNumber = 0;
        setMenuState(0);
   /*     toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_clear:
                        new AlertDialog.Builder(context)
                                .setMessage(R.string.dialog_clear_record_message)
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
                                                    DbHelper.getInstance(context).clearAllFromRecords();
                                                    recordAdapter.clearData();
                                                    tvHintRecord.setText(R.string.hint_not_data);
                                                    Log.i(Constants.TAG, "Success getId");
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }).show();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });  */
        if(getSupportActionBar() != null){
            getSupportActionBar().setHomeButtonEnabled(true);  //设置返回键可用
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initRecyclerView(){
        tvHintRecord = (TextView) findViewById(R.id.tv_hint_record);
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.record_recyclerView);
        recordAdapter = new RecordAdapter(this);
        recyclerView.setAdapter(recordAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recordAdapter.setOnItemClickListener(new RecordAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {
                if(selectingState){
                    if(!mResultlist.get(position).getIsSelected()){
                        selectNumber++;
                        toolbar.setTitle(selectNumber+"");
                        mResultlist.get(position).setIsSelected(true);
                    }else{
                        selectNumber--;
                        toolbar.setTitle(selectNumber+"");
                        mResultlist.get(position).setIsSelected(false);
                    }
                    recordAdapter.notifyDataSetChanged();
                    if( selectNumber==0){
                        setMenuState(0);
                        toolbar.setTitle(R.string.title_record);
                        selectingState=false;
                    }
                }
                else{
                    // 如果不是多选状态，则进入点击事件的显示拦截信息详情
                    if(position < mResultlist.size()){
                        showRecordDetail(position);
                    }

                }

            }

            @Override
            public void onItemLongClick(View view, int position) {
                if(!selectingState){
                    selectingState = true;
                    mResultlist.get(position).setIsSelected(true);
                    selectNumber++;
                    toolbar.setTitle(selectNumber+"");
                    setMenuState(1);
                    recordAdapter.notifyDataSetChanged();
                }else{
                    if(!mResultlist.get(position).getIsSelected()){
                        mResultlist.get(position).setIsSelected(true);
                        selectNumber++;
                        toolbar.setTitle(selectNumber+"");
                        recordAdapter.notifyDataSetChanged();
                    }else{
                        selectNumber--;
                        mResultlist.get(position).setIsSelected(false);
                        toolbar.setTitle(selectNumber+"");
                        recordAdapter.notifyDataSetChanged();
                        if(selectNumber == 0){
                            setMenuState(0);
                            toolbar.setTitle(R.string.title_record);
                            selectingState=false;
                        }
                    }
                }

            }
        });
    }

    private void  showRecordDetail(int position){
        Record record = mResultlist.get(position);
        Dialog dialog = new Dialog(context);
        View v = View.inflate(context, R.layout.dialog_record_detail, null);
        TextView tvRecordDialog = (TextView)v.findViewById(R.id.tv_dialog_record);

        HtmlBuilder htmlBuilder = new HtmlBuilder();
        htmlBuilder.newLine()
                .appendBold("method: ").appendText(record.getMethod()).newLine(2)
                .appendBold("time: ").appendText(DataUtil.getDateFromTimestamp(record.getCreatedAt())).newLine(2)
                .appendBold("from: ").appendText(record.getOrigin()).newLine(2)
                .appendBold("to: ").appendText(record.getDest()).newLine(2)
                .appendBold("action: ").appendText(record.getAction()).newLine(2)
                .appendBold("componentName: ").appendText(record.getComponentName())
                .newLine(2);
        //           .appendBold("intentExtras: ").appendText(record.getIntentExtras())
        //         .newLine(1);


        htmlBuilder.appendBold("intentExtras: ");
        if (record.getIntentExtras().equals("null")) {
            htmlBuilder.appendText("null");
        }
        else {
            try{
                htmlBuilder.appendText("[").newLine();
                JSONArray jsonArray = new JSONArray(record.getIntentExtras());
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    htmlBuilder
                            .appendTab(1).appendText("{").newLine()
                            .appendTab(2).appendText("key: ").appendText(jsonObject.getString("key")).newLine()
                            .appendTab(2).appendText("value: ").appendText(jsonObject.getString("value"))
                            .newLine()
                            .appendTab(2).appendText("class: ").appendText(jsonObject.getString("class"))
                            .newLine()
                            .appendTab(1).appendText("},").newLine();
                }
                htmlBuilder.appendText("]");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        htmlBuilder.newLine();

        tvRecordDialog.setText(htmlBuilder.build());
        dialog.setContentView(v);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT);
        }
        dialog.setTitle(R.string.dialog_detail_info);
        dialog.show();
    }

    private void updateList() throws IOException{
        Observable.create(new Observable.OnSubscribe<List<Record>>(){
            @Override
            public void call(Subscriber<? super List<Record>> subscriber){
                mResultlist.clear(); //初始化list,防止数据重复
                mResultlist = DbHelper.getInstance(context).getAllRecords();
  /*              Cursor cursor = dbHelper.getReadableDatabase().query("records", null, null, null, null, null, null);
                if(cursor != null && cursor.getCount() >= 0){
                    cursor.moveToLast();
                    for(int i = cursor.getCount() - 1; i >= 0; i--){
                        cursor.moveToPosition(i);
                        Record record = new Record();
                        record.setKeyId(cursor.getInt(cursor.getColumnIndex("record_id")));
                        Log.i(Constants.TAG,record.getKeyId()+"");
                        record.setCreatedAt(cursor.getLong(cursor.getColumnIndex("time")));
                        record.setOrigin(cursor.getString(cursor.getColumnIndex("origin")));
                        record.setDest(cursor.getString(cursor.getColumnIndex("dest")));
                        record.setMethod(cursor.getString(cursor.getColumnIndex("method")));
                        record.setAction(cursor.getString(cursor.getColumnIndex("action")));
                        record.setComponentName(cursor.getString(cursor.getColumnIndex("component_name")));
                        record.setIntentExtras(cursor.getString(cursor.getColumnIndex("intent_extras")));
                        mResultlist.add(record);
                    }
                    cursor.close();
                }  */
                subscriber.onNext(mResultlist);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Record>>() {
                    @Override
                    public void call(List<Record> records) {
                        if (records.size() == 0) {
                            tvHintRecord.setText(R.string.hint_not_data);
                        } else {
                            tvHintRecord.setText("");
                        }
                        recordAdapter.setData(records);
                    }
                });
    }

    private void setMenuState(int menuState){
        this.menuState = menuState;
        invalidateOptionsMenu();
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_record, menu);
        return true;
    }  */

   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch(item.getItemId()){
           case android.R.id.home:
               finish();
               break;
           case 0:
               new AlertDialog.Builder(context)
                       .setMessage(R.string.dialog_clear_record_message)
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
                                           DbHelper.getInstance(context).clearAllFromRecords();
                                           recordAdapter.clearData();
                                           tvHintRecord.setText(R.string.hint_not_data);
                                           Log.i(Constants.TAG, "Success getId");
                                       } catch (Exception e) {
                                           e.printStackTrace();
                                       }
                                   }
                               }).show();
               break;
           case 1:
               new AlertDialog.Builder(context)
                       .setTitle(context.getResources().getString(R.string.dialog_delete_record_title))
                       .setMessage(R.string.dialog_delete_record_message)
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
                                           for(Record record : mResultlist){
                                               if(record.getIsSelected()){
                                                   DbHelper.getInstance(context).deleteRecord(record.getKeyId());
                                               }
                                           }
                                           updateList();
                                           selectingState=false;
                                           selectNumber=0;
                                           toolbar.setTitle(R.string.title_record);
                                           setMenuState(0);
                                       } catch (Exception e) {
                                           e.printStackTrace();
                                       }
                                   }
                               }).show();
               break;
           case 2:
               for(Record record : mResultlist){
                   if(record.getIsSelected()){
                       record.setIsSelected(false);
                       selectNumber--;
                   }
               }
               recordAdapter.notifyDataSetChanged();
               setMenuState(0);
               selectingState = false;
               toolbar.setTitle(R.string.title_record);
               break;
       }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.clear();
        switch(menuState){
            case 0:
                menu.add(0, 0, 1, getResources().getString(R.string.action_clear)).setIcon(R.drawable.ic_white_clear).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                break;
            case 1:
                menu.add(0, 1, 2, getResources().getString(R.string.action_delete)).setIcon(R.drawable.ic_delete_white).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                menu.add(0, 2, 3, getResources().getString(R.string.action_cancel)).setIcon(R.drawable.ic_cancel_white).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            break;
        }
        return true;
    }
}


