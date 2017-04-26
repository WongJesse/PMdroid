package com.huang.pmdroid.adapters;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.huang.pmdroid.R;
import com.huang.pmdroid.models.AppInfo;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huang on 2017/4/25.
 */
public class AppInfoAdapter extends RecyclerView.Adapter<AppInfoAdapter.AppInfoViewHolder>{
    private List<AppInfo> list = new ArrayList<>();
    private Context context;
    private boolean[] flag;
 //   private Map<Integer, Boolean> map = new HashMap<>();

    public interface OnCheckBoxClickListener{
        void onCheckBoxClick(CompoundButton buttonView, boolean isChecked,int position);
    }

    private OnCheckBoxClickListener onCheckBoxClickListener;

    public void setOnCheckBoxClickListener(OnCheckBoxClickListener onCheckBoxClickListener){
        this.onCheckBoxClickListener = onCheckBoxClickListener;
    }

    public AppInfoAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getItemCount(){
        return list.size();
    }

    public void setData(List<AppInfo> list){
        this.list = list;
  //      initMap();
        flag = new boolean[list.size()];
        notifyDataSetChanged();
    }

 /*   private void initMap(){
        for(int i = 0; i < list.size(); i++){
            map.put(i, false);
        }
    }
    public Map<Integer, Boolean> getMap(){
        return map;
    }  */

    @Override
    public AppInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        return new AppInfoViewHolder(LayoutInflater.from(context).inflate(R.layout.item_add_whitelist, parent, false));
    }

    @Override
    public void onBindViewHolder(final AppInfoViewHolder holder, final int position){
        holder.tvAppName.setText(list.get(position).getAppName());
        holder.icon.setImageDrawable(list.get(position).getAppIcon());

        //解决checkbox混乱问题
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(flag[position]);
        //checkbox通过回调处理
   /*     holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                map.put(position, isChecked);

            }
        });  */
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                flag[position] = isChecked;
                onCheckBoxClickListener.onCheckBoxClick(buttonView, isChecked, position);
            }
        });
    }

    class AppInfoViewHolder extends RecyclerView.ViewHolder{
        private TextView tvAppName;
        private CheckBox checkBox;
        private ImageView icon;

        public AppInfoViewHolder(View itemView){
            super(itemView);
            tvAppName = (TextView) itemView.findViewById(R.id.tv_app_name);
            checkBox = (CheckBox) itemView.findViewById(R.id.cb_add_whiteList);
            icon = (ImageView) itemView.findViewById(R.id.app_icon);
        }
    }

}
