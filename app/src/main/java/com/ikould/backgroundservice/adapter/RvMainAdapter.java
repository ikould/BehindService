package com.ikould.backgroundservice.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ikould.backgroundservice.CoreApplication;
import com.ikould.backgroundservice.R;
import com.ikould.backgroundservice.data.AppData;
import com.ikould.frame.widget.StatusButton;

import java.util.List;

/**
 * RecyclerView的Adapter
 * <p>
 * Created by liudong on 2016/6/29.
 */
public class RvMainAdapter extends RecyclerView.Adapter<RvMainAdapter.RecyclerViewHolder> {
    private List<AppData> datas;
    private Context mContext;
    private RecycleListener recycleListener;
    private String tag;

    /**
     * TAG
     */
    private static final String SHOW_CHECK = "SHOW_CHECK";


    public RvMainAdapter(Context context, List<AppData> datas, RecycleListener recycleListener, String tag) {
        this.datas = datas;
        this.mContext = context;
        this.recycleListener = recycleListener;
        this.tag = tag;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(LayoutInflater
                .from(mContext)
                .inflate(R.layout.item_rvmain, parent, false));
    }


    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        AppData appData = datas.get(position);
        holder.itemImg.setBackgroundDrawable(appData.getAppIcon());
        String textTemp = appData.getAppName() + "(" + appData.getAppPackageName() + ")";
        holder.itemName.setText(textTemp);
        if (tag != null && SHOW_CHECK.equals(tag)) {
            holder.itemChecked.setVisibility(View.GONE);
        } else {
            holder.itemChecked.setChecked(CoreApplication.getInstance().saveApps.contains(appData.getAppPackageName()));
        }
        holder.itemChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemChecked.toggle();
                recycleListener.onItemClickListener(v, position, holder.itemChecked.isChecked());
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImg;
        TextView itemName;
        LinearLayout ll_rvmain;
        StatusButton itemChecked;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            ll_rvmain = (LinearLayout) itemView.findViewById(R.id.ll_rvmain);
            itemImg = (ImageView) itemView.findViewById(R.id.iv_rvitem_icon);
            itemName = (TextView) itemView.findViewById(R.id.tv_rvitem_name);
            itemChecked = (StatusButton) itemView.findViewById(R.id.sb_rvitem_checkd);
        }
    }

    public interface RecycleListener {
        void onItemClickListener(View v, int position, boolean isChecked);
    }
}
