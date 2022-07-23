package com.abc.callvoicerecorder.adapter;

import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.abc.callvoicerecorder.helper.ContactsHelper;
import com.abc.callvoicerecorder.helper.PaintIconsHelper;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.abc.callvoicerecorder.R;
import com.abc.callvoicerecorder.db.CallRecord;
import com.abc.callvoicerecorder.constant.Constants;
import com.abc.callvoicerecorder.constant.ConstantsColor;
import com.abc.callvoicerecorder.utils.RoundedCornersTransformation;
import com.abc.callvoicerecorder.utils.SharedPreferencesFile;
import com.abc.callvoicerecorder.utils.TimeUtils;



public class MainCallAllAdapter extends RecyclerView.Adapter<MainCallAllAdapter.ViewHolder> implements Constants, ConstantsColor {
    private final OnClickListener onclick;
    private final int displayHeight;
    private List<CallRecord> itemsList;
    private Context context;
    private int displayWidth;
    private Drawable iconBitmap;
    private ApplicationInfo ai;
    private View viewHeader;


    public interface OnClickListener{
        void onClick(CallRecord callRecordItem, View view, int position);
    }

    public MainCallAllAdapter(Context context, OnClickListener onclcik) {
        this.onclick = onclcik;
        this.itemsList = new ArrayList<>();
        this.context = context;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        displayWidth = display.getWidth();
        displayHeight = display.getHeight();
    }

    public void setLists(List<CallRecord> itemsList){
        this.itemsList = new ArrayList<>();
        this.itemsList.addAll(itemsList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_callrecord, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        SharedPreferencesFile.initSharedReferences(context);
        final View view = holder.view;
        if (itemsList.get(position).getCallType() != 999) {
            (view.findViewById(R.id.title_item_layout)).setVisibility(View.GONE);
            (view.findViewById(R.id.main_item_layout)).setVisibility(View.VISIBLE);
            final TextView callName = (TextView) view.findViewById(R.id.call_name);
            final TextView callTime = (TextView) view.findViewById(R.id.call_time);
            final TextView callDuration = (TextView) view.findViewById(R.id.duration_text);
            final ImageView callContactPhoto = (ImageView) view.findViewById(R.id.img_call_contact_photo);

            callName.setTextColor(context.getResources().getColor(R.color.color_list_text_color));
            callTime.setTextColor(context.getResources().getColor(R.color.color_list_text_desc_color));
            ((ImageView) view.findViewById(R.id.img_dots)).setImageDrawable(PaintIconsHelper.recolorIcon(context, R.drawable.item_dots, COLOR_ICONS[SharedPreferencesFile.getThemeNumber()]));


            if (itemsList.get(position).getCallType() == CALL_TYPE_INCOMING)
                ((ImageView) view.findViewById(R.id.img_call_type)).setImageResource(R.drawable.item_outgoing_call);
            else
                ((ImageView) view.findViewById(R.id.img_call_type)).setImageResource(R.drawable.item_incoming_call);

            ((ImageView) view.findViewById(R.id.imageView2)).setImageResource(MAIN_WINDOW_CONTACT_DESC[SharedPreferencesFile.getThemeNumber()]);
            String contactName = ContactsHelper.getContactName(itemsList.get(position).getCallNumber(), context);
            if (contactName.equals(""))
                contactName = itemsList.get(position).getCallNumber();
            callName.setText(contactName);
            callTime.setText(itemsList.get(position).getCallTime());

            if (itemsList.get(position).getDesc() != null && itemsList.get(position).getDescTitle() != null &&
                    (!itemsList.get(position).getDesc().equals("") || !itemsList.get(position).getDescTitle().equals(""))) {
                (view.findViewById(R.id.imageView2)).setVisibility(View.VISIBLE);
            } else {
                (view.findViewById(R.id.imageView2)).setVisibility(View.GONE);
            }

            String contactPhoto = ContactsHelper.getContactPhoto(itemsList.get(position).getCallNumber(), context);
            if (contactPhoto.equals(""))
                Picasso.get().load(R.drawable.item_contact_icon).into(callContactPhoto);
            else
                Picasso.get().load(contactPhoto).error(R.drawable.item_contact_icon).transform(new RoundedCornersTransformation()).into(callContactPhoto);

            try {
                MediaPlayer mp = MediaPlayer.create(context, Uri.parse(itemsList.get(position).getPath()));
                int duration = mp.getDuration();
                callDuration.setText(TimeUtils.getTimeToString(duration));
            } catch (Exception e) {
                e.printStackTrace();
                callDuration.setText("00:00");
            }


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onclick.onClick(itemsList.get(position), view, position);
                }
            });

            (view.findViewById(R.id.dots_layout)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onclick.onClick(itemsList.get(position), view.findViewById(R.id.dots_layout), position);
                }
            });
            (view.findViewById(R.id.root_item_layout)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onclick.onClick(itemsList.get(position), view.findViewById(R.id.root_item_layout), position);
                }
            });
        } else {
            (view.findViewById(R.id.title_item_layout)).setVisibility(View.VISIBLE);
            (view.findViewById(R.id.main_item_layout)).setVisibility(View.GONE);
            final TextView titleText = (TextView) view.findViewById(R.id.title_item_text);


            Calendar timeC = Calendar.getInstance();
            timeC.set(Calendar.HOUR_OF_DAY, 0);
            timeC.set(Calendar.MINUTE, 0);
            timeC.set(Calendar.SECOND, 0);
            timeC.set(Calendar.MILLISECOND, 0);

            Calendar yesterdayC = Calendar.getInstance();
            yesterdayC.set(Calendar.HOUR_OF_DAY, 0);
            yesterdayC.set(Calendar.MINUTE, 0);
            yesterdayC.set(Calendar.SECOND, 0);
            yesterdayC.set(Calendar.MILLISECOND, 0);
            yesterdayC.add(Calendar.DAY_OF_MONTH, -1);

            if (itemsList.get(position).getCallDate() == timeC.getTimeInMillis())
                titleText.setText(context.getString(R.string.list_title_today));
            else if (itemsList.get(position).getCallDate() == yesterdayC.getTimeInMillis())
                titleText.setText(context.getString(R.string.list_title_yesterday));
            else {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM, dd");
                String time = simpleDateFormat.format(itemsList.get(position).getCallDate());
                titleText.setText(time);
            }

        }
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

    public void setNewElements(List<CallRecord> newElements) {
        itemsList.addAll(new ArrayList<CallRecord>(newElements));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }


}

