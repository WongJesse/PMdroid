package com.huang.pmdroid.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteReadOnlyDatabaseException;
import android.support.design.widget.TabLayout;
import android.util.Log;

import com.huang.pmdroid.MainActivity;
import com.huang.pmdroid.models.Record;
import com.huang.pmdroid.utils.Constants;

/**
 * Created by huang on 2017/4/18.
 */
public class DbHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = Constants.DATABASE_NAME;
    private static final int DATABASE_VERSION = 1000;

    //拦截记录表名与属性名
    public static final String TABLE_RECORDS = "records";
    public static final String KEY_RECORD_ID = "record_id";
    public static final String KEY_RECORD_TIME = "time";
    public static final String KEY_RECORD_ORIGIN = "origin";
    public static final String KEY_RECORD_DEST = "dest";
    public static final String KEY_RECORD_METHOD = "method";
    public static final String KEY_RECORD_ACTION = "action";
    public static final String KEY_REDCORD_COMPONENT_NAME = "component_name";
    public static final String KEY_RECORD_INTENT_EXTRAS = "intent_extras";

    public static final String CREATE_RECORDS = "create table " + TABLE_RECORDS + " ("
            + KEY_RECORD_ID + " integer primary key autoincrement, "
            + KEY_RECORD_TIME + " integer, "
            + KEY_RECORD_ORIGIN + " text, "
            + KEY_RECORD_DEST + " text, "
            + KEY_RECORD_METHOD + " text, "
            + KEY_RECORD_ACTION + " text, "
            + KEY_REDCORD_COMPONENT_NAME + " text, "
            + KEY_RECORD_INTENT_EXTRAS + " text)";
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
            Log.i(Constants.TAG, CREATE_RECORDS);
        }catch (Exception e){
            Log.e(Constants.TAG, ""+ e);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
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
        values.put(KEY_REDCORD_COMPONENT_NAME, record.getComponentName());
        values.put(KEY_RECORD_INTENT_EXTRAS, record.getIntentExtras());
        db.insert(TABLE_RECORDS, null, values);
        values.clear();
        Log.i(Constants.TAG, "insert successfully!");
    }

    public void clearAllFromRecords(){
        SQLiteDatabase db = getDatabase(true);
        db.delete(TABLE_RECORDS, null, null);
    }

    public void deleteRecord(int keyId){
        SQLiteDatabase db = getDatabase(true);
        db.delete(TABLE_RECORDS, "record_id=?", new String[]{keyId+""});
    }
}
