package com.huang.pmdroid.services;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
//import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import com.huang.pmdroid.models.Record;
import com.huang.pmdroid.utils.Constants;
import com.huang.pmdroid.utils.DataUtil;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by huang on 2017/4/13.
 *
 */
public class Xposed implements IXposedHookLoadPackage{
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if(lpparam.packageName.equals("com.huang.pmdroid")){
            findAndHookMethod("com.huang.pmdroid.MainActivity", lpparam.classLoader,
                    "isModuleActive", XC_MethodReplacement.returnConstant(true));
        }

         /*
        *  hook intent for broadcast
        *   11（6） methods
        * */


        findAndHookMethod("android.content.ContextWrapper", lpparam.classLoader,
                "sendOrderedBroadcast", Intent.class, String.class, BroadcastReceiver.class,
                Handler.class, int.class, String.class, Bundle.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Log.e("66666", "hook Broadcast9");
                Intent intent = (Intent) param.args[0];
                String origin = param.thisObject.getClass().getName();
                Context context = (Context)param.thisObject;
                monitorBroadcast(intent, origin, context, "sendOrderedBroadcast");
            }
        });

        findAndHookMethod("android.content.ContextWrapper", lpparam.classLoader,
                "sendOrderedBroadcast", Intent.class, String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Log.e("66666", "hook Broadcast8");
                Intent intent = (Intent) param.args[0];
                String origin = param.thisObject.getClass().getName();
                Context context = (Context)param.thisObject;
                monitorBroadcast(intent, origin, context, "sendOrderedBroadcast");
            }
        });

            //hook失效，暂不明原因，失效后影响后面的hook函数,个人认为是UserHandle类没有在4.2版本下的源码中
        //因此在4.2以下的版本编译，将与多用户有关的hook函数注释掉
/*        findAndHookMethod("android.content.ContextWrapper", lpparam.classLoader,
                "sendOrderedBroadcastAsUser", Intent.class, UserHandle.class, String.class,
                BroadcastReceiver.class, Handler.class, int.class, String.class, Bundle.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Log.e("66666", "hook Broadcast7");
                Intent intent = (Intent) param.args[0];
                String origin = param.thisObject.getClass().getName();
                Context context = (Context)param.thisObject;
                monitorBroadcast(intent, origin, context, "sendOrderedBroadcastAsUser");
            }
        });

        findAndHookMethod("android.content.ContextWrapper", lpparam.classLoader,
                "sendBroadcastAsUser", Intent.class, UserHandle.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Log.e("66666", "hook Broadcast6");
                Intent intent = (Intent) param.args[0];
                String origin = param.thisObject.getClass().getName();
                Context context = (Context)param.thisObject;
                monitorBroadcast(intent, origin, context, "sendBroadcastAsUser");
            }
        });


        findAndHookMethod("android.content.ContextWrapper", lpparam.classLoader,
                "sendBroadcastAsUser", Intent.class, UserHandle.class, String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Log.e("66666", "hook Broadcast5");
                Intent intent = (Intent) param.args[0];
                String origin = param.thisObject.getClass().getName();
                Context context = (Context)param.thisObject;
                monitorBroadcast(intent, origin, context, "sendBroadcastAsUser");
            }
        });  */

       findAndHookMethod("android.content.ContextWrapper", lpparam.classLoader,
                "sendStickyBroadcast", Intent.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Log.e("66666", "hook Broadcast4");
                Intent intent = (Intent) param.args[0];
                String origin = param.thisObject.getClass().getName();
                Context context = (Context)param.thisObject;
                monitorBroadcast(intent, origin, context, "sendStickyBroadcast");
            }
        });

 /*       findAndHookMethod("android.content.ContextWrapper", lpparam.classLoader,
                "sendStickyBroadcastAsUser", Intent.class, UserHandle.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Log.e("66666", "hook Broadcast3");
                Intent intent = (Intent) param.args[0];
                String origin = param.thisObject.getClass().getName();
                Context context = (Context)param.thisObject;
                monitorBroadcast(intent, origin, context, "sendStickyBroadcastAsUser");
            }
        });   */


        findAndHookMethod("android.content.ContextWrapper", lpparam.classLoader,
                "sendStickyOrderedBroadcast", Intent.class, BroadcastReceiver.class,
                Handler.class, int.class, String.class, Bundle.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Log.e("66666", "hook Broadcast2");
                Intent intent = (Intent) param.args[0];
                String origin = param.thisObject.getClass().getName();
                Context context = (Context)param.thisObject;
                monitorBroadcast(intent, origin, context, "sendStickyOrderedBroadcast");
            }
        });

/*        findAndHookMethod("android.content.ContextWrapper", lpparam.classLoader,
                "sendStickyOrderedBroadcastAsUser", Intent.class, UserHandle.class,
                BroadcastReceiver.class, Handler.class, int.class, String.class, Bundle.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Log.e("66666", "hook Broadcast1");
                Intent intent = (Intent) param.args[0];
                String origin = param.thisObject.getClass().getName();
                Context context = (Context)param.thisObject;
                monitorBroadcast(intent, origin, context, "sendStickyOrderedBroadcastAsUser");
            }
        });   */


         /*
        *  hook intent for activity.
        *   5 methods
        * */
        findAndHookMethod("android.app.Activity", lpparam.classLoader, 
                "startActivity", Intent.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Log.e("666", "hook Activity");
                Intent intent = (Intent) param.args[0];
                String origin = param.thisObject.getClass().getName();
                Context context = (Context)param.thisObject;
                monitorActivity(intent, origin, context, "startActivity");
            }
        });

        findAndHookMethod("android.content.ContextWrapper", lpparam.classLoader,
                "startActivity", Intent.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Log.e("6666", "hook Activity");
                Intent intent = (Intent) param.args[0];
                String origin = param.thisObject.getClass().getName();
                Context context = (Context)param.thisObject;
                monitorActivity(intent, origin, context, "startActivity");
            }
        });

        findAndHookMethod("android.content.ContextWrapper", lpparam.classLoader,
                "startActivity", Intent.class, Bundle.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Log.e("66666", "hook Activity");
                Intent intent = (Intent) param.args[0];
                String origin = param.thisObject.getClass().getName();
                Context context = (Context)param.thisObject;
                monitorActivity(intent, origin, context, "startActivity");
            }
        });

        findAndHookMethod("android.app.Activity", lpparam.classLoader,
                "startActivityForResult", Intent.class, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Log.e("66666", "hook startActivityForResult1");
                Intent intent = (Intent) param.args[0];
                String origin = param.thisObject.getClass().getName();
                Context context = (Context)param.thisObject;
                monitorActivity(intent, origin, context, "startActivityForResult");
            }
        });

        findAndHookMethod("android.app.Activity", lpparam.classLoader,
                "startActivityForResult", Intent.class, int.class, Bundle.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Log.e("66666", "hook startActivityForResult2");
                Intent intent = (Intent) param.args[0];
                String origin = param.thisObject.getClass().getName();
                Context context = (Context)param.thisObject;
                monitorActivity(intent, origin, context, "startActivityForResult");
            }
        });

        /*
        *  hook intent for service
        *   2  methods
        *
        * */

        findAndHookMethod("android.content.ContextWrapper", lpparam.classLoader,
                "startService", Intent.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Intent intent = (Intent) param.args[0];
                String origin = param.thisObject.getClass().getName();
                Context context = (Context)param.thisObject;
                monitorService(intent, origin, context, "startService");
            }
        });

        findAndHookMethod("android.content.ContextWrapper", lpparam.classLoader,
                "bindService", Intent.class, ServiceConnection.class, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Intent intent = (Intent) param.args[0];
                String origin = param.thisObject.getClass().getName();
                Context context = (Context)param.thisObject;
                monitorService(intent, origin, context, "bindService");
            }
        });


        findAndHookMethod("android.content.ContextWrapper", lpparam.classLoader,
                "sendBroadcast", Intent.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        Log.e("6666", "hook Broadcast");
                        Intent intent = (Intent) param.args[0];
                        String origin = param.thisObject.getClass().getName();
                        Context context = (Context)param.thisObject;
                        monitorBroadcast(intent, origin, context, "sendBroadcast");
                    }
                });

        findAndHookMethod("android.content.ContextWrapper", lpparam.classLoader,
                "sendBroadcast", Intent.class, String.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                        Log.e("6666", "hook Broadcast");
                        Intent intent = (Intent) param.args[0];
                        String origin = param.thisObject.getClass().getName();
                        Context context = (Context)param.thisObject;
                        monitorBroadcast(intent, origin, context, "sendBroadcast");
                    }
                });


    }


    private void monitorBroadcast(Intent intent, String origin, Context context, String method){
        Bundle intentExtras = intent.getExtras();
        if(DataUtil.hasExtras(intentExtras) && !intent.getAction().equals("com.huang.pmdroid.receive")
                && !origin.equals("com.android.launcher2.Launcher"))
        {
            //   Log.e(Constants.XPOSED_TAG, "successEnter");
            Long createAt = System.currentTimeMillis();
            //Log.e(Constants.XPOSED_TAG,""+ createAt);
            String dest = DataUtil.getPack(intent);
            //Log.e(Constants.XPOSED_TAG,origin + "   " + dest);
            String componentName = DataUtil.dumpComponentName(intent.getComponent());
            //Log.e(Constants.XPOSED_TAG,""+ componentName);
            String intentExtrasParser = DataUtil.intentExtrasParser(intentExtras);
            //Log.e(Constants.XPOSED_TAG,""+ intentExtrasParser);
            Record record = new Record(createAt,origin,dest,method,intent.getAction(),componentName,intentExtrasParser);
            Intent set_data = new Intent();                  //将数据传输给Service组件中
            set_data.setAction(MonitorService.ACTION);
            set_data.putExtra("intentData", record);
            context.sendBroadcast(set_data);
        }
        

    }

    private void monitorActivity(Intent intent, String origin, Context context, String method){
        Bundle intentExtras = intent.getExtras();
        if(DataUtil.hasExtras(intentExtras)){
     //       Log.e(Constants.XPOSED_TAG, "successEnter");
            Long createAt = System.currentTimeMillis();
     //       Log.e(Constants.XPOSED_TAG,""+ createAt);
            String dest = DataUtil.getPack(intent);
    //        Log.e(Constants.XPOSED_TAG,origin + "   " + dest);
            String componentName = DataUtil.dumpComponentName(intent.getComponent());
            //Log.e(Constants.XPOSED_TAG,""+ componentName);
            String intentExtrasParser = DataUtil.intentExtrasParser(intentExtras);
            //Log.e(Constants.XPOSED_TAG,""+ intentExtrasParser);
            Record record = new Record(createAt,origin,dest,method,intent.getAction(),componentName,intentExtrasParser);
            Intent set_data = new Intent();                  //将数据传输给Service组件中
            set_data.setAction(MonitorService.ACTION);
            set_data.putExtra("intentData", record);
            context.sendBroadcast(set_data);
        }
    }


    private void monitorService(Intent intent, String origin, Context context, String method){
        Bundle intentExtras = intent.getExtras();
        if(DataUtil.hasExtras(intentExtras)){
            Log.e(Constants.XPOSED_TAG, "successEnter");
            Long createAt = System.currentTimeMillis();
            Log.e(Constants.XPOSED_TAG,""+ createAt);
            String dest = DataUtil.getPack(intent);
            Log.e(Constants.XPOSED_TAG,origin + "   " + dest);
            String componentName = DataUtil.dumpComponentName(intent.getComponent());
            //Log.e(Constants.XPOSED_TAG,""+ componentName);
            String intentExtrasParser = DataUtil.intentExtrasParser(intentExtras);
            //Log.e(Constants.XPOSED_TAG,""+ intentExtrasParser);
            Record record = new Record(createAt,origin,dest,method,intent.getAction(),componentName,intentExtrasParser);
            Intent set_data = new Intent();                  //将数据传输给Service组件中
            set_data.setAction(MonitorService.ACTION);
            set_data.putExtra("intentData", record);
            context.sendBroadcast(set_data);
        }
    }
}
