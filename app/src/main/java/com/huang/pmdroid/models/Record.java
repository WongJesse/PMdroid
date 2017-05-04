package com.huang.pmdroid.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by huang on 2017/4/19.
 *
 */
//record实体类，实现序列化接口
public class Record implements Parcelable{
    private long createdAt;  //截获的时间
    private String origin;
    private String originPermission;  //被检测到的敏感权限
    private String dest;
    private String destPermission;
    private String method;
    private String action;
    private String componentName;
    private String intentExtras;
    private int keyId; //records数据库自增主键，用于删除指定的数据
    private boolean isSelected = false;

    public Record(){

    }

    public Record(long createdAt, String origin, String dest, String method, String action, String componentName, String intentExtras){
        this.createdAt = createdAt;
        this.origin = origin;
        this.dest = dest;
        this.method = method;
        this.action = action;
        this.componentName = componentName;
        this.intentExtras = intentExtras;
    }

    public String getOriginPermission(){return originPermission;}
    public void setOriginPermission(String originPermission){
        this.originPermission = originPermission;
    }

    public String getDestPermission(){return destPermission;}
    public void setDestPermission(String destPermission){
         this.destPermission = destPermission;
    }

    public int getKeyId(){return keyId;}
    public void setKeyId(int keyId){
        this.keyId = keyId;
    }

    public boolean getIsSelected(){return isSelected;}
    public void setIsSelected(boolean isSelected){
        this.isSelected = isSelected;
    }

    public long getCreatedAt(){
        return createdAt;
    }
    public void setCreatedAt(long createdAt){
        this.createdAt = createdAt;
    }

    public String getOrigin(){
        return origin;
    }
    public void setOrigin(String origin){
        this.origin = origin;
    }

    public String getDest(){
        return dest;
    }
    public void setDest(String dest){
        this.dest = dest;
    }

    public String getMethod(){
        return method;
    }
    public void setMethod(String method){
        this.method = method;
    }

    public String getAction(){
        return action;
    }
    public void setAction(String action){
        this.action = action;
    }

    public String getComponentName(){
        return componentName;
    }
    public void setComponentName(String componentName){
        this.componentName = componentName;
    }

    public String getIntentExtras(){
        return intentExtras;
    }

    public void setIntentExtras(String intentExtras){
        this.intentExtras = intentExtras;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(getCreatedAt());
        parcel.writeString(getOrigin());
        parcel.writeString(getDest());
        parcel.writeString(getMethod());
        parcel.writeString(getAction());
        parcel.writeString(getComponentName());
        parcel.writeString(getIntentExtras());
    }

    /*
	 * Parcelable interface must also have a static field called CREATOR, which is an object implementing the
	 * Parcelable.Creator interface. Used to un-marshal or de-serialize object from Parcel.
	 */
    public static final Parcelable.Creator<Record> CREATOR = new Parcelable.Creator<Record>() {
        @Override
        public Record[] newArray(int size){
            return new Record[size];
        }

        @Override
        public Record createFromParcel(Parcel in){
            return new Record(in);
        }
    };

    public Record(Parcel in){
        createdAt = in.readLong();
        origin = in.readString();
        dest = in.readString();
        method = in.readString();
        action = in.readString();
        componentName = in.readString();
        intentExtras = in.readString();
    }

}
