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
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.abc.callvoicerecorder.R;
import com.abc.callvoicerecorder.db.Record;
import com.abc.callvoicerecorder.constant.Constants;
import com.abc.callvoicerecorder.utils.SharedPreferencesFile;
import com.abc.callvoicerecorder.utils.TimeUtils;


public class DeleteAllListDicAdapter extends RecyclerView.Adapter<DeleteAllListDicAdapter.ViewHolder> implements Constants {
    private final OnClickListener onclick;
    private final int displayHeight;
    private List<Record> itemsList;
    private Context context;
    private int displayWidth;
    private Drawable iconBitmap;
    private ApplicationInfo ai;
    private View viewHeader;

    boolean[] checked;


    public interface OnClickListener{
        void onClick(Record callRecordItem, View view, int position);
    }

    public DeleteAllListDicAdapter(Context context, OnClickListener onclcik) {
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
        checked = new boolean[itemsList.size()];
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_delete_dc, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        SharedPreferencesFile.initSharedReferences(context);
        final View view = holder.view;
        final TextView callName = (TextView) view.findViewById(R.id.call_name);
        final TextView callTime = (TextView) view.findViewById(R.id.call_time);
        final CheckBox deleteCheckBox = (CheckBox) view.findViewById(R.id.delete_checkbox);

        callName.setTextColor(context.getResources().getColor(R.color.color_list_text_color));
        callTime.setTextColor(context.getResources().getColor(R.color.color_list_text_color));

        callName.setText( itemsList.get(position).getRecordName());
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

        holder.mCheckBox.setChecked(checked[position]);
        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checked[position] = !checked[position];
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
        private CheckBox mCheckBox;

        public ViewHolder(View v) {
            super(v);
            view = v;
            mCheckBox = (CheckBox) v.findViewById(R.id.delete_checkbox);
        }
    }

    public boolean[] getChecked() {
        return checked;
    }

    public List<Record> getCheckedList() {
        List<Record> itemsListReturned = new ArrayList<>();
        for (int i = 0; i < itemsList.size(); i++) {
            if (checked[i])
                itemsListReturned.add(itemsList.get(i));
        }

        return itemsListReturned;
    }

    public void setCheckAllCheckBox(boolean isChecked) {
        for (int i = 0; i < itemsList.size(); i++) {
            checked[i] = isChecked;
        }
        notifyDataSetChanged();
    }

}

