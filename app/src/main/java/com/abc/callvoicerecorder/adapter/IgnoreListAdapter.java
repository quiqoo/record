package com.abc.callvoicerecorder.adapter;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import com.abc.callvoicerecorder.R;
import com.abc.callvoicerecorder.db.IgnoreContact;
import com.abc.callvoicerecorder.constant.Constants;
import com.abc.callvoicerecorder.constant.ConstantsColor;
import com.abc.callvoicerecorder.helper.ContactsHelper;
import com.abc.callvoicerecorder.helper.PaintIconsHelper;
import com.abc.callvoicerecorder.utils.RoundedCornersTransformation;
import com.abc.callvoicerecorder.utils.SharedPreferencesFile;


public class IgnoreListAdapter extends RecyclerView.Adapter<IgnoreListAdapter.ViewHolder> implements Constants, ConstantsColor {
    private final OnClickListener onclick;
    private final int displayHeight;
    private List<IgnoreContact> itemsList;
    private Context context;
    private int displayWidth;
    private Drawable iconBitmap;
    private ApplicationInfo ai;
    private View viewHeader;


    public interface OnClickListener{
        void onClick(IgnoreContact ignoreContactItem, View view, int position);
    }

    public IgnoreListAdapter(Context context, OnClickListener onclcik) {
        this.onclick = onclcik;
        this.itemsList = new ArrayList<>();
        this.context = context;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        displayWidth = display.getWidth();
        displayHeight = display.getHeight();
    }

    public void setLists(List<IgnoreContact> itemsList){
        this.itemsList = new ArrayList<>();
        this.itemsList.addAll(itemsList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ignore_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        SharedPreferencesFile.initSharedReferences(context);
        final View view = holder.view;
        final TextView callName = (TextView) view.findViewById(R.id.call_name);
        final TextView callNumber = (TextView) view.findViewById(R.id.call_number);
        final ImageView callContactPhoto = (ImageView) view.findViewById(R.id.img_call_contact_photo);

        callName.setTextColor(context.getResources().getColor(R.color.color_list_text_color));
        callNumber.setTextColor(context.getResources().getColor(R.color.color_list_text_color));
        ((ImageView) view.findViewById(R.id.img_delete_ignore)).setImageDrawable(PaintIconsHelper.recolorIcon(context, R.drawable.edit_record_delete, COLOR_ICONS[SharedPreferencesFile.getThemeNumber()]));

        callNumber.setText(itemsList.get(position).getCallNumber());
        String contactName = ContactsHelper.getContactName(itemsList.get(position).getCallNumber(), context);
        if (contactName.equals("")) {
            contactName = itemsList.get(position).getCallNumber();
            callNumber.setVisibility(View.GONE);
        }
        callName.setText(contactName);

        String contactPhoto = ContactsHelper.getContactPhoto(itemsList.get(position).getCallNumber(), context);
        if (contactPhoto.equals(""))
            Picasso.get().load(R.drawable.item_contact_icon).into(callContactPhoto);
        else
            Picasso.get().load(contactPhoto).error(R.drawable.item_contact_icon).transform(new RoundedCornersTransformation()).into(callContactPhoto);


//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onclick.onClick(itemsList.get(position), view, position);
//            }
//        });

        (view.findViewById(R.id.delete_ignore_layout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onclick.onClick(itemsList.get(position), view.findViewById(R.id.delete_ignore_layout), position);
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

    public void setNewElements(List<IgnoreContact> newElements) {
        itemsList.addAll(new ArrayList<IgnoreContact>(newElements));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }


}

