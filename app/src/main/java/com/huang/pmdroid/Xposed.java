package com.huang.pmdroid;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;



import java.io.IOException;
import java.util.List;
import java.util.Set;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import com.huang.pmdroid.utils.DataUtil;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by huang on 2017/4/13.
 */
public class Xposed implements IXposedHookLoadPackage{
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        findAndHookMethod("android.content.ContextWrapper", lpparam.classLoader,
                "sendBroadcast", Intent.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        Intent intent = (Intent) param.args[0];
                        Bundle intentExtras = intent.getExtras();
                        Log.e("777TF",""+ intent.getAction().equals("com.huang.pmdroid.receive"));
                        if(DataUtil.hasExtras(intentExtras) == true && intent.getAction().equals("com.huang.pmdroid.receive") != true)
                        {
                            Log.e("777", "successEnter");
                            String from = param.thisObject.getClass().getName();
                            Context context = (Context)param.thisObject;
                            Intent set_data = new Intent();                  //将数据传输给Service组件中
                            set_data.setAction("com.huang.pmdroid.receive");
                            set_data.putExtra("from", from);
                            set_data.putExtra("to",DataUtil.getPackorToName(intent));
                            context.sendBroadcast(set_data);
                        }

                      /*  if(DataUtil.hasExtras(intentExtras) == true){
                            // Log.e("666", intent.getAction());
                            IntentLogger.dump("dumpIntentsendBroadcast(666)", intent);
                            try{
                                //  Log.e("666", "from: "+ from);
                                DataUtil.write(DataUtil.parser("sendBroadcast", intent, param.thisObject.getClass().getPackage().getName()));
                            }catch (Exception e){
                                Log.e("666",""+ e);
                            }
                        } */
                    }
                });
    }
}
