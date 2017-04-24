package com.huang.pmdroid.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.BadParcelableException;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Set;
import org.apache.commons.io.FileUtils;




/**
 * Created by huang on 2017/4/14.
 */


public class DataUtil {

    private static final String FILE_PATH = "/data/data/com.huang.pmdroid/files/";

    private static final String DATA_PATH =
            "/data/data/com.huang.pmdroid/files/intent_data";

    public static void createFile(Context context) {
        if (Build.VERSION.SDK_INT >= 24) {
            return;
        }
        File file = new File(DATA_PATH);
        if (!file.exists()) {
            try {
                FileOutputStream stream =
                        context.openFileOutput("intent_data", Context.MODE_WORLD_WRITEABLE);
                stream.write(" ".getBytes());
                stream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String read() throws IOException {
        String str = "";
        try {
            str = FileUtils.readFileToString(new File(FILE_PATH, "intent_data"), "GBK");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "[" + str + "]";
    }

    public static void clear() throws IOException {
        FileUtils.write(new File(DATA_PATH), "", "GBK", false);
    }

    public static void write(String s) throws IOException {
        FileUtils.write(new File(DATA_PATH), s + "\n", "GBK", true);
    }

 /*   public static String parser(Intent intent, int requestCode, Bundle bundle, String from) {
        StringBuilder builder = new StringBuilder();

        Calendar c = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");

        builder.append("{")
                .append(String.format("\"time\": \"%s\",", simpleDateFormat.format(c.getTime())))
                .append(String.format("\"from\": \"%s\",", from))
                .append(String.format("\"to\": \"%s\",", intent.getComponent().getClassName()))
                .append(String.format("\"action\": \"%s\",", intent.getAction()))
                .append(String.format("\"clipData\": \"%s\",", intent.getClipData()))
                .append(String.format((Locale) null, "\"flags\": %d,", intent.getFlags()))
                .append(String.format("\"dataString\": \"%s\",", intent.getDataString()))
                .append(String.format("\"type\": \"%s\",", intent.getType()))
                .append(String.format("\"componentName\": \"%s\",", intent.getComponent()))
                .append(String.format("\"scheme\": \"%s\",", intent.getScheme()))
                .append(String.format("\"package\": \"%s\",", intent.getPackage()));

        Set<String> categories = intent.getCategories();
        if (categories != null) {
            builder.append("\"categories\" :[");
            boolean added = false;
            for (String s : categories) {
                if (added) {
                    builder.append(",");
                }
                builder.append(String.format("\"%s\"", s));
                added = true;
            }
            builder.append("],");
        }

        Bundle intentExtras = intent.getExtras();
        if (intentExtras != null) {
            builder.append("\"intentExtras\": [");
            boolean added = false;
            for (String key : intentExtras.keySet()) {
                Object value = intentExtras.get(key);
                if (added) {
                    builder.append(",");
                }
                builder.append(String.format("{\"key\": \"%s\",", key))
                        .append(String.format("\"value\": \"%s\",", value))
                        .append(String.format("\"class\": \"%s\"}", value.getClass().getName()));
                added = true;
            }
            builder.append("],");
        }
        if (bundle != null) {
            builder.append("\"bundle\": [");
            boolean added = false;
            for (String key : bundle.keySet()) {
                Object value = bundle.get(key);
                if (added) {
                    builder.append(",");
                }
                builder.append(String.format("{\"key\": \"%s\",", key))
                        .append(String.format("\"value\": \"%s\",", value));
                if (value != null) {
                    builder.append(String.format("\"class\": \"%s\"}", value.getClass().getName()));
                } else {
                    builder.append(String.format("\"class\": \"%s\"}", "unknown"));
                }
                added = true;
            }
            builder.append("],");
        }
        builder.append(String.format((Locale) null, "\"requestCode\": %d", requestCode));
        builder.append("},");
        return builder.toString();
    } */


    public static String dumpComponentName(ComponentName componentName) {
        if (componentName != null){
            Log.e("666", "Component: " + componentName.getPackageName() + "/" + componentName.getClassName());
            return componentName.getPackageName() + "/" + componentName.getClassName();
        }
        else
            return "null";
    }

    public static String getPackorToName(Intent intent){
        if (intent.getComponent() != null)
            return intent.getComponent().getPackageName();
        else if (intent.getAction() != null)
            return intent.getAction();
        else
            return "null";
    }

    public static String parser(String method, Intent intent, String from) {
        StringBuilder builder = new StringBuilder();
        Bundle intentExtras = intent.getExtras();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        builder.append("{")
                .append(String.format("\"method\": \"%s\",", method))
                .append(String.format("\"time\": \"%s\",", simpleDateFormat.format(c.getTime())))
                .append(String.format("\"from\": \"%s\",", from))
                .append(String.format("\"to\": \"%s\",", getPackorToName(intent)))
                .append(String.format("\"action\": \"%s\",", intent.getAction()))
                .append(String.format("\"clipData\": \"%s\",", intent.getClipData()))
                .append(String.format((Locale) null, "\"flags\": %d,", intent.getFlags()))
                .append(String.format("\"dataString\": \"%s\",", intent.getDataString()))
                .append(String.format("\"type\": \"%s\",", intent.getType()))
                .append(String.format("\"componentName\": \"%s\",", dumpComponentName(intent.getComponent())))
                .append(String.format("\"scheme\": \"%s\",", intent.getScheme()))
                .append(String.format("\"hasExtras\": \"%b\",", hasExtras(intentExtras)));

        Set<String> categories = intent.getCategories();
        if (categories != null) {
            builder.append("\"categories\" :[");
            boolean added = false;
            for (String s : categories) {
                if (added) {
                    builder.append(",");
                }
                builder.append(String.format("\"%s\"", s));
                added = true;
            }
            builder.append("],");
        }


        if (intentExtras != null) {
            builder.append("\"intentExtras\": [");
            boolean added = false;
            for (String key : intentExtras.keySet()) {
                Object value = intentExtras.get(key);
                if (added) {
                    builder.append(",");
                }
                builder.append(String.format("{\"key\": \"%s\",", key))
                        .append(String.format("\"value\": \"%s\",", value))
                        .append(String.format("\"class\": \"%s\"}", value.getClass().getName()));
                added = true;
            }
            builder.append("],");
        }
        builder.append(String.format("\"package\": \"%s\"", intent.getPackage()));
        builder.append("},");
        return builder.toString();
    }

    public static String intentExtrasParser(Bundle intentExtras){
        if(intentExtras != null){
            StringBuilder builder = new StringBuilder();
            builder.append("[");
            boolean added = false;
            for (String key : intentExtras.keySet()) {
                Object value = intentExtras.get(key);
                if (added) {
                    builder.append(",");
                }
                builder.append(String.format("{\"key\": \"%s\",", key))
                        .append(String.format("\"value\": \"%s\",", value))
                        .append(String.format("\"class\": \"%s\"}", value.getClass().getName()));
                added = true;
            }
            builder.append("]");
            return builder.toString();
        }
        else
            return "null";
    }

    public static boolean hasExtras(Bundle extras) {
        try {
            return (extras != null && !extras.isEmpty());
        } catch (BadParcelableException e) {
            Log.e("IntentLogger", "Extra contains unknown class instance: ", e);
            return true;
        }
    }

    public static String getDateFromTimestamp(long timestamp){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        return simpleDateFormat.format(timestamp);
    }
}