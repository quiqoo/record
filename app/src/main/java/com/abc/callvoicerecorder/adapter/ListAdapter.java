package com.abc.callvoicerecorder.adapter;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.abc.callvoicerecorder.R;
import com.abc.callvoicerecorder.db.Record;
import com.abc.callvoicerecorder.constant.ConstantsColor;
import com.abc.callvoicerecorder.utils.TimeUtils;


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> implements ConstantsColor {
    private final OnClickListener onclick;
    private final int displayHeight;
    private List<Record> itemsList;
    private Context context;
    private int displayWidth;
    private Drawable iconBitmap;
    private ApplicationInfo ai;
    private View viewHeader;


    public interface OnClickListener{
        void onClick(Record dictaphoneRecordItem, View view, int position);
    }

    public ListAdapter(Context context, OnClickListener onclcik) {
        this.onclick = onclcik;
        this.itemsList = new ArrayList<>();
        this.context = context;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        displayWidth = display.getWidth();
        displayHeight = display.getHeight();
    }

    public void setLists(List<Record> itemsList){
        this.itemsList = new ArrayList<>();
        this.itemsList.addAll(itemsList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dc_list_, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final View view = holder.view;
        final TextView callName = (TextView) view.findViewById(R.id.call_name);
        final TextView callTime = (TextView) view.findViewById(R.id.call_time);

        callName.setTextColor(context.getResources().getColor(R.color.color_list_text_color));
        callTime.setTextColor(context.getResources().getColor(R.color.color_list_text_color));
        callName.setText(itemsList.get(position).getRecordName());

        try {
            MediaPlayer mp = MediaPlayer.create(context, Uri.parse(itemsList.get(position).getPath()));
            int duration = mp.getDuration();
            callTime.setText(TimeUtils.getTimeToString(duration));
        } catch (Exception e) {
            e.printStackTrace();
            callTime.setText("00:00");
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onclick.onClick(itemsList.get(position), view, position);
            }
        });
        (view.findViewById(R.id.root_item_layout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onclick.onClick(itemsList.get(position), view.findViewById(R.id.root_item_layout), position);
            }
        });
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        if (itemsList != null)
            return itemsList.size();
        else
            return 0;
    }

    public void setNewElements(List<Record> newElements) {
        itemsList.addAll(new ArrayList<Record>(newElements));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }


}

