package com.huang.pmdroid.adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.huang.pmdroid.R;
import com.huang.pmdroid.models.Record;
import com.huang.pmdroid.utils.DataUtil;
import com.huang.pmdroid.utils.HtmlBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huang on 2017/4/22.
 *
 */
public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder>{
    private Context context;
    private List<Record> list = new ArrayList<>();

    /**
     * 点击事件接口
     * */
    public interface OnItemClickListener{
        void onItemClick(View view,int position);
        void onItemLongClick(View view,int position);
    }
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public RecordAdapter(Context context){this.context = context;}

    @Override
    public RecordViewHolder onCreateViewHolder( ViewGroup parent, int viewType){
        return new RecordViewHolder(LayoutInflater.from(context).inflate(R.layout.item_record, parent, false));
    }

    public void setData(List<Record> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public Record getRecord(int pos){
        return list.get(pos);
    }

    public void clearData(){
        int count = list.size();
        list.clear();
        notifyItemRangeRemoved(0, count);
    }



    @Override
    public int getItemCount(){
        return list.size();
    }

    @Override
    public void onBindViewHolder(final RecordViewHolder holder, final int position){
  //      holder.itemView.setOnClickListener(new RecordItemOnClickListener(context, list.get(position)));
        holder.selectedRecord(list.get(position));
        HtmlBuilder htmlBuilder = new HtmlBuilder();
        htmlBuilder.newLine(1)
                .appendBold("time: ").appendText(DataUtil.getDateFromTimestamp(list.get(position).getCreatedAt())).newLine(2)
                .appendBold("from: ").appendText(list.get(position).getOrigin()).newLine(2)
                .appendBold("to: ").appendText(list.get(position).getDest()).newLine(1);
        holder.tv.setText(htmlBuilder.build());
        if (onItemClickListener!=null){
            //可以获得每个item的包装类itemView
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(v,position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemClickListener.onItemLongClick(v,position);
                    return true;
                }
            });
        }

    }

    class RecordViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        public RecordViewHolder(View itemView){
            super(itemView);
            tv = (TextView)itemView.findViewById(R.id.tv_item_record);
        }
        public void selectedRecord(Record item){
            if(item.getIsSelected()){
               // itemView.setBackground(context.getResources().getDrawable(R.drawable.item_selected));
                ContextCompat.getDrawable(context,R.drawable.item_selected);
            }else{
                ContextCompat.getDrawable(context,R.drawable.item_common);
                //itemView.setBackground(context.getResources().getDrawable(R.drawable.item_common));
            }

        }
    }

    //弃用，点击事件回调到活动中
    public static class RecordItemOnClickListener implements View.OnClickListener{
        private Context context;
        private Record record;
        TextView tvRecordDialog;
        public RecordItemOnClickListener(Context context, Record record){
            this.context = context;
            this.record = record;
        }
        @Override
        public void onClick(View v){
            Dialog dialog = new Dialog(context);
            View view = View.inflate(context, R.layout.dialog_record_detail, null);
            tvRecordDialog = (TextView)view.findViewById(R.id.tv_dialog_record);

            HtmlBuilder htmlBuilder = new HtmlBuilder();
            htmlBuilder.newLine()
                    .appendBold("method: ").appendText(record.getMethod()).newLine(2)
                    .appendBold("time: ").appendText(DataUtil.getDateFromTimestamp(record.getCreatedAt())).newLine(2)
                    .appendBold("from: ").appendText(record.getOrigin()).newLine(2)
                    .appendBold("to: ").appendText(record.getDest()).newLine(2)
                    .appendBold("action: ").appendText(record.getAction()).newLine(2)
                    .appendBold("componentName: ").appendText(record.getComponentName())
                    .newLine(2);
         //           .appendBold("intentExtras: ").appendText(record.getIntentExtras())
         //         .newLine(1);


            htmlBuilder.appendBold("intentExtras: ");
            if (record.getIntentExtras().equals("null")) {
                htmlBuilder.appendText("null");
            }
            else {
                try{
                    htmlBuilder.appendText("[").newLine();
                    JSONArray jsonArray = new JSONArray(record.getIntentExtras());
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        htmlBuilder
                                .appendTab(1).appendText("{").newLine()
                                .appendTab(2).appendText("key: ").appendText(jsonObject.getString("key")).newLine()
                                .appendTab(2).appendText("value: ").appendText(jsonObject.getString("value"))
                                .newLine()
                                .appendTab(2).appendText("class: ").appendText(jsonObject.getString("class"))
                                .newLine()
                                .appendTab(1).appendText("},").newLine();
                    }
                    htmlBuilder.appendText("]");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            htmlBuilder.newLine();

            tvRecordDialog.setText(htmlBuilder.build());
            dialog.setContentView(view);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT);
            }
            dialog.setTitle(R.string.dialog_detail_info);
            dialog.show();
        }
    }
}
