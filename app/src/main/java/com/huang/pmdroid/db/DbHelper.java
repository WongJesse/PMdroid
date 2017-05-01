package com.huang.pmdroid.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteReadOnlyDatabaseException;
import android.util.Log;
import com.huang.pmdroid.models.AppInfo;
import com.huang.pmdroid.models.Record;
import com.huang.pmdroid.utils.Constants;
import com.huang.pmdroid.utils.ImageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huang on 2017/4/18.
 *
 */
public class DbHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = Constants.DATABASE_NAME;
    private static final int DATABASE_VERSION = 1001;

    //拦截记录表名与属性名
    public static final String TABLE_RECORDS = "records";
    public static final String KEY_RECORD_ID = "record_id";
    public static final String KEY_RECORD_TIME = "time";
    public static final String KEY_RECORD_ORIGIN = "origin";
    public static final String KEY_RECORD_DEST = "dest";
    public static final String KEY_RECORD_METHOD = "method";
    public static final String KEY_RECORD_ACTION = "action";
    public static final String KEY_RECORD_COMPONENT_NAME = "component_name";
    public static final String KEY_RECORD_INTENT_EXTRAS = "intent_extras";

    //白名单表名与属性名  --数据库升级版本1000---1001
    public static final String TABLE_WHITELIST = "whiteList";
    public static final String KEY_APP_ID = "app_id";
    public static final String KEY_APP_PACKNAME = "pack_name";
    public static final String KEY_APP_APPNAME = "app_name";
    public static final String KEY_APP_ICON = "app_icon";

    public static final String CREATE_RECORDS = "create table " + TABLE_RECORDS + " ("
            + KEY_RECORD_ID + " integer primary key autoincrement, "
            + KEY_RECORD_TIME + " integer, "
            + KEY_RECORD_ORIGIN + " text, "
            + KEY_RECORD_DEST + " text, "
            + KEY_RECORD_METHOD + " text, "
            + KEY_RECORD_ACTION + " text, "
            + KEY_RECORD_COMPONENT_NAME + " text, "
            + KEY_RECORD_INTENT_EXTRAS + " text)";

    public static final String CREATE_WHITELIST = "create table " + TABLE_WHITELIST + " ("
            + KEY_APP_ID + " integer primary key autoincrement, "
            + KEY_APP_PACKNAME + " text, "
            + KEY_APP_APPNAME + " text, "
            + KEY_APP_ICON + " blob)";

    private Context mContext;
    private static DbHelper instance = null;
    private SQLiteDatabase db;

    public static synchronized DbHelper getInstance(Context context){
        if(instance == null){
            instance = new DbHelper(context);
        }
        return instance;
    }

    public DbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        try{
            db.execSQL(CREATE_RECORDS);
            db.execSQL(CREATE_WHITELIST);
            Log.i(Constants.TAG, CREATE_WHITELIST+"onCreate");
        }catch (Exception e){
            Log.e(Constants.TAG, ""+ e);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion){
            case 1000:
                db.execSQL(CREATE_WHITELIST);
                Log.i(Constants.TAG, CREATE_WHITELIST + "onUpdgrade");
            default:
        }
    }


    public SQLiteDatabase getDatabase() {
        return getDatabase(false);
    }

    public SQLiteDatabase getDatabase(boolean forceWritable) {
        try {
            SQLiteDatabase db = getReadableDatabase();
            if (forceWritable && db.isReadOnly()) {
                throw new SQLiteReadOnlyDatabaseException("Required writable database, obtained read-only");
            }
            return db;
        } catch (IllegalStateException e) {
            return this.db;
        }
    }

    public void insertRecord(Record record){
        SQLiteDatabase db = getDatabase(true);
        ContentValues values = new ContentValues();
        values.put(KEY_RECORD_TIME, record.getCreatedAt());
        values.put(KEY_RECORD_ORIGIN, record.getOrigin());
        values.put(KEY_RECORD_DEST, record.getDest());
        values.put(KEY_RECORD_METHOD, record.getMethod());
        values.put(KEY_RECORD_ACTION, record.getAction());
        values.put(KEY_RECORD_COMPONENT_NAME, record.getComponentName());
        values.put(KEY_RECORD_INTENT_EXTRAS, record.getIntentExtras());
        db.insert(TABLE_RECORDS, null, values);
        values.clear();
        Log.i(Constants.TAG, "insert record successfully!");
    }

    public List<Record> getAllRecords(){
        List<Record> mResultlist = new ArrayList<>();
        Cursor cursor = getDatabase(true).query(TABLE_RECORDS, null, null, null, null, null, null);
        if(cursor != null && cursor.getCount() >= 0){
            cursor.moveToLast();
            for(int i = cursor.getCount() - 1; i >= 0; i--){
                cursor.moveToPosition(i);
                Record record = new Record();
                record.setKeyId(cursor.getInt(cursor.getColumnIndex(KEY_RECORD_ID)));
                record.setCreatedAt(cursor.getLong(cursor.getColumnIndex(KEY_RECORD_TIME)));
                record.setOrigin(cursor.getString(cursor.getColumnIndex(KEY_RECORD_ORIGIN)));
                record.setDest(cursor.getString(cursor.getColumnIndex(KEY_RECORD_DEST)));
                record.setMethod(cursor.getString(cursor.getColumnIndex(KEY_RECORD_METHOD)));
                record.setAction(cursor.getString(cursor.getColumnIndex(KEY_RECORD_ACTION)));
                record.setComponentName(cursor.getString(cursor.getColumnIndex(KEY_RECORD_COMPONENT_NAME)));
                record.setIntentExtras(cursor.getString(cursor.getColumnIndex(KEY_RECORD_INTENT_EXTRAS)));
                mResultlist.add(record);
            }
            cursor.close();
        }
        return mResultlist;

    }


    public void clearAllFromRecords(){
        SQLiteDatabase db = getDatabase(true);
        db.delete(TABLE_RECORDS, null, null);
    }

    public void deleteRecord(int keyId){
        SQLiteDatabase db = getDatabase(true);
        db.delete(TABLE_RECORDS, "record_id=?", new String[]{keyId + ""});
    }

    public void insertWhiteList(AppInfo appInfo) {
        SQLiteDatabase db = getDatabase(true);
        ContentValues values = new ContentValues();
        values.put(KEY_APP_PACKNAME, appInfo.getPackName());
        values.put(KEY_APP_APPNAME, appInfo.getAppName());
        values.put(KEY_APP_ICON, ImageUtil.drawableToBitmap(appInfo.getAppIcon()));
        db.insert(TABLE_WHITELIST, null, values);
        values.clear();
        Log.i(Constants.TAG, "insert whiteList successfully!");
    }

    public List<AppInfo> getAllAppInfo(){
        List<AppInfo> appInfos = new ArrayList<>();
        Cursor cursor = getDatabase(true).query(TABLE_WHITELIST, null, null, null, null, null, null);
        if(cursor != null && cursor.getCount() >= 0){
            cursor.moveToLast();
            for(int i = cursor.getCount() - 1; i >= 0; i--){
                cursor.moveToPosition(i);
                AppInfo appInfo = new AppInfo();
                appInfo.setPackName(cursor.getString(cursor.getColumnIndex(KEY_APP_PACKNAME)));
                appInfo.setAppName(cursor.getString(cursor.getColumnIndex(KEY_APP_APPNAME)));
                appInfo.setAppIcon(ImageUtil.bitmapToDrawable(mContext, cursor.getBlob(cursor.getColumnIndex(KEY_APP_ICON))));
                appInfos.add(appInfo);
            }
            cursor.close();
        }
        return appInfos;
    }

    public List<String> getPackNameFromWhiteList(){
        List<String> packNames = new ArrayList<>();
        String[] selectionArgs = new String[] {KEY_APP_PACKNAME};
        Cursor cursor = getDatabase(true).query(TABLE_WHITELIST, selectionArgs, null, null, null, null, null);
        if(cursor != null && cursor.getCount() >= 0){
            cursor.moveToLast();
            for(int i = cursor.getCount() - 1; i >= 0; i--){
                cursor.moveToPosition(i);
                String packName = cursor.getString(cursor.getColumnIndex(KEY_APP_PACKNAME));
                packNames.add(packName);
            }
            cursor.close();
        }
        return packNames;
    }

    public void deleteWhiteList(String packName){
        SQLiteDatabase db = getDatabase(true);
        db.delete(TABLE_WHITELIST, KEY_APP_PACKNAME+"=?", new String[]{packName+""});
    }

    public boolean checkPackNameExist(String packName){
        String sql = KEY_APP_PACKNAME + " = " + "'" + packName + "'";
        Cursor cursor = getDatabase(true).query(TABLE_WHITELIST, new String[]{KEY_APP_PACKNAME}, sql, null, null, null, null);
        if(cursor != null && cursor.getCount() == 1){
            cursor.close();
            return true;
        }
        else{
            if(cursor != null){
                cursor.close();
            }
            return false;
        }
    }

    public void clearAllFromWhiteList(){
        SQLiteDatabase db = getDatabase(true);
        db.delete(TABLE_WHITELIST, null, null);
    }
}
