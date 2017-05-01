package com.huang.pmdroid.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Log;

import com.huang.pmdroid.MainActivity;
import com.huang.pmdroid.R;
import com.huang.pmdroid.RecordActivity;
import com.huang.pmdroid.db.DbHelper;
import com.huang.pmdroid.models.Record;
import com.huang.pmdroid.utils.Constants;
import com.huang.pmdroid.utils.SplitHelper;
import com.huang.pmdroid.db.SensitivePermissionsData;
import java.util.List;
import java.util.Map;

/**
 * Created by huang on 2017/4/14.
 *
 */
public class MonitorService extends Service {
    private Context context;
    private DataReceiver receiver;
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Log.e("777","startSuccess");
        receiver = new DataReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.huang.pmdroid.receive");
        registerReceiver(receiver, filter);
        context = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification.Builder builder = new Notification.Builder(this.getApplicationContext());
        Intent nfIntent = new Intent(this, MainActivity.class);
        builder.setContentIntent(PendingIntent.
                getActivity(this, 0, nfIntent, 0))
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                        R.mipmap.ic_launcher))
                .setContentTitle("PMdroid")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("PMdroid正在监控应用")
                .setWhen(System.currentTimeMillis());
        Notification notification = builder.build();
        notification.defaults = Notification.DEFAULT_SOUND;
        startForeground(6666,notification);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d("MonitorService", "onDestroy()");
        stopForeground(true);// 停止前台服务--参数：表示是否移除之前的通知
        unregisterReceiver(receiver);
        super.onDestroy();
    }
    //传入的from与to不一定是包名，需要分割、比较确定包名
    private boolean decisionMaker(Record record){
        int index;
        String from = record.getOrigin();
        String to = record.getDest();
        List<String> listFrom = SplitHelper.stringSplit(from);
        List<String> listTo = SplitHelper.stringSplit(to);
        if(!listFrom.isEmpty() && !listTo.isEmpty()){
            PackageManager packageManager = context.getPackageManager();
            List<ApplicationInfo> infos = packageManager.getInstalledApplications(PackageManager.GET_ACTIVITIES);
            //getFromPackage　因为分割的时候长的包放在后面，后面的包更容易比对成功，所以从后往前遍历可以减少比较次数
            flag0:for(index = listFrom.size() - 1; index >= 0; index--){
                String cmp = listFrom.get(index);
                for(ApplicationInfo info : infos){
                    if(info.packageName.equals(cmp)){
                        from = cmp;
                        Log.e("MonitorService", "getFromPackage  "+from);
                        break flag0;
                    }
                }
            }
            if(index == -1){
                return false;
            }
            //getToPackage
            flag1:for(index = listTo.size() - 1; index >= 0; index--){
                String cmp = listTo.get(index);
          //      Log.e("MonitorServiceTo", cmp);
                for(ApplicationInfo info : infos){
                    if(info.packageName.equals(cmp)){
                        to = cmp;
                        Log.e("MonitorService", "getToPackage  "+to);
                        break flag1;
                    }
                }
            }
            if(index == -1){
                return false;
            }
            if(from.equals(to)){
                return false;
            }

            //检查from 与 to 是否在白名单中, 如果在,返回false
            if(checkFromWhiteList(from, to)){
                return false;
            }

            String permissionFrom[] = getPermission(from);
            String permissionTo[] = getPermission(to);
            if(permissionFrom == null || permissionTo == null){
                Log.e("MonitorService","from/to no permission");
                return false;
            }
            else{
                Map<String,List> data = SensitivePermissionsData.getData();
                //for(int i = 0; i < permissionFrom.length; i++){
                for(String perFrom : permissionFrom){
                    if(data.containsKey(perFrom)){
                        List<String> list = data.get(perFrom);
                        for(String perTo : permissionTo){
                            for(String cmp : list){
                                if(perTo.equals(cmp)){
                                    Log.e("MonitorServiceLast", perFrom);
                                    Log.e("MonitorServiceLast", perTo);
                                    record.setOrigin(from);   //更新包名
                                    record.setDest(to);
                                    return true;
                                }
                            }
                        }
                    }
                }
                Log.e("MonitorServiceLast", "can not cmp success");
                return false;
            }

        }
        else
            return false;
    }

    private boolean checkFromWhiteList(String from, String to){
        return (DbHelper.getInstance(context).checkPackNameExist(from) || DbHelper.getInstance(context).checkPackNameExist(to));
    /*    if(DbHelper.getInstance(context).checkPackNameExist(from) || DbHelper.getInstance(context).checkPackNameExist(to))
            return true;
        else
            return false;  */
    }

    private String[] getPermission(String string){
        try{
   //         int length;
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(string, PackageManager.GET_PERMISSIONS);
           // String permissions[] = packInfo.requestedPermissions;
/*            if(permissions != null){
                length = permissions.length;
            }
            else
                length = 0;
            Log.e("MonitorService",string);
            for(int i = 0 ;i < length ;i ++){
               Log.e("MonitorService",permissions[i]);
            }   */
            return packInfo.requestedPermissions;
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
            Log.e("MonitorService","NameNotFound");
            return new String[1];
        }
    }

    private class DataReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent){
            if(intent.getAction().equals("com.huang.pmdroid.receive")){
                boolean decision;
                Record record = intent.getParcelableExtra("intentData");
                String to = record.getDest();
                String from = record.getOrigin();
                Log.i(Constants.TAG, from);
                Log.i(Constants.TAG,to);
                Log.i(Constants.TAG, record.getIntentExtras());
                decision = decisionMaker(record);
                Log.e("MonitorServiceResult", ""+decision);
                if(decision){
                    Log.i(Constants.TAG, record.getOrigin());
                    Log.i(Constants.TAG, record.getDest());
                    from = record.getOrigin();  //更新包名
                    to = record.getDest();
                    DbHelper.getInstance(context).insertRecord(record);
                    sendNotification(from, to);
                }

            }
        }
    }

    private void sendNotification(String from, String to){
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent mainIntent = new Intent(this, MainActivity.class);
        //将MainAtivity的launchMode设置成SingleTask, 或者在下面flag中加上Intent.FLAG_CLEAR_TOP,
        //如果Task栈中有MainActivity的实例，就会把它移到栈顶，把在它之上的Activity都清理出栈，
        //如果Task栈不存在MainActivity实例，则在栈顶创建
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent recordIntent = new Intent(this, RecordActivity.class);
        Intent[] nf = {mainIntent, recordIntent};
        Notification.Builder builder = new Notification.Builder(this.getApplicationContext());
        builder.setContentIntent(PendingIntent.
                getActivities(this, 0, nf, 0)) //getActivities可以传入Intent数组， getActivity传入Intent
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                        R.mipmap.ic_launcher))
                .setTicker("PMdroid")
                .setContentTitle("检测到权限提升攻击风险！")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("点击查看详情。")
                .setStyle(new Notification.BigTextStyle()
                    .bigText("应用"+from+"与应用"+to+"的通信存在权限提升的风险，点击查看详情。"))
                .setWhen(System.currentTimeMillis());
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        manager.notify(7777,notification);
    }
}
