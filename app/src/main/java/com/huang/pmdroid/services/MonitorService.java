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
import com.huang.pmdroid.utils.SplitHelper;
import com.huang.pmdroid.db.SensitivePermissionsData;
import java.util.List;
import java.util.Map;

/**
 * Created by huang on 2017/4/14.
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
        super.onDestroy();
    }
    //传入的from与to不一定是包名，需要分割、比较确定包名
    private boolean decisionMaker(String from, String to){
        int index;
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

            String permissionFrom[] = getPermission(from);
            String permissionTo[] = getPermission(to);
            if(permissionFrom == null || permissionTo == null){
                Log.e("MonitorService","from/to no permission");
                return false;
            }
            else{
                Map<String,List> data = SensitivePermissionsData.getData();
                for(int i = 0; i < permissionFrom.length; i++){
                    if(data.containsKey(permissionFrom[i])){
                        List<String> list = data.get(permissionFrom[i]);
                        for(int j = 0; j < permissionTo.length; j++){
                            for(String cmp : list){
                                if(permissionTo[j].equals(cmp)){
                                    Log.e("MonitorServiceLast", permissionFrom[i]);
                                    Log.e("MonitorServiceLast", permissionTo[j]);
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

    private String[] getPermission(String string){
        try{
   //         int length;
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(string, PackageManager.GET_PERMISSIONS);
            String permissions[] = packInfo.requestedPermissions;
/*            if(permissions != null){
                length = permissions.length;
            }
            else
                length = 0;
            Log.e("MonitorService",string);
            for(int i = 0 ;i < length ;i ++){
               Log.e("MonitorService",permissions[i]);
            }   */
            return permissions;
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
            Log.e("MonitorService","NameNotFound");
            String exception[] = new String[1];
            return exception;
        }
    }

    private class DataReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent){
            if(intent.getAction().equals("com.huang.pmdroid.receive")){
                boolean decision;
                String from = intent.getExtras().getString("from");
                String to = intent.getExtras().getString("to");
        //        Log.e("777", from);
         //       Log.e("777",to);
                decision = decisionMaker(from, to);
                Log.e("MonitorServiceResult", ""+decision);
                if(decision){
                     sendNotification(from, to);
                }

            }
        }
    }

    private void sendNotification(String from, String to){
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent nf = new Intent(this, RecordActivity.class);
        Notification.Builder builder = new Notification.Builder(this.getApplicationContext());
        builder.setContentIntent(PendingIntent.
                getActivity(this, 0, nf, 0))
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
