package com.huang.pmdroid.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.huang.pmdroid.R;
import com.huang.pmdroid.models.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huang on 2017/4/26.
 *
 */
public class WhiteListAdapter extends RecyclerView.Adapter<WhiteListAdapter.WhiteListViewHolder> {
    private Context context;
    private List<AppInfo> list = new ArrayList<>();

    public interface OnButtonClickListener{
        void onButtonClick(int position);
    }

    private OnButtonClickListener onButtonClickListener;

    public void setOnButtonClickListener(OnButtonClickListener  onButtonClickListener){
        this.onButtonClickListener = onButtonClickListener;
    }


    public WhiteListAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getItemCount(){
        return list.size();
    }

    public void setData(List<AppInfo> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public void clearData(){
        int count = list.size();
        list.clear();
        notifyItemRangeRemoved(0, count);
    }

    public void removeData(int position){
        list.remove(position);
        notifyItemRemoved(position);
        //防止position混乱
        notifyItemRangeChanged(position, list.size());
    }

    @Override
    public WhiteListViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        return new WhiteListViewHolder(LayoutInflater.from(context).inflate(R.layout.item_whitelist, parent, false));
    }

    @Override
    public void onBindViewHolder(final WhiteListViewHolder holder, final int position){
        holder.tvAppName.setText(list.get(position).getAppName());
        holder.icon.setImageDrawable(list.get(position).getAppIcon());
        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClickListener.onButtonClick(position);
            }
        });
    }

    class WhiteListViewHolder extends RecyclerView.ViewHolder{
        private TextView tvAppName;
        private Button btnRemove;
        private ImageView icon;
        public WhiteListViewHolder(View itemView){
            super(itemView);
            tvAppName = (TextView)itemView.findViewById(R.id.tv_whiteList_name);
            icon = (ImageView) itemView.findViewById(R.id.whiteList_icon);
            btnRemove = (Button) itemView.findViewById(R.id.btn_remove_whiteList);
        }
    }
}
